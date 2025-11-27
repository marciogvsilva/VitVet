// Mock do serviço de tipos de exames
import { mockData } from '../data.js';
import { delay, deepClone, NotFoundError } from '../mockHelpers.js';

/**
 * Lista todos os tipos de exames disponíveis
 */
export async function listarTiposExames() {
  await delay(300);
  return mockData.tiposExames.map(t => deepClone(t));
}

/**
 * Busca tipo de exame por ID
 */
export async function buscarTipoExame(id) {
  await delay(200);
  
  const tipoExame = mockData.tiposExames.find(t => t.id === parseInt(id));
  
  if (!tipoExame) {
    throw new NotFoundError(`Tipo de exame com ID ${id} não encontrado`);
  }
  
  return deepClone(tipoExame);
}

