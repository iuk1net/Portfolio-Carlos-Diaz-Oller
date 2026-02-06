/**
 * Gestor de Galería de Imágenes - Portfolio Social
 * Maneja subida, eliminación y establecimiento de imagen principal
 *
 * @author Portfolio Social v2.0
 * @version 1.0
 */

(function() {
    'use strict';

    class GaleriaManager {
        constructor() {
            this.baseUrl = '/api/proyectos';
            this.proyectoId = null;
            this.init();
        }

        init() {
            document.addEventListener('DOMContentLoaded', () => {
                const galeriaContainer = document.getElementById('galeriaContainer');
                if (galeriaContainer) {
                    this.proyectoId = galeriaContainer.dataset.proyectoId;
                    this.attachEventListeners();
                }
            });
        }

        attachEventListeners() {
            // Input para subir imágenes
            const inputFile = document.getElementById('galeriaInput');
            if (inputFile) {
                inputFile.addEventListener('change', async (e) => {
                    const files = Array.from(e.target.files);
                    if (files.length === 0) return;
                    await this.subirImagenes(files);
                    setTimeout(() => location.reload(), 1000);
                    e.target.value = '';
                });
            }

            // Botón de agregar imágenes
            const btnAgregar = document.getElementById('btnAgregarImagenes');
            if (btnAgregar) {
                btnAgregar.addEventListener('click', () => {
                    inputFile?.click();
                });
            }

            // Listeners en imágenes existentes
            this.attachImageListeners();
        }

        attachImageListeners() {
            // Eliminar imagen
            document.querySelectorAll('.btn-eliminar-imagen').forEach(btn => {
                btn.addEventListener('click', (e) => {
                    const index = parseInt(btn.dataset.index);
                    this.eliminarImagen(index);
                });
            });

            // Establecer como principal
            document.querySelectorAll('.btn-principal-imagen').forEach(btn => {
                btn.addEventListener('click', (e) => {
                    const index = parseInt(btn.dataset.index);
                    this.establecerPrincipal(index);
                });
            });
        }

        async subirImagenes(files) {
            const formData = new FormData();
            files.forEach(file => formData.append('files', file));
            const previewContainer = document.getElementById('galeriaPreview');
            files.forEach(file => {
                const preview = this.crearPreview(file);
                previewContainer?.appendChild(preview);
            });
            try {
                const response = await fetch(`${this.baseUrl}/${this.proyectoId}/imagenes`, {
                    method: 'POST',
                    body: formData,
                    credentials: 'same-origin'
                });
                const data = await response.json();
                if (data.success) {
                    this.mostrarNotificacion(data.mensaje, 'success');
                } else {
                    this.mostrarNotificacion(data.mensaje || 'Algunas imágenes no se subieron', 'error');
                }
            } catch (error) {
                this.mostrarNotificacion(error.message || 'Error al subir imágenes', 'error');
            }
        }

        async eliminarImagen(index) {
            if (!confirm('¿Eliminar esta imagen?')) return;

            try {
                const response = await fetch(`${this.baseUrl}/${this.proyectoId}/imagenes/${index}`, {
                    method: 'DELETE',
                    credentials: 'same-origin'
                });

                const data = await response.json();

                if (data.success) {
                    this.mostrarNotificacion(data.mensaje, 'success');
                    setTimeout(() => location.reload(), 500);
                } else {
                    throw new Error(data.error);
                }

            } catch (error) {
                console.error('Error:', error);
                this.mostrarNotificacion(error.message || 'Error al eliminar', 'error');
            }
        }

        async establecerPrincipal(index) {
            try {
                const response = await fetch(`${this.baseUrl}/${this.proyectoId}/imagenes/${index}/principal`, {
                    method: 'PUT',
                    credentials: 'same-origin'
                });

                const data = await response.json();

                if (data.success) {
                    this.mostrarNotificacion(data.mensaje, 'success');
                    setTimeout(() => location.reload(), 500);
                } else {
                    throw new Error(data.error);
                }

            } catch (error) {
                console.error('Error:', error);
                this.mostrarNotificacion(error.message || 'Error', 'error');
            }
        }

        crearPreview(file) {
            const div = document.createElement('div');
            div.className = 'col-md-4 mb-3';
            div.innerHTML = `
                <div class="card bg-dark">
                    <div class="card-body text-center">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Subiendo...</span>
                        </div>
                        <p class="mt-2 mb-0 text-muted">${file.name}</p>
                    </div>
                </div>
            `;
            return div;
        }

        mostrarNotificacion(mensaje, tipo = 'info') {
            let container = document.getElementById('toast-container');
            if (!container) {
                container = document.createElement('div');
                container.id = 'toast-container';
                container.style.cssText = `
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    z-index: 10000;
                `;
                document.body.appendChild(container);
            }

            const toast = document.createElement('div');
            toast.style.cssText = `
                background: ${tipo === 'success' ? '#10b981' : (tipo === 'error' ? '#ef4444' : '#6366f1')};
                color: white;
                padding: 1rem 1.5rem;
                border-radius: 8px;
                margin-bottom: 10px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
                animation: slideInRight 0.3s ease-out;
            `;

            const icon = tipo === 'success' ? '✅' : (tipo === 'error' ? '❌' : 'ℹ️');
            toast.innerHTML = `<span style="font-size: 1.2rem;">${icon}</span> <span>${mensaje}</span>`;

            container.appendChild(toast);

            setTimeout(() => {
                toast.style.animation = 'slideOutRight 0.3s ease-out';
                setTimeout(() => toast.remove(), 300);
            }, 3000);
        }
    }

    // Estilos
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideInRight {
            from { transform: translateX(100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
        @keyframes slideOutRight {
            from { transform: translateX(0); opacity: 1; }
            to { transform: translateX(100%); opacity: 0; }
        }
    `;
    document.head.appendChild(style);

    // Inicializar
    window.galeriaManager = new GaleriaManager();

})();
