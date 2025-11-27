/**
 * Facade de serviços - Escolhe automaticamente entre Mock e API Real
 * baseado na variável de ambiente VITE_USE_MOCK
 */

import { USE_MOCK } from '$lib/config';

// Importa serviços MOCK
import * as mockServices from '$lib/mocks/services';

// Importa serviços API REAL
import * as apiServices from '$lib/api/services';

// Exporta os serviços apropriados baseado na configuração
export const authService = USE_MOCK ? mockServices.authService : apiServices.authService;
export const tutorService = USE_MOCK ? mockServices.tutorService : apiServices.tutorService;
export const animalService = USE_MOCK ? mockServices.animalService : apiServices.animalService;
export const solicitacaoService = USE_MOCK ? mockServices.solicitacaoService : apiServices.solicitacaoService;
export const tipoExameService = USE_MOCK ? mockServices.tipoExameService : null; // API real não tem ainda

console.log(`🔌 Serviços carregados: ${USE_MOCK ? 'MOCK (Simulado)' : 'API REAL (Backend)'}`);

