// Mock do serviço de tutores
import { mockData, nextId } from '../data.js';
import { delay, NotFoundError, ValidationError, deepClone } from '../mockHelpers.js';

/**
 * Lista todos os tutores
 */
export async function listarTutores() {
  await delay();
  return mockData.tutores.map(t => deepClone(t));
}

/**
 * Busca tutor por ID
 */
export async function buscarTutor(id) {
  await delay(300);
  
  const tutor = mockData.tutores.find(t => t.id === parseInt(id));
  
  if (!tutor) {
    throw new NotFoundError(`Tutor com ID ${id} não encontrado`);
  }
  
  return deepClone(tutor);
}

/**
 * Cria novo tutor
 */
export async function criarTutor(dados) {
  await delay(600);
  
  // Validações
  if (!dados.nomeCompleto) {
    throw new ValidationError("Nome completo é obrigatório");
  }
  
  if (!dados.cpf) {
    throw new ValidationError("CPF é obrigatório");
  }
  
  if (!dados.telefone) {
    throw new ValidationError("Telefone é obrigatório");
  }
  
  // Verifica CPF duplicado
  const cpfExiste = mockData.tutores.some(t => t.cpf === dados.cpf);
  if (cpfExiste) {
    throw new ValidationError("CPF já cadastrado");
  }
  
  const novoTutor = {
    id: nextId.tutor++,
    nomeCompleto: dados.nomeCompleto,
    cpf: dados.cpf,
    email: dados.email || null,
    telefone: dados.telefone
  };
  
  mockData.tutores.push(novoTutor);
  
  return deepClone(novoTutor);
}

/**
 * Atualiza tutor
 */
export async function atualizarTutor(id, dados) {
  await delay(500);
  
  const tutor = mockData.tutores.find(t => t.id === parseInt(id));
  
  if (!tutor) {
    throw new NotFoundError(`Tutor com ID ${id} não encontrado`);
  }
  
  // Verifica CPF duplicado (se mudou)
  if (dados.cpf && dados.cpf !== tutor.cpf) {
    const cpfExiste = mockData.tutores.some(t => t.cpf === dados.cpf && t.id !== parseInt(id));
    if (cpfExiste) {
      throw new ValidationError("CPF já cadastrado");
    }
  }
  
  Object.assign(tutor, {
    nomeCompleto: dados.nomeCompleto ?? tutor.nomeCompleto,
    cpf: dados.cpf ?? tutor.cpf,
    email: dados.email ?? tutor.email,
    telefone: dados.telefone ?? tutor.telefone
  });
  
  return deepClone(tutor);
}

/**
 * Deleta tutor
 */
export async function deletarTutor(id) {
  await delay(400);
  
  const index = mockData.tutores.findIndex(t => t.id === parseInt(id));
  
  if (index === -1) {
    throw new NotFoundError(`Tutor com ID ${id} não encontrado`);
  }
  
  // Verifica se tem animais associados
  const temAnimais = mockData.animais.some(a => a.tutor.id === parseInt(id));
  if (temAnimais) {
    throw new ValidationError("Não é possível deletar tutor com animais cadastrados");
  }
  
  mockData.tutores.splice(index, 1);
  
  return { success: true };
}

