// Store de autenticação
import { writable } from 'svelte/store';
import { browser } from '$app/environment';

// Estado inicial
const initialState = {
  token: null,
  usuario: null,
  isAuthenticated: false
};

// Cria o store
function createAuthStore() {
  const { subscribe, set, update } = writable(initialState);

  // Recupera dados salvos do localStorage ao iniciar
  if (browser) {
    const savedToken = localStorage.getItem('vitvet_token');
    const savedUsuario = localStorage.getItem('vitvet_usuario');
    
    if (savedToken && savedUsuario) {
      try {
        set({
          token: savedToken,
          usuario: JSON.parse(savedUsuario),
          isAuthenticated: true
        });
      } catch (error) {
        console.error('Erro ao recuperar sessão:', error);
        localStorage.removeItem('vitvet_token');
        localStorage.removeItem('vitvet_usuario');
      }
    }
  }

  return {
    subscribe,
    
    /**
     * Salva dados de autenticação
     */
    login: (token, usuario) => {
      if (browser) {
        localStorage.setItem('vitvet_token', token);
        localStorage.setItem('vitvet_usuario', JSON.stringify(usuario));
      }
      
      set({
        token,
        usuario,
        isAuthenticated: true
      });
    },
    
    /**
     * Limpa dados de autenticação
     */
    logout: () => {
      if (browser) {
        localStorage.removeItem('vitvet_token');
        localStorage.removeItem('vitvet_usuario');
      }
      
      set(initialState);
    },
    
    /**
     * Atualiza dados do usuário
     */
    updateUsuario: (usuario) => {
      if (browser) {
        localStorage.setItem('vitvet_usuario', JSON.stringify(usuario));
      }
      
      update(state => ({
        ...state,
        usuario
      }));
    },
    
    /**
     * Reseta o store
     */
    reset: () => set(initialState)
  };
}

export const authStore = createAuthStore();

