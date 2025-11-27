import { writable } from 'svelte/store';

/**
 * Store para gerenciar toasts/notificações
 */
function createToastStore() {
  const { subscribe, update } = writable([]);

  let nextId = 1;

  return {
    subscribe,

    /**
     * Adiciona um toast de sucesso
     * @param {string} message 
     * @param {number} duration 
     */
    success(message, duration = 3000) {
      this.add(message, 'success', duration);
    },

    /**
     * Adiciona um toast de erro
     * @param {string} message 
     * @param {number} duration 
     */
    error(message, duration = 4000) {
      this.add(message, 'error', duration);
    },

    /**
     * Adiciona um toast informativo
     * @param {string} message 
     * @param {number} duration 
     */
    info(message, duration = 3000) {
      this.add(message, 'info', duration);
    },

    /**
     * Adiciona um toast genérico
     * @param {string} message 
     * @param {string} type 
     * @param {number} duration 
     */
    add(message, type = 'info', duration = 3000) {
      const id = nextId++;
      
      update(toasts => [...toasts, { id, message, type, duration }]);
    },

    /**
     * Remove um toast por ID
     * @param {number} id 
     */
    remove(id) {
      update(toasts => toasts.filter(t => t.id !== id));
    },

    /**
     * Limpa todos os toasts
     */
    clear() {
      update(() => []);
    }
  };
}

export const toastStore = createToastStore();

