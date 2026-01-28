/**
 * Módulo de Votación AJAX para Portfolio Social
 * Maneja la votación de proyectos sin recargar la página
 *
 * @author Portfolio Social v2.0
 * @version 1.0
 */

(function() {
    'use strict';

    /**
     * Clase VotacionManager
     * Gestiona todas las operaciones de votación AJAX
     */
    class VotacionManager {
        constructor() {
            this.baseUrl = '/api/votos';
            this.animationDuration = 300;
            this.init();
        }

        /**
         * Inicializa los event listeners
         */
        init() {
            // Delegar eventos a botones de votación
            document.addEventListener('DOMContentLoaded', () => {
                this.attachVoteButtonListeners();
            });
        }

        /**
         * Adjunta listeners a todos los botones de votación
         */
        attachVoteButtonListeners() {
            // Botones en detalle de proyecto
            const voteButton = document.getElementById('voteButton');
            if (voteButton) {
                voteButton.addEventListener('click', (e) => {
                    e.preventDefault();
                    const proyectoId = voteButton.dataset.proyectoId;
                    this.toggleVoto(proyectoId, voteButton);
                });
            }

            // Botones en lista de proyectos (múltiples)
            const voteButtons = document.querySelectorAll('.vote-btn-ajax');
            voteButtons.forEach(button => {
                button.addEventListener('click', (e) => {
                    e.preventDefault();
                    const proyectoId = button.dataset.proyectoId;
                    this.toggleVoto(proyectoId, button);
                });
            });
        }

        /**
         * Toggle de voto (votar o quitar voto)
         *
         * @param {number} proyectoId - ID del proyecto
         * @param {HTMLElement} button - Botón que disparó el evento
         */
        async toggleVoto(proyectoId, button) {
            // Deshabilitar botón mientras procesa
            button.disabled = true;
            button.classList.add('loading');

            try {
                const response = await fetch(`${this.baseUrl}/${proyectoId}/toggle`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    credentials: 'same-origin'
                });

                const data = await response.json();

                if (data.success) {
                    // Actualizar UI
                    this.actualizarUIVotacion(button, data);

                    // Mostrar notificación
                    this.mostrarNotificacion(data.mensaje, 'success');

                    // Animación de éxito
                    this.animarVoto(button, data.votado);
                } else {
                    throw new Error(data.error || 'Error al procesar el voto');
                }

            } catch (error) {
                console.error('Error en votación:', error);
                this.mostrarNotificacion(error.message || 'Error al votar. Por favor, intenta de nuevo.', 'error');
            } finally {
                // Rehabilitar botón
                button.disabled = false;
                button.classList.remove('loading');
            }
        }

        /**
         * Actualiza la interfaz de usuario después de votar
         *
         * @param {HTMLElement} button - Botón de votación
         * @param {Object} data - Datos de respuesta del servidor
         */
        actualizarUIVotacion(button, data) {
            const { votado, totalLikes } = data;

            // Actualizar estado del botón
            if (votado) {
                button.classList.add('voted');
                button.innerHTML = `<span class="vote-icon">💔</span> <span class="vote-text">Quitar Voto</span>`;
            } else {
                button.classList.remove('voted');
                button.innerHTML = `<span class="vote-icon">❤️</span> <span class="vote-text">Votar Proyecto</span>`;
            }

            // Actualizar contador de likes
            this.actualizarContadorLikes(button, totalLikes);
        }

        /**
         * Actualiza el contador de likes en la UI
         *
         * @param {HTMLElement} button - Botón de votación
         * @param {number} totalLikes - Total de likes actualizado
         */
        actualizarContadorLikes(button, totalLikes) {
            // Buscar contador en la misma card o en el sidebar
            const card = button.closest('.project-card, .col-lg-4');
            if (card) {
                const likeCounter = card.querySelector('.like-counter, .display-3');
                if (likeCounter) {
                    // Animación de cambio de número
                    likeCounter.style.transform = 'scale(1.3)';
                    likeCounter.textContent = totalLikes;

                    setTimeout(() => {
                        likeCounter.style.transform = 'scale(1)';
                    }, this.animationDuration);
                }
            }

            // Actualizar también en badge si existe
            const badge = card?.querySelector('.badge-likes');
            if (badge) {
                badge.textContent = `❤️ ${totalLikes}`;
            }
        }

        /**
         * Animación visual al votar
         *
         * @param {HTMLElement} button - Botón de votación
         * @param {boolean} votado - Si se añadió el voto (true) o se quitó (false)
         */
        animarVoto(button, votado) {
            if (votado) {
                // Animación de corazón pulsando
                button.style.transform = 'scale(1.1)';
                button.style.transition = 'all 0.3s ease';

                // Crear efecto de partículas (corazones flotantes)
                this.crearEfectoParticulas(button);

                setTimeout(() => {
                    button.style.transform = 'scale(1)';
                }, this.animationDuration);
            } else {
                // Animación suave de des-voto
                button.style.transform = 'scale(0.9)';
                setTimeout(() => {
                    button.style.transform = 'scale(1)';
                }, this.animationDuration);
            }
        }

        /**
         * Crea efecto de partículas (corazones flotantes) al votar
         *
         * @param {HTMLElement} button - Botón de votación
         */
        crearEfectoParticulas(button) {
            const rect = button.getBoundingClientRect();
            const particulas = 5;

            for (let i = 0; i < particulas; i++) {
                const particula = document.createElement('div');
                particula.textContent = '❤️';
                particula.style.cssText = `
                    position: fixed;
                    left: ${rect.left + rect.width / 2}px;
                    top: ${rect.top + rect.height / 2}px;
                    font-size: 1.5rem;
                    pointer-events: none;
                    z-index: 9999;
                    animation: floatUp 1s ease-out forwards;
                `;

                // Añadir variación en la animación
                particula.style.animationDelay = `${i * 0.1}s`;
                particula.style.transform = `translateX(${(Math.random() - 0.5) * 100}px)`;

                document.body.appendChild(particula);

                // Eliminar después de la animación
                setTimeout(() => {
                    particula.remove();
                }, 1000 + (i * 100));
            }
        }

        /**
         * Muestra una notificación toast
         *
         * @param {string} mensaje - Mensaje a mostrar
         * @param {string} tipo - Tipo de notificación (success, error, info)
         */
        mostrarNotificacion(mensaje, tipo = 'info') {
            // Verificar si existe un contenedor de notificaciones
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

            // Crear toast
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

        /**
         * Verifica el estado de votación de un proyecto
         * Útil para sincronizar el estado inicial
         *
         * @param {number} proyectoId - ID del proyecto
         * @returns {Promise<Object>} Estado del voto
         */
        async verificarEstadoVoto(proyectoId) {
            try {
                const response = await fetch(`${this.baseUrl}/${proyectoId}/estado`, {
                    method: 'GET',
                    credentials: 'same-origin'
                });

                return await response.json();
            } catch (error) {
                console.error('Error al verificar estado de voto:', error);
                return null;
            }
        }
    }

    // Agregar estilos CSS para las animaciones
    const style = document.createElement('style');
    style.textContent = `
        @keyframes floatUp {
            0% {
                opacity: 1;
                transform: translateY(0) scale(1);
            }
            100% {
                opacity: 0;
                transform: translateY(-100px) scale(0.5);
            }
        }

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

        .vote-btn-ajax.loading {
            opacity: 0.6;
            cursor: not-allowed;
        }

        .vote-btn-ajax.loading::after {
            content: '';
            display: inline-block;
            width: 14px;
            height: 14px;
            margin-left: 8px;
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-top-color: white;
            border-radius: 50%;
            animation: spin 0.6s linear infinite;
        }

        @keyframes spin {
            to { transform: rotate(360deg); }
        }

        .vote-icon {
            display: inline-block;
            transition: transform 0.2s ease;
        }

        .vote-btn-ajax:hover .vote-icon {
            transform: scale(1.2);
        }

        .like-counter {
            transition: transform 0.3s ease !important;
        }
    `;
    document.head.appendChild(style);

    // Inicializar el manager de votación
    window.votacionManager = new VotacionManager();

})();

