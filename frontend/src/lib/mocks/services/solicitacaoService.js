// Mock do serviço de solicitações de exames
import { mockData, nextId } from '../data.js';
import { 
  delay, 
  gerarProtocolo, 
  NotFoundError, 
  ValidationError,
  expandirReferencias,
  deepClone 
} from '../mockHelpers.js';

/**
 * Lista todas as solicitações (com filtros opcionais)
 */
export async function listarSolicitacoes(filtros = {}) {
  await delay();
  
  let resultado = [...mockData.solicitacoes];
  
  // Filtro por status
  if (filtros.status) {
    resultado = resultado.filter(s => s.status === filtros.status);
  }
  
  // Filtro por nome do animal
  if (filtros.animal) {
    resultado = resultado.filter(s => {
      const animal = mockData.animais.find(a => a.id === s.animal.id);
      return animal?.nome.toLowerCase().includes(filtros.animal.toLowerCase());
    });
  }
  
  // Expande referências
  return resultado.map(s => expandirReferencias(s, mockData));
}

/**
 * Busca solicitação por ID
 */
export async function buscarSolicitacao(id) {
  await delay(400);
  
  const solicitacao = mockData.solicitacoes.find(s => s.id === parseInt(id));
  
  if (!solicitacao) {
    throw new NotFoundError(`Solicitação com ID ${id} não encontrada`);
  }
  
  return expandirReferencias(solicitacao, mockData);
}

/**
 * Cria nova solicitação
 */
export async function criarSolicitacao(dados) {
  await delay(800);
  
  // Validações básicas
  if (!dados.suspeitaClinica) {
    throw new ValidationError("Suspeita clínica é obrigatória");
  }
  
  if (!dados.animal?.id) {
    throw new ValidationError("Animal é obrigatório");
  }
  
  if (!dados.veterinarioSolicitante?.id) {
    throw new ValidationError("Veterinário solicitante é obrigatório");
  }
  
  if (!dados.exames || dados.exames.length === 0) {
    throw new ValidationError("Selecione ao menos um exame");
  }
  
  const novaSolicitacao = {
    id: nextId.solicitacao++,
    protocolo: gerarProtocolo(),
    suspeitaClinica: dados.suspeitaClinica,
    status: "RECEBIDO",
    dataSolicitacao: new Date().toISOString(),
    animal: { id: dados.animal.id },
    veterinarioSolicitante: { id: dados.veterinarioSolicitante.id },
    exames: dados.exames.map(e => ({ id: e.id }))
  };
  
  mockData.solicitacoes.push(novaSolicitacao);
  
  // Cria notificação para patologistas
  const notificacao = {
    id: nextId.notificacao++,
    titulo: "Nova Solicitação",
    mensagem: `Nova solicitação de exame recebida - Protocolo ${novaSolicitacao.protocolo}`,
    lida: false,
    dataCriacao: new Date().toISOString(),
    destinatario: { id: 2 } // Patologista
  };
  mockData.notificacoes.push(notificacao);
  
  return expandirReferencias(novaSolicitacao, mockData);
}

/**
 * Atualiza status da solicitação
 */
export async function atualizarStatus(id, novoStatus) {
  await delay(500);
  
  const solicitacao = mockData.solicitacoes.find(s => s.id === parseInt(id));
  
  if (!solicitacao) {
    throw new NotFoundError(`Solicitação com ID ${id} não encontrada`);
  }
  
  const statusValidos = ["RECEBIDO", "EM_ANALISE", "CONCLUIDO", "CANCELADO"];
  if (!statusValidos.includes(novoStatus)) {
    throw new ValidationError(`Status inválido: ${novoStatus}`);
  }
  
  solicitacao.status = novoStatus;
  
  return expandirReferencias(solicitacao, mockData);
}

/**
 * Envia resultado/laudo
 */
export async function enviarResultado(id, observacoes, arquivo) {
  await delay(1200); // Upload demora mais
  
  const solicitacao = mockData.solicitacoes.find(s => s.id === parseInt(id));
  
  if (!solicitacao) {
    throw new NotFoundError(`Solicitação com ID ${id} não encontrada`);
  }
  
  // Simula upload do arquivo
  const urlFake = `/mock/laudos/laudo-${String(id).padStart(3, '0')}.pdf`;
  
  const resultado = {
    id: parseInt(id),
    observacoes: observacoes,
    urlLaudoPdf: urlFake,
    dataLaudo: new Date().toISOString(),
    solicitacao: { id: parseInt(id) },
    patologistaResponsavel: { id: 2 } // Mock: sempre patologista ID 2
  };
  
  mockData.resultados.push(resultado);
  solicitacao.resultado = resultado;
  solicitacao.status = "CONCLUIDO";
  
  // Cria notificação para o veterinário
  const notificacao = {
    id: nextId.notificacao++,
    titulo: "Resultado Disponível",
    mensagem: `O laudo da solicitação ${solicitacao.protocolo} está pronto`,
    lida: false,
    dataCriacao: new Date().toISOString(),
    destinatario: { id: solicitacao.veterinarioSolicitante.id }
  };
  mockData.notificacoes.push(notificacao);
  
  return resultado;
}

