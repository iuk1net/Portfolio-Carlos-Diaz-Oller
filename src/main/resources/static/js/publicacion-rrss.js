/**
 * Módulo de Publicación en Redes Sociales - Portfolio Social
 * Maneja la compartición de proyectos en redes sociales
 *
 * @author Portfolio Social v2.0
 * @version 1.0
 */

(function() {
    'use strict';

    /**
     * Clase PublicacionRRSSManager
     * Gestiona todas las operaciones de compartir en redes sociales
     */
    class PublicacionRRSSManager {
        constructor() {
            this.baseUrl = '/api/publicaciones';
            this.init();
        }

        /**
         * Inicializa los event listeners
         */
        init() {
            document.addEventListener('DOMContentLoaded', () => {
                this.attachShareButtonListeners();
                this.cargarHistorial();
            });
        }

        /**
         * Adjunta listeners a los botones de compartir
         */
        attachShareButtonListeners() {
            const shareButtons = document.querySelectorAll('.btn-share-rrss');
            shareButtons.forEach(button => {
                button.addEventListener('click', (e) => {
                    e.preventDefault();
                    const proyectoId = button.dataset.proyectoId;
                    const redSocial = button.dataset.redSocial;
                    this.compartirEnRedSocial(proyectoId, redSocial, button);
                });
            });
        }

        /**
         * Comparte un proyecto en una red social
         *
         * @param {number} proyectoId - ID del proyecto
         * @param {string} redSocial - Nombre de la red social
         * @param {HTMLElement} button - Botón que disparó el evento
         */
        async compartirEnRedSocial(proyectoId, redSocial, button) {
            // Deshabilitar botón
            button.disabled = true;
            button.classList.add('loading');
            const originalText = button.innerHTML;
            button.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span> Compartiendo...';

            try {
                const response = await fetch(`${this.baseUrl}/${proyectoId}/publicar?redSocial=${redSocial}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    credentials: 'same-origin'
                });

                const data = await response.json();

                if (data.success) {
                    // Mostrar notificación de éxito
                    this.mostrarNotificacion(data.mensaje, 'success');

                    // Actualizar el botón para mostrar que ya fue compartido
                    this.marcarComoCompartido(button, redSocial);

                    // Recargar historial
                    this.cargarHistorial();

                    // Animación de éxito
                    this.animarCompartido(button);
                } else {
                    throw new Error(data.error || 'Error al compartir');
                }

            } catch (error) {
                console.error('Error al compartir:', error);
                this.mostrarNotificacion(error.message || 'Error al compartir. Inténtalo de nuevo.', 'error');
                button.innerHTML = originalText;
            } finally {
                button.disabled = false;
                button.classList.remove('loading');
            }
        }

        /**
         * Carga el historial de publicaciones de un proyecto
         */
        async cargarHistorial() {
            const historialContainer = document.getElementById('historialPublicaciones');
            if (!historialContainer) return;

            const proyectoId = historialContainer.dataset.proyectoId;
            if (!proyectoId) return;

            try {
                const response = await fetch(`${this.baseUrl}/${proyectoId}/historial`, {
                    method: 'GET',
                    credentials: 'same-origin'
                });

                const data = await response.json();

                if (data.success) {
                    this.renderizarHistorial(data.publicaciones, historialContainer);
                }

            } catch (error) {
                console.error('Error al cargar historial:', error);
                historialContainer.innerHTML = '<p class="text-muted">Error al cargar el historial</p>';
            }
        }

        /**
         * Renderiza el historial de publicaciones en el DOM
         *
         * @param {Array} publicaciones - Lista de publicaciones
         * @param {HTMLElement} container - Contenedor del historial
         */
        renderizarHistorial(publicaciones, container) {
            if (!publicaciones || publicaciones.length === 0) {
                container.innerHTML = `
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle me-2"></i>
                        Este proyecto aún no ha sido compartido en redes sociales
                    </div>
                `;
                return;
            }

            let html = '<div class="list-group">';

            publicaciones.forEach(pub => {
                const estadoClass = this.obtenerClaseEstado(pub.estado);
                const estadoIcon = this.obtenerIconoEstado(pub.estado);
                const fecha = new Date(pub.fechaPublicacion).toLocaleString('es-ES');

                html += `
                    <div class="list-group-item list-group-item-action">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="mb-1">
                                    ${this.obtenerIconoRedSocial(pub.redSocial)} ${pub.redSocial}
                                </h6>
                                <small class="text-muted">
                                    <i class="far fa-clock me-1"></i>${fecha}
                                </small>
                            </div>
                            <div>
                                <span class="badge ${estadoClass}">
                                    ${estadoIcon} ${pub.estado}
                                </span>
                                ${pub.estado === 'ERROR' ? 
                                    `<button class="btn btn-sm btn-warning ms-2 btn-reintentar" 
                                             data-publicacion-id="${pub.id}">
                                        <i class="fas fa-redo"></i> Reintentar
                                    </button>` : ''}
                            </div>
                        </div>
                    </div>
                `;
            });

            html += '</div>';
            container.innerHTML = html;

            // Adjuntar listeners a botones de reintentar
            container.querySelectorAll('.btn-reintentar').forEach(btn => {
                btn.addEventListener('click', () => {
                    const publicacionId = btn.dataset.publicacionId;
                    this.reintentarPublicacion(publicacionId);
                });
            });
        }

        /**
         * Reintenta una publicación fallida
         *
         * @param {number} publicacionId - ID de la publicación
         */
        async reintentarPublicacion(publicacionId) {
            try {
                const response = await fetch(`${this.baseUrl}/${publicacionId}/reintentar`, {
                    method: 'POST',
                    credentials: 'same-origin'
                });

                const data = await response.json();

                if (data.success) {
                    this.mostrarNotificacion(data.mensaje, 'info');

                    // Recargar historial después de 1 segundo
                    setTimeout(() => {
                        this.cargarHistorial();
                    }, 1000);
                } else {
                    throw new Error(data.error);
                }

            } catch (error) {
                console.error('Error al reintentar:', error);
                this.mostrarNotificacion(error.message || 'Error al reintentar', 'error');
            }
        }

        /**
         * Marca un botón como compartido
         *
         * @param {HTMLElement} button - Botón de compartir
         * @param {string} redSocial - Nombre de la red social
         */
        marcarComoCompartido(button, redSocial) {
            button.innerHTML = `<i class="fas fa-check-circle me-1"></i> Compartido`;
            button.classList.add('btn-success');
            button.classList.remove(button.dataset.originalClass || 'btn-primary');
        }

        /**
         * Animación al compartir exitosamente
         *
         * @param {HTMLElement} button - Botón de compartir
         */
        animarCompartido(button) {
            button.style.transform = 'scale(1.1)';
            button.style.transition = 'all 0.3s ease';

            setTimeout(() => {
                button.style.transform = 'scale(1)';
            }, 300);
        }

        /**
         * Obtiene la clase CSS para el badge de estado
         *
         * @param {string} estado - Estado de la publicación
         * @returns {string} Clase CSS
         */
        obtenerClaseEstado(estado) {
            switch (estado) {
                case 'PUBLICADO': return 'bg-success';
                case 'PENDIENTE': return 'bg-warning text-dark';
                case 'ERROR': return 'bg-danger';
                default: return 'bg-secondary';
            }
        }

        /**
         * Obtiene el icono para el estado
         *
         * @param {string} estado - Estado de la publicación
         * @returns {string} HTML del icono
         */
        obtenerIconoEstado(estado) {
            switch (estado) {
                case 'PUBLICADO': return '<i class="fas fa-check-circle"></i>';
                case 'PENDIENTE': return '<i class="fas fa-clock"></i>';
                case 'ERROR': return '<i class="fas fa-exclamation-circle"></i>';
                default: return '<i class="fas fa-question-circle"></i>';
            }
        }

        /**
         * Obtiene el icono de la red social
         *
         * @param {string} redSocial - Nombre de la red social
         * @returns {string} HTML del icono
         */
        obtenerIconoRedSocial(redSocial) {
            switch (redSocial) {
                case 'LinkedIn': return '<i class="fab fa-linkedin"></i>';
                case 'Twitter': return '<i class="fab fa-twitter"></i>';
                case 'Facebook': return '<i class="fab fa-facebook"></i>';
                case 'Instagram': return '<i class="fab fa-instagram"></i>';
                case 'GitHub': return '<i class="fab fa-github"></i>';
                default: return '<i class="fas fa-share-alt"></i>';
            }
        }

        /**
         * Muestra una notificación toast
         *
         * @param {string} mensaje - Mensaje a mostrar
         * @param {string} tipo - Tipo de notificación (success, error, info)
         */
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
            toast.className = `toast toast-${tipo}`;
            toast.style.cssText = `
                background: ${tipo === 'success' ? '#10b981' : (tipo === 'error' ? '#ef4444' : '#6366f1')};
                color: white;
                padding: 1rem 1.5rem;
                border-radius: 8px;
                margin-bottom: 10px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
                animation: slideInRight 0.3s ease-out;
                display: flex;
                align-items: center;
                gap: 0.5rem;
                min-width: 250px;
            `;

            const icon = tipo === 'success' ? '✅' : (tipo === 'error' ? '❌' : 'ℹ️');
            toast.innerHTML = `<span style="font-size: 1.2rem;">${icon}</span> <span>${mensaje}</span>`;

            container.appendChild(toast);

            // Auto-remover después de 3 segundos
            setTimeout(() => {
                toast.style.animation = 'slideOutRight 0.3s ease-out';
                setTimeout(() => toast.remove(), 300);
            }, 3000);
        }
    }

    // Agregar estilos CSS
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideInRight {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }

        @keyframes slideOutRight {
            from {
                transform: translateX(0);
                opacity: 1;
            }
            to {
                transform: translateX(100%);
                opacity: 0;
            }
        }

        .btn-share-rrss.loading {
            opacity: 0.7;
            cursor: not-allowed;
        }
    `;
    document.head.appendChild(style);

    // Inicializar el manager
    window.publicacionRRSSManager = new PublicacionRRSSManager();

})();

