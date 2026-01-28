/**
 * Editor de Perfil - Portfolio Social
 * Maneja la gestión dinámica de enlaces a redes sociales
 *
 * @author Portfolio Social v2.0
 * @version 1.0
 */

(function() {
    'use strict';

    /**
     * Clase PerfilEditor
     * Gestiona la adición y eliminación dinámica de campos de enlaces
     */
    class PerfilEditor {
        constructor() {
            this.enlacesContainer = document.getElementById('enlacesContainer');
            this.btnAgregarEnlace = document.getElementById('btnAgregarEnlace');
            this.init();
        }

        /**
         * Inicializa los event listeners
         */
        init() {
            // Listener para agregar enlaces
            if (this.btnAgregarEnlace) {
                this.btnAgregarEnlace.addEventListener('click', () => {
                    this.agregarEnlace();
                });
            }

            // Listeners para eliminar enlaces existentes
            this.attachEliminarListeners();

            // Validación del formulario
            this.setupFormValidation();
        }

        /**
         * Agrega un nuevo campo de enlace
         */
        agregarEnlace() {
            const enlaceItem = document.createElement('div');
            enlaceItem.className = 'enlace-item';
            enlaceItem.innerHTML = `
                <input type="url" 
                       class="form-control enlace-input" 
                       name="enlacesRRSS" 
                       placeholder="https://linkedin.com/in/tu-perfil"
                       pattern="https?://.+"
                       title="Debe ser una URL válida (https://...)">
                <button type="button" class="btn btn-danger btn-eliminar-enlace">
                    🗑️
                </button>
            `;

            this.enlacesContainer.appendChild(enlaceItem);

            // Agregar listener al botón de eliminar
            const btnEliminar = enlaceItem.querySelector('.btn-eliminar-enlace');
            btnEliminar.addEventListener('click', () => {
                this.eliminarEnlace(enlaceItem);
            });

            // Animación de entrada
            enlaceItem.style.opacity = '0';
            enlaceItem.style.transform = 'translateX(-20px)';

            setTimeout(() => {
                enlaceItem.style.transition = 'all 0.3s ease';
                enlaceItem.style.opacity = '1';
                enlaceItem.style.transform = 'translateX(0)';
            }, 10);

            // Focus en el nuevo input
            enlaceItem.querySelector('input').focus();
        }

        /**
         * Elimina un campo de enlace
         * @param {HTMLElement} enlaceItem - Elemento a eliminar
         */
        eliminarEnlace(enlaceItem) {
            // Verificar que quede al menos un campo
            const totalEnlaces = this.enlacesContainer.querySelectorAll('.enlace-item').length;

            if (totalEnlaces <= 1) {
                // Si es el último, limpiar el input en lugar de eliminarlo
                const input = enlaceItem.querySelector('input');
                input.value = '';
                this.mostrarNotificacion('Limpiado el campo. Deja al menos un campo disponible.', 'info');
                return;
            }

            // Animación de salida
            enlaceItem.style.transition = 'all 0.3s ease';
            enlaceItem.style.opacity = '0';
            enlaceItem.style.transform = 'translateX(20px)';

            setTimeout(() => {
                enlaceItem.remove();
            }, 300);
        }

        /**
         * Adjunta listeners a los botones de eliminar existentes
         */
        attachEliminarListeners() {
            const botonesEliminar = document.querySelectorAll('.btn-eliminar-enlace');
            botonesEliminar.forEach(btn => {
                btn.addEventListener('click', (e) => {
                    const enlaceItem = e.target.closest('.enlace-item');
                    this.eliminarEnlace(enlaceItem);
                });
            });
        }

        /**
         * Configura la validación del formulario
         */
        setupFormValidation() {
            const form = document.getElementById('editarPerfilForm');

            if (form) {
                form.addEventListener('submit', (e) => {
                    if (!this.validarFormulario()) {
                        e.preventDefault();
                        return false;
                    }
                });
            }

            // Validación en tiempo real del nombre
            const nombreInput = document.getElementById('nombre');
            if (nombreInput) {
                nombreInput.addEventListener('input', () => {
                    this.validarNombre(nombreInput);
                });
            }

            // Validación de URLs en tiempo real
            this.enlacesContainer.addEventListener('input', (e) => {
                if (e.target.classList.contains('enlace-input')) {
                    this.validarURL(e.target);
                }
            });
        }

        /**
         * Valida el formulario completo antes del envío
         * @returns {boolean} true si es válido
         */
        validarFormulario() {
            let esValido = true;

            // Validar nombre
            const nombreInput = document.getElementById('nombre');
            if (!this.validarNombre(nombreInput)) {
                esValido = false;
            }

            // Validar URLs de enlaces
            const inputsEnlaces = document.querySelectorAll('.enlace-input');
            inputsEnlaces.forEach(input => {
                if (input.value.trim() !== '' && !this.validarURL(input)) {
                    esValido = false;
                }
            });

            if (!esValido) {
                this.mostrarNotificacion('Por favor, corrige los errores antes de guardar', 'error');
            }

            return esValido;
        }

        /**
         * Valida el campo de nombre
         * @param {HTMLInputElement} input - Input del nombre
         * @returns {boolean} true si es válido
         */
        validarNombre(input) {
            const valor = input.value.trim();

            if (valor === '') {
                this.marcarError(input, 'El nombre es obligatorio');
                return false;
            }

            if (valor.length < 3) {
                this.marcarError(input, 'El nombre debe tener al menos 3 caracteres');
                return false;
            }

            this.marcarExito(input);
            return true;
        }

        /**
         * Valida una URL
         * @param {HTMLInputElement} input - Input de la URL
         * @returns {boolean} true si es válido
         */
        validarURL(input) {
            const valor = input.value.trim();

            // Si está vacío, es válido (campo opcional)
            if (valor === '') {
                this.limpiarEstado(input);
                return true;
            }

            // Validar formato de URL
            try {
                const url = new URL(valor);
                if (url.protocol !== 'http:' && url.protocol !== 'https:') {
                    this.marcarError(input, 'La URL debe comenzar con http:// o https://');
                    return false;
                }
                this.marcarExito(input);
                return true;
            } catch {
                this.marcarError(input, 'URL no válida');
                return false;
            }
        }

        /**
         * Marca un input como error
         * @param {HTMLInputElement} input - Input a marcar
         * @param {string} mensaje - Mensaje de error
         */
        marcarError(input, mensaje) {
            input.style.borderColor = '#ef4444';
            input.style.boxShadow = '0 0 0 0.2rem rgba(239, 68, 68, 0.25)';

            // Eliminar mensaje anterior si existe
            this.eliminarMensajeError(input);

            // Agregar nuevo mensaje
            const errorMsg = document.createElement('div');
            errorMsg.className = 'error-message';
            errorMsg.style.cssText = 'color: #ef4444; font-size: 0.85rem; margin-top: 0.3rem;';
            errorMsg.textContent = '❌ ' + mensaje;
            input.parentElement.appendChild(errorMsg);
        }

        /**
         * Marca un input como exitoso
         * @param {HTMLInputElement} input - Input a marcar
         */
        marcarExito(input) {
            input.style.borderColor = '#10b981';
            input.style.boxShadow = '0 0 0 0.2rem rgba(16, 185, 129, 0.25)';
            this.eliminarMensajeError(input);
        }

        /**
         * Limpia el estado de un input
         * @param {HTMLInputElement} input - Input a limpiar
         */
        limpiarEstado(input) {
            input.style.borderColor = '';
            input.style.boxShadow = '';
            this.eliminarMensajeError(input);
        }

        /**
         * Elimina el mensaje de error de un input
         * @param {HTMLInputElement} input - Input
         */
        eliminarMensajeError(input) {
            const parent = input.parentElement;
            const errorMsg = parent.querySelector('.error-message');
            if (errorMsg) {
                errorMsg.remove();
            }
        }

        /**
         * Muestra una notificación toast
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

    // Agregar estilos de animación
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
    `;
    document.head.appendChild(style);

    // Inicializar el editor cuando el DOM esté listo
    document.addEventListener('DOMContentLoaded', () => {
        new PerfilEditor();
    });

})();

