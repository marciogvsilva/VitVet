// Funções auxiliares para o sistema de mocks

/**
 * Simula latência de rede
 * @param {number} ms - Milissegundos de delay (padrão: 500-1000ms aleatório)
 */
export function delay(ms = null) {
  const time = ms || Math.random() * 500 + 500; // 500-1000ms aleatório
  return new Promise(resolve => setTimeout(resolve, time));
}

/**
 * Gera um protocolo único para solicitações
 * @returns {string} Protocolo no formato YYYY-XXXX
 */
export function gerarProtocolo() {
  const ano = new Date().getFullYear();
  const random = Math.random().toString(36).substring(2, 6).toUpperCase();
  return `${ano}-${random}`;
}

/**
 * Gera um token JWT fake
 * @param {number} userId 
 * @returns {string} Token fake
 */
export function gerarTokenFake(userId) {
  const header = btoa(JSON.stringify({ alg: "HS256", typ: "JWT" }));
  const payload = btoa(JSON.stringify({ 
    sub: userId.toString(),
    iat: Math.floor(Date.now() / 1000),
    exp: Math.floor(Date.now() / 1000) + (60 * 60 * 24) // 24h
  }));
  const signature = btoa(`fake-signature-${userId}-${Date.now()}`);
  return `${header}.${payload}.${signature}`;
}

/**
 * Simula erro de autenticação
 */
export class UnauthorizedError extends Error {
  constructor(message = "Não autorizado") {
    super(message);
    this.name = "UnauthorizedError";
    this.status = 401;
  }
}

/**
 * Simula erro de não encontrado
 */
export class NotFoundError extends Error {
  constructor(message = "Recurso não encontrado") {
    super(message);
    this.name = "NotFoundError";
    this.status = 404;
  }
}

/**
 * Simula erro de validação
 */
export class ValidationError extends Error {
  constructor(message = "Dados inválidos") {
    super(message);
    this.name = "ValidationError";
    this.status = 400;
  }
}

/**
 * Clona objeto profundamente (evita mutação acidental)
 */
export function deepClone(obj) {
  return JSON.parse(JSON.stringify(obj));
}

/**
 * Expande referências (simula eager loading do JPA)
 * Por exemplo, transforma { animal: { id: 1 } } em { animal: { id: 1, nome: "Pipoca", ... } }
 */
export function expandirReferencias(obj, mockData) {
  const result = deepClone(obj);
  
  // Expande animal
  if (result.animal?.id) {
    const animal = mockData.animais.find(a => a.id === result.animal.id);
    if (animal) {
      result.animal = deepClone(animal);
      
      // Expande tutor dentro do animal
      if (result.animal.tutor?.id) {
        const tutor = mockData.tutores.find(t => t.id === result.animal.tutor.id);
        if (tutor) {
          result.animal.tutor = deepClone(tutor);
        }
      }
    }
  }
  
  // Expande veterinario
  if (result.veterinarioSolicitante?.id) {
    const vet = mockData.usuarios.find(u => u.id === result.veterinarioSolicitante.id);
    if (vet) {
      result.veterinarioSolicitante = deepClone(vet);
      delete result.veterinarioSolicitante.senha; // Remove senha
    }
  }
  
  // Expande exames
  if (result.exames?.length) {
    result.exames = result.exames.map(exame => {
      if (exame.id) {
        const tipoExame = mockData.tiposExames.find(t => t.id === exame.id);
        return tipoExame ? deepClone(tipoExame) : exame;
      }
      return exame;
    });
  }
  
  // Expande resultado
  if (result.resultado) {
    const resultadoCompleto = mockData.resultados.find(r => r.id === obj.id);
    if (resultadoCompleto) {
      result.resultado = deepClone(resultadoCompleto);
      
      // Expande patologista no resultado
      if (result.resultado.patologistaResponsavel?.id) {
        const pato = mockData.usuarios.find(u => u.id === result.resultado.patologistaResponsavel.id);
        if (pato) {
          result.resultado.patologistaResponsavel = deepClone(pato);
          delete result.resultado.patologistaResponsavel.senha;
        }
      }
    }
  }
  
  return result;
}

/**
 * Valida token fake
 */
export function validarToken(token) {
  if (!token || !token.startsWith('Bearer ')) {
    throw new UnauthorizedError("Token não fornecido");
  }
  
  // Em mock, qualquer token válido estruturalmente é aceito
  const tokenValue = token.replace('Bearer ', '');
  const parts = tokenValue.split('.');
  
  if (parts.length !== 3) {
    throw new UnauthorizedError("Token inválido");
  }
  
  return true;
}

/**
 * Extrai userId do token fake
 */
export function getUserIdFromToken(token) {
  try {
    const tokenValue = token.replace('Bearer ', '');
    const payload = JSON.parse(atob(tokenValue.split('.')[1]));
    return parseInt(payload.sub);
  } catch (error) {
    throw new UnauthorizedError("Token inválido");
  }
}

