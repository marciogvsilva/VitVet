// Funções de validação

/**
 * Valida CPF (apenas formato e dígitos verificadores)
 */
export function validarCPF(cpf) {
  if (!cpf) return { valido: false, erro: 'CPF é obrigatório' };
  
  // Remove caracteres não numéricos
  const cpfLimpo = cpf.replace(/\D/g, '');
  
  if (cpfLimpo.length !== 11) {
    return { valido: false, erro: 'CPF deve ter 11 dígitos' };
  }
  
  // Verifica se todos os dígitos são iguais
  if (/^(\d)\1{10}$/.test(cpfLimpo)) {
    return { valido: false, erro: 'CPF inválido' };
  }
  
  // Validação dos dígitos verificadores
  let soma = 0;
  let resto;
  
  for (let i = 1; i <= 9; i++) {
    soma += parseInt(cpfLimpo.substring(i - 1, i)) * (11 - i);
  }
  
  resto = (soma * 10) % 11;
  if (resto === 10 || resto === 11) resto = 0;
  if (resto !== parseInt(cpfLimpo.substring(9, 10))) {
    return { valido: false, erro: 'CPF inválido' };
  }
  
  soma = 0;
  for (let i = 1; i <= 10; i++) {
    soma += parseInt(cpfLimpo.substring(i - 1, i)) * (12 - i);
  }
  
  resto = (soma * 10) % 11;
  if (resto === 10 || resto === 11) resto = 0;
  if (resto !== parseInt(cpfLimpo.substring(10, 11))) {
    return { valido: false, erro: 'CPF inválido' };
  }
  
  return { valido: true, erro: null };
}

/**
 * Formata CPF (000.000.000-00)
 */
export function formatarCPF(cpf) {
  const cpfLimpo = cpf.replace(/\D/g, '');
  return cpfLimpo
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
}

/**
 * Valida telefone
 */
export function validarTelefone(telefone) {
  if (!telefone) return { valido: false, erro: 'Telefone é obrigatório' };
  
  const telefoneLimpo = telefone.replace(/\D/g, '');
  
  if (telefoneLimpo.length < 10 || telefoneLimpo.length > 11) {
    return { valido: false, erro: 'Telefone deve ter 10 ou 11 dígitos' };
  }
  
  return { valido: true, erro: null };
}

/**
 * Formata telefone (00) 00000-0000
 */
export function formatarTelefone(telefone) {
  const telefoneLimpo = telefone.replace(/\D/g, '');
  
  if (telefoneLimpo.length === 11) {
    return telefoneLimpo.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
  } else if (telefoneLimpo.length === 10) {
    return telefoneLimpo.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
  }
  
  return telefone;
}

/**
 * Valida email
 */
export function validarEmail(email) {
  if (!email) return { valido: true, erro: null }; // Email é opcional
  
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  
  if (!regex.test(email)) {
    return { valido: false, erro: 'Email inválido' };
  }
  
  return { valido: true, erro: null };
}

/**
 * Valida campo obrigatório
 */
export function validarObrigatorio(valor, nomeCampo) {
  if (!valor || (typeof valor === 'string' && !valor.trim())) {
    return { valido: false, erro: `${nomeCampo} é obrigatório` };
  }
  return { valido: true, erro: null };
}

/**
 * Valida data de nascimento
 */
export function validarDataNascimento(data) {
  if (!data) return { valido: true, erro: null }; // Opcional
  
  const dataNasc = new Date(data);
  const hoje = new Date();
  
  if (dataNasc > hoje) {
    return { valido: false, erro: 'Data não pode ser futura' };
  }
  
  const idade = (hoje - dataNasc) / (1000 * 60 * 60 * 24 * 365);
  if (idade > 50) {
    return { valido: false, erro: 'Data parece incorreta (mais de 50 anos)' };
  }
  
  return { valido: true, erro: null };
}

