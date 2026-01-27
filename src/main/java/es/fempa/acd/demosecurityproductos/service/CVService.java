package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.CV;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.CVRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de CVs
 * Implementa las operaciones definidas en el UML: subirCV(), descargarCV(), eliminarCV()
 */
@Service
public class CVService {

    private final CVRepository cvRepository;
    private final Path uploadPath;

    // Tamaño máximo permitido: 10 MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    // Formatos permitidos según documentación
    private static final List<String> ALLOWED_EXTENSIONS = List.of("pdf", "docx", "txt");

    /**
     * Constructor con inyección de dependencias
     * @param cvRepository repositorio de CVs
     * @param uploadDirectory directorio base para subida de archivos
     */
    public CVService(CVRepository cvRepository,
                     @Value("${app.upload.dir:uploads/cvs}") String uploadDirectory) {
        this.cvRepository = cvRepository;
        this.uploadPath = Paths.get(uploadDirectory);
        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de subida de CVs", e);
        }
    }

    /**
     * Sube un nuevo archivo de CV al servidor
     * Método definido en UML
     *
     * @param file archivo multipart del CV
     * @param usuario usuario propietario del CV
     * @return CV guardado en base de datos
     * @throws IllegalArgumentException si el archivo no es válido
     * @throws IOException si hay error al guardar el archivo
     */
    @Transactional
    public CV subirCV(MultipartFile file, Usuario usuario) throws IOException {
        // Validaciones
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo excede el tamaño máximo permitido de 10 MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Nombre de archivo inválido");
        }

        // Obtener extensión del archivo
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException(
                "Formato de archivo no permitido. Formatos válidos: PDF, DOCX, TXT"
            );
        }

        // Crear directorio del usuario si no existe
        Path userDirectory = uploadPath.resolve(String.valueOf(usuario.getId()));
        Files.createDirectories(userDirectory);

        // Generar nombre único para el archivo: {id_usuario}_{timestamp}.{extension}
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = usuario.getId() + "_" + timestamp + "." + extension;
        Path filePath = userDirectory.resolve(filename);

        // Guardar archivo en el servidor
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Crear entidad CV
        CV cv = new CV();
        cv.setTipoArchivo(extension.toUpperCase());
        cv.setRutaServidor(filePath.toString());
        cv.setUsuario(usuario);

        // Guardar en base de datos
        return cvRepository.save(cv);
    }

    /**
     * Descarga el CV del usuario
     * Método definido en UML
     *
     * @param cvId ID del CV a descargar
     * @param username nombre de usuario autenticado (para verificar permisos)
     * @return Resource del archivo para descarga
     * @throws IllegalArgumentException si el CV no existe
     * @throws AccessDeniedException si el usuario no tiene permisos
     * @throws IOException si hay error al leer el archivo
     */
    public Resource descargarCV(Long cvId, String username) throws IOException {
        // Buscar el CV
        CV cv = cvRepository.findById(cvId)
            .orElseThrow(() -> new IllegalArgumentException("CV no encontrado"));

        // Verificar permisos: solo el propietario puede descargar su CV
        if (!cv.getUsuario().getUsername().equals(username)) {
            throw new AccessDeniedException("No tienes permisos para descargar este CV");
        }

        // Obtener el archivo
        Path filePath = Paths.get(cv.getRutaServidor());
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new IOException("No se pudo leer el archivo del CV");
        }

        return resource;
    }

    /**
     * Elimina el archivo del servidor y el registro de base de datos
     * Método definido en UML
     *
     * @param cvId ID del CV a eliminar
     * @param username nombre de usuario autenticado (para verificar permisos)
     * @throws IllegalArgumentException si el CV no existe
     * @throws AccessDeniedException si el usuario no tiene permisos
     * @throws IOException si hay error al eliminar el archivo físico
     */
    @Transactional
    public void eliminarCV(Long cvId, String username) throws IOException {
        // Buscar el CV
        CV cv = cvRepository.findById(cvId)
            .orElseThrow(() -> new IllegalArgumentException("CV no encontrado"));

        // Verificar permisos: solo el propietario puede eliminar su CV
        if (!cv.getUsuario().getUsername().equals(username)) {
            throw new AccessDeniedException("No tienes permisos para eliminar este CV");
        }

        // Eliminar archivo físico del servidor
        Path filePath = Paths.get(cv.getRutaServidor());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new IOException("Error al eliminar el archivo físico del CV", e);
        }

        // Eliminar registro de base de datos
        cvRepository.delete(cv);
    }

    /**
     * Lista todos los CVs de un usuario ordenados por fecha (más reciente primero)
     *
     * @param usuario usuario propietario
     * @return lista de CVs del usuario
     */
    public List<CV> listarCVsPorUsuario(Usuario usuario) {
        return cvRepository.findByUsuarioOrderByFechaSubidaDesc(usuario);
    }

    /**
     * Obtiene el CV más reciente de un usuario
     *
     * @param usuario usuario propietario
     * @return Optional con el CV más reciente, o vacío si no existe
     */
    public Optional<CV> obtenerCVMasReciente(Usuario usuario) {
        return cvRepository.findFirstByUsuarioOrderByFechaSubidaDesc(usuario);
    }

    /**
     * Obtiene un CV por su ID
     *
     * @param cvId ID del CV
     * @return Optional con el CV encontrado
     */
    public Optional<CV> obtenerCVPorId(Long cvId) {
        return cvRepository.findById(cvId);
    }

    /**
     * Extrae la extensión de un nombre de archivo
     *
     * @param filename nombre del archivo
     * @return extensión del archivo sin el punto
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}

