// Mock do serviço de autenticação
import { mockData } from '../data.js';
import { delay, gerarTokenFake, UnauthorizedError, deepClone } from '../mockHelpers.js';

/**
 * Realiza login (mock)
 * @param {string} email 
 * @param {string} senha 
 * @returns {Promise<{token: string}>}
 */
export async function login(email, senha) {
  await delay(600);
  
  const usuario = mockData.usuarios.find(
    u => u.email === email && u.senha === senha
  );
  
  if (!usuario) {
    throw new UnauthorizedError("Credenciais inválidas");
  }
  
  const token = gerarTokenFake(usuario.id);
  
  // Salva token no sessionStorage (simula backend)
  sessionStorage.setItem(`mock_token_user_${usuario.id}`, token);
  
  return {
    token: token
  };
}

/**
 * Busca dados do usuário pelo token
 * Útil para recuperar sessão
 */
export async function getUsuarioLogado(token) {
  await delay(200);
  
  // Extrai ID do payload do token fake
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const userId = parseInt(payload.sub);
    
    const usuario = mockData.usuarios.find(u => u.id === userId);
    
    if (!usuario) {
      throw new UnauthorizedError("Usuário não encontrado");
    }
    
    const usuarioSemSenha = deepClone(usuario);
    delete usuarioSemSenha.senha;
    
    return usuarioSemSenha;
  } catch (error) {
    throw new UnauthorizedError("Token inválido");
  }
}

/**
 * Logout (mock) - apenas limpa dados locais
 */
export async function logout() {
  await delay(100);
  // Em mock, não há muito o que fazer no "servidor"
  return { success: true };
}

