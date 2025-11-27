import httpClient from '../httpClient';

/**
 * Serviço de animais - Chamadas reais ao backend
 */
export const animalService = {
  /**
   * Lista todos os animais
   * @returns {Promise<Array>}
   */
  async listarTodos() {
    try {
      const response = await httpClient.get('/api/animais');
      return response.data;
    } catch (error) {
      console.error('Erro ao listar animais:', error);
      throw error;
    }
  },

  /**
   * Busca um animal por ID
   * @param {number} id 
   * @returns {Promise<object>}
   */
  async buscarPorId(id) {
    try {
      const response = await httpClient.get(`/api/animais/${id}`);
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar animal:', error);
      throw error;
    }
  },

  /**
   * Cria um novo animal
   * @param {object} animal 
   * @returns {Promise<object>}
   */
  async criar(animal) {
    try {
      const response = await httpClient.post('/api/animais', animal);
      return response.data;
    } catch (error) {
      console.error('Erro ao criar animal:', error);
      throw error;
    }
  },

  /**
   * Atualiza um animal existente
   * @param {number} id 
   * @param {object} animal 
   * @returns {Promise<object>}
   */
  async atualizar(id, animal) {
    try {
      const response = await httpClient.put(`/api/animais/${id}`, animal);
      return response.data;
    } catch (error) {
      console.error('Erro ao atualizar animal:', error);
      throw error;
    }
  },

  /**
   * Remove um animal
   * @param {number} id 
   * @returns {Promise<void>}
   */
  async deletar(id) {
    try {
      await httpClient.delete(`/api/animais/${id}`);
    } catch (error) {
      console.error('Erro ao deletar animal:', error);
      throw error;
    }
  }
};

export default animalService;

