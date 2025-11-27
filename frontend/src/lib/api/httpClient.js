import axios from 'axios';
import { authStore } from '$lib/stores/auth';
import { get } from 'svelte/store';
import { goto } from '$app/navigation';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

// Criar instância do Axios
const httpClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  }
});

// Interceptor de Request - Adiciona JWT
httpClient.interceptors.request.use(
  (config) => {
    const auth = get(authStore);
    
    if (auth.token) {
      config.headers.Authorization = `Bearer ${auth.token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor de Response - Tratamento de erros
httpClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response) {
      const { status, data } = error.response;
      
      switch (status) {
        case 401:
          // Token inválido ou expirado
          authStore.logout();
          goto('/');
          throw new Error('Sessão expirada. Faça login novamente.');
        
        case 403:
          throw new Error('Você não tem permissão para realizar esta ação.');
        
        case 404:
          throw new Error('Recurso não encontrado.');
        
        case 500:
          throw new Error('Erro interno do servidor. Tente novamente mais tarde.');
        
        default:
          throw new Error(data?.message || 'Erro ao processar sua solicitação.');
      }
    } else if (error.request) {
      // Sem resposta do servidor
      throw new Error('Não foi possível conectar ao servidor. Verifique sua conexão.');
    } else {
      throw new Error(error.message || 'Erro desconhecido.');
    }
  }
);

export default httpClient;

