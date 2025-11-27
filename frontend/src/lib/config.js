/**
 * Configuração global do frontend
 */

// Define se deve usar serviços mock ou API real
export const USE_MOCK = import.meta.env.VITE_USE_MOCK === 'true';

// URL base da API
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

// Configurações de desenvolvimento
export const IS_DEV = import.meta.env.DEV;
export const IS_PROD = import.meta.env.PROD;

console.log('🔧 Configuração do Frontend:');
console.log(`   - Modo: ${IS_DEV ? 'Desenvolvimento' : 'Produção'}`);
console.log(`   - API Base URL: ${API_BASE_URL}`);
console.log(`   - Usando Mocks: ${USE_MOCK ? 'Sim' : 'Não (API Real)'}`);

