import httpClient from '../httpClient';

/**
 * Serviço de solicitações - Chamadas reais ao backend
 */
export const solicitacaoService = {
  /**
   * Lista todas as solicitações com filtros opcionais
   * @param {object} filtros - { status?, nomeAnimal? }
   * @returns {Promise<Array>}
   */
  async listar(filtros = {}) {
    try {
      const params = {};
      if (filtros.status) params.status = filtros.status;
      if (filtros.nomeAnimal) params.animal = filtros.nomeAnimal;

      const response = await httpClient.get('/api/solicitacoes/listar', { params });
      return response.data;
    } catch (error) {
      console.error('Erro ao listar solicitações:', error);
      throw error;
    }
  },

  /**
   * Busca uma solicitação por ID
   * Nota: Este endpoint pode não existir no backend ainda
   * @param {number} id 
   * @returns {Promise<object>}
   */
  async buscarPorId(id) {
    try {
      // Tenta buscar diretamente
      const response = await httpClient.get(`/api/solicitacoes/${id}`);
      return response.data;
    } catch (error) {
      // Se não existir endpoint específico, busca todas e filtra
      console.warn('Endpoint /api/solicitacoes/{id} não encontrado, buscando via listar');
      const todas = await this.listar();
      const solicitacao = todas.find(s => s.id === parseInt(id));
      
      if (!solicitacao) {
        throw new Error('Solicitação não encontrada');
      }
      
      return solicitacao;
    }
  },

  /**
   * Cria uma nova solicitação
   * @param {object} dados - { animalId, tiposExame[], suspeitaClinica, veterinarioId }
   * @returns {Promise<object>}
   */
  async criar(dados) {
    try {
      const response = await httpClient.post('/api/solicitacoes', dados);
      return response.data;
    } catch (error) {
      console.error('Erro ao criar solicitação:', error);
      throw error;
    }
  },

  /**
   * Atualiza o status de uma solicitação (Patologista)
   * @param {number} id 
   * @param {string} novoStatus - "RECEBIDO" | "EM_ANALISE" | "CONCLUIDO"
   * @returns {Promise<object>}
   */
  async atualizarStatus(id, novoStatus) {
    try {
      const response = await httpClient.put(
        `/api/solicitacoes/${id}/status`,
        novoStatus,
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      return response.data;
    } catch (error) {
      console.error('Erro ao atualizar status:', error);
      throw error;
    }
  },

  /**
   * Envia o laudo (PDF + observações) para uma solicitação
   * @param {number} id 
   * @param {File} arquivoPDF 
   * @param {string} observacoes 
   * @returns {Promise<object>}
   */
  async adicionarLaudo(id, arquivoPDF, observacoes) {
    try {
      const formData = new FormData();
      formData.append('arquivo', arquivoPDF);
      formData.append('observacoes', observacoes);

      const response = await httpClient.post(
        `/api/solicitacoes/${id}/resultado`,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      );
      return response.data;
    } catch (error) {
      console.error('Erro ao adicionar laudo:', error);
      throw error;
    }
  }
};

export default solicitacaoService;

