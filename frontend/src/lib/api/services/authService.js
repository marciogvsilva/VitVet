import httpClient from '../httpClient';

/**
 * Serviço de autenticação - Chamadas reais ao backend
 */
export const authService = {
  /**
   * Realiza login no sistema
   * @param {string} email 
   * @param {string} senha 
   * @returns {Promise<{token: string, usuario: object}>}
   */
  async login(email, senha) {
    try {
      const response = await httpClient.post('/api/auth/login', {
        email,
        senha
      });

      const { token } = response.data;

      // Decodificar o token JWT para extrair informações do usuário
      const usuario = decodeJWT(token);

      return {
        token,
        usuario: {
          email: usuario.sub, // subject do JWT (geralmente é o email/username)
          papel: usuario.authorities?.[0] || usuario.role, // role/authority do usuário
          nome: usuario.nome || email.split('@')[0] // nome se disponível
        }
      };
    } catch (error) {
      console.error('Erro ao fazer login:', error);
      throw error;
    }
  },

  /**
   * Faz logout (apenas local, backend não tem endpoint)
   */
  logout() {
    // Apenas limpa o token localmente
    return Promise.resolve();
  }
};

/**
 * Decodifica um token JWT (sem validar assinatura)
 * @param {string} token 
 * @returns {object}
 */
function decodeJWT(token) {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error('Erro ao decodificar JWT:', error);
    return {};
  }
}

export default authService;

