// Mock do serviço de animais
import { mockData, nextId } from '../data.js';
import { delay, NotFoundError, ValidationError, deepClone, expandirReferencias } from '../mockHelpers.js';

/**
 * Lista todos os animais
 */
export async function listarAnimais() {
  await delay();
  
  return mockData.animais.map(animal => {
    const result = deepClone(animal);
    // Expande tutor
    if (result.tutor?.id) {
      const tutor = mockData.tutores.find(t => t.id === result.tutor.id);
      if (tutor) {
        result.tutor = deepClone(tutor);
      }
    }
    return result;
  });
}

/**
 * Busca animal por ID
 */
export async function buscarAnimal(id) {
  await delay(300);
  
  const animal = mockData.animais.find(a => a.id === parseInt(id));
  
  if (!animal) {
    throw new NotFoundError(`Animal com ID ${id} não encontrado`);
  }
  
  const result = deepClone(animal);
  
  // Expande tutor
  if (result.tutor?.id) {
    const tutor = mockData.tutores.find(t => t.id === result.tutor.id);
    if (tutor) {
      result.tutor = deepClone(tutor);
    }
  }
  
  return result;
}

/**
 * Cria novo animal
 */
export async function criarAnimal(dados) {
  await delay(600);
  
  // Validações
  if (!dados.nome) {
    throw new ValidationError("Nome do animal é obrigatório");
  }
  
  if (!dados.especie) {
    throw new ValidationError("Espécie é obrigatória");
  }
  
  if (!dados.tutor?.id) {
    throw new ValidationError("Tutor é obrigatório");
  }
  
  const novoAnimal = {
    id: nextId.animal++,
    nome: dados.nome,
    rgAnimal: dados.rgAnimal || null,
    especie: dados.especie,
    raca: dados.raca || null,
    sexo: dados.sexo || null,
    dataNascimento: dados.dataNascimento || null,
    tutor: { id: dados.tutor.id }
  };
  
  mockData.animais.push(novoAnimal);
  
  return buscarAnimal(novoAnimal.id);
}

/**
 * Atualiza animal
 */
export async function atualizarAnimal(id, dados) {
  await delay(500);
  
  const animal = mockData.animais.find(a => a.id === parseInt(id));
  
  if (!animal) {
    throw new NotFoundError(`Animal com ID ${id} não encontrado`);
  }
  
  Object.assign(animal, {
    nome: dados.nome ?? animal.nome,
    rgAnimal: dados.rgAnimal ?? animal.rgAnimal,
    especie: dados.especie ?? animal.especie,
    raca: dados.raca ?? animal.raca,
    sexo: dados.sexo ?? animal.sexo,
    dataNascimento: dados.dataNascimento ?? animal.dataNascimento,
    tutor: dados.tutor?.id ? { id: dados.tutor.id } : animal.tutor
  });
  
  return buscarAnimal(id);
}

/**
 * Deleta animal
 */
export async function deletarAnimal(id) {
  await delay(400);
  
  const index = mockData.animais.findIndex(a => a.id === parseInt(id));
  
  if (index === -1) {
    throw new NotFoundError(`Animal com ID ${id} não encontrado`);
  }
  
  mockData.animais.splice(index, 1);
  
  return { success: true };
}

