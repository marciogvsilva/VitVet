import httpClient from '../httpClient';

/**
 * Serviço de tutores - Chamadas reais ao backend
 */
export const tutorService = {
  /**
   * Lista todos os tutores
   * @returns {Promise<Array>}
   */
  async listarTodos() {
    try {
      const response = await httpClient.get('/api/tutores');
      return response.data;
    } catch (error) {
      console.error('Erro ao listar tutores:', error);
      throw error;
    }
  },

  /**
   * Cria um novo tutor
   * @param {object} tutor 
   * @returns {Promise<object>}
   */
  async criar(tutor) {
    try {
      const response = await httpClient.post('/api/tutores', tutor);
      return response.data;
    } catch (error) {
      console.error('Erro ao criar tutor:', error);
      throw error;
    }
  }
};

export default tutorService;

