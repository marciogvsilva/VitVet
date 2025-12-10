<script>
import { page } from '$app/stores';
import { goto } from '$app/navigation';
import { onMount } from 'svelte';
import { authStore } from '$lib/stores/auth';
import { authService } from '$lib/services';
import ToastContainer from '$lib/components/ToastContainer.svelte';

// Importando ícones do svelte-icons
import FaHome from 'svelte-icons/fa/FaHome.svelte';
import FaClipboardList from 'svelte-icons/fa/FaClipboardList.svelte';
import FaPlus from 'svelte-icons/fa/FaPlus.svelte';
import FaCog from 'svelte-icons/fa/FaCog.svelte';
import FaSignOutAlt from 'svelte-icons/fa/FaSignOutAlt.svelte';

// Props para renderizar conteúdo filho
let { children } = $props();

// Estado de autenticação
let isAuthenticated = $derived($authStore.isAuthenticated);
let usuario = $derived($authStore.usuario);

// Determina o tipo de usuário a partir da URL usando derived do Svelte 5
let path = $derived($page.url.pathname);

// Define o tipo de usuário baseado no caminho
let userType = $derived(
  path.includes('patologista') ? 'patologista' : 
  path.includes('veterinario') ? 'veterinario' : null
);

// Determina a página atual para destacar o item do menu
let isHomePage = $derived(path.endsWith('/home') || path === `/${userType}`);
let isSolicitacoesPage = $derived(path.includes('/solicitacoes'));
let isNovaSolicitacaoPage = $derived(path.includes('/nova-solicitacao'));

// Define cores baseadas no tipo de usuário
let primaryColor = $derived(
  userType === 'patologista' ? 'var(--color-primary-blue)' :
  userType === 'veterinario' ? 'var(--color-primary-green)' : null
);

let secondaryColor = $derived(
  userType === 'patologista' ? 'var(--color-secondary-blue)' :
  userType === 'veterinario' ? 'var(--color-secondary-green)' : null
);

let darkPrimaryColor = $derived(
  userType === 'patologista' ? 'var(--color-dark-blue)' :
  userType === 'veterinario' ? 'var(--color-dark-green)' : null
);

// Nome do usuário (agora vem do store)
let userName = $derived(
  usuario ? `${usuario.nome} (${usuario.papel === 'PATOLOGISTA' ? 'Patologista' : 'Veterinário'})` : 'Usuário'
);

// Proteção de rota: verifica autenticação ao montar
onMount(() => {
  if (!isAuthenticated) {
    goto('/');
  }
});

// Função de logout
async function handleLogout() {
  try {
    await authService.logout();
    authStore.logout();
    goto('/');
  } catch (error) {
    console.error('Erro ao fazer logout:', error);
    // Mesmo com erro, limpa localmente
    authStore.logout();
    goto('/');
  }
}
</script>

<div class="app-layout" style="--primary-color: {primaryColor}; --secondary-color: {secondaryColor}; --dark-primary-color: {darkPrimaryColor};">
  <!-- Menu lateral - Desktop only -->
  <aside class="sidebar hidden md:flex">
    <!-- Logo do site -->
    <div class="logo-container">
      <img src="/images/vivet-logo.png" alt="VitVet - Clínica Veterinária" class="logo-image" />
    </div>
    
    <!-- Menu de navegação -->
    <nav class="nav-menu">
      <!-- Botão de página inicial -->
      <div class="sidebar-item" class:active={isHomePage}>
        <a href="/{userType}/home" class="sidebar-link">
          <span class="sidebar-icon">
            <div class="icon-wrapper">
              <FaHome />
            </div>
          </span>
          <span class="sidebar-text">Página inicial</span>
        </a>
      </div>
      
      <!-- Item de menu Solicitações -->
      <div class="sidebar-item" class:active={isSolicitacoesPage}>
        <a href="/{userType}/solicitacoes" class="sidebar-link">
          <span class="sidebar-icon">
            <div class="icon-wrapper">
              <FaClipboardList />
            </div>
          </span>
          <span class="sidebar-text">Solicitações</span>
        </a>
      </div>
      
      <!-- Item de Nova Solicitação (apenas para veterinário) -->
      {#if userType === 'veterinario'}
        <div class="sidebar-item" class:active={isNovaSolicitacaoPage}>
          <a href="/{userType}/nova-solicitacao" class="sidebar-link">
            <span class="sidebar-icon">
              <div class="icon-wrapper">
                <FaPlus />
              </div>
            </span>
            <span class="sidebar-text">Nova Solicitação</span>
          </a>
        </div>
      {/if}
    </nav>
  </aside>

  <!-- Conteúdo principal -->
  <div class="content-area">
    <!-- Cabeçalho acoplado na parte superior -->
    <header class="content-header">
      <div class="header-content">
        <div class="user-info">
          <span class="user-name">{userName}</span>
        </div>
        <div class="header-logo-container">
          <img src="/images/vivet-logo.png" alt="VitVet" class="header-logo" />
        </div>
        <div class="header-actions">
          <button class="icon-button" aria-label="Configurações">
            <span class="icon">
              <div class="icon-wrapper">
                <FaCog />
              </div>
            </span>
          </button>
          <button class="icon-button" aria-label="Sair" onclick={handleLogout}>
            <span class="icon">
              <div class="icon-wrapper">
                <FaSignOutAlt />
              </div>
            </span>
          </button>
        </div>
      </div>
    </header>

    <!-- Linha divisória -->
    <hr class="content-divider">
    
    <!-- Área de conteúdo principal com fundo cinza claro -->
    <main class="content-main">
      <div class="content-container">
        {#if isAuthenticated}
          {@render children()}
        {:else}
          <div class="flex items-center justify-center h-full">
            <p class="text-gray-600">Verificando autenticação...</p>
          </div>
        {/if}
      </div>
    </main>
  </div>

  <!-- Bottom Navigation - Mobile only -->
  <nav class="bottom-nav md:hidden">
    <a href="/{userType}/home" class="bottom-nav-item" class:active={isHomePage}>
      <div class="bottom-nav-icon">
        <div class="icon-wrapper">
          <FaHome />
        </div>
      </div>
      <span class="bottom-nav-label">Home</span>
    </a>

    <a href="/{userType}/solicitacoes" class="bottom-nav-item" class:active={isSolicitacoesPage}>
      <div class="bottom-nav-icon">
        <div class="icon-wrapper">
          <FaClipboardList />
        </div>
      </div>
      <span class="bottom-nav-label">Solicitações</span>
    </a>

    {#if userType === 'veterinario'}
      <a href="/{userType}/nova-solicitacao" class="bottom-nav-item" class:active={isNovaSolicitacaoPage}>
        <div class="bottom-nav-icon">
          <div class="icon-wrapper">
            <FaPlus />
          </div>
        </div>
        <span class="bottom-nav-label">Nova</span>
      </a>
    {:else}
      <button class="bottom-nav-item" aria-label="Configurações">
        <div class="bottom-nav-icon">
          <div class="icon-wrapper">
            <FaCog />
          </div>
        </div>
        <span class="bottom-nav-label">Config</span>
      </button>
    {/if}
  </nav>
</div>

<!-- Container de notificações Toast -->
<ToastContainer />

<style>
/* Cores definidas pelo usuário */
:global(:root) {
  --color-white: #FFFFFF;
  --color-background-light: #F1F1F1;
  --color-primary-blue: #1D6088;
  --color-secondary-blue: #B5CFDE;
  --color-dark-blue: #165070;
  --color-primary-green: #5DB578;
  --color-secondary-green: #BBDCC5;
  --color-dark-green: #4a9163;
}

/* Layout principal */
.app-layout {
  display: flex;
  min-height: 100vh;
  background-color: var(--secondary-color);
  position: relative;
}

/* Menu lateral - Desktop */
.sidebar {
  display: none; /* Oculto no mobile por padrão */
  width: 250px;
  background-color: var(--secondary-color);
  flex-direction: column;
  border-right: 1px solid rgba(0, 0, 0, 0.1);
}

/* Mostrar sidebar apenas no desktop */
@media (min-width: 768px) {
  .sidebar {
    display: flex !important; /* Garantir que aparece no desktop */
  }
}

.logo-container {
  padding: 20px 15px;
  margin-bottom: 10px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
}

.logo-image {
  width: 100%;
  max-width: 180px;
  height: auto;
  object-fit: contain;
}

.icon-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-menu {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 10px 0;
}

.sidebar-item {
  padding: 0 15px;
}

.sidebar-link {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 15px;
  border-radius: 0 20px 20px 0;
  text-decoration: none;
  color: #333;
  transition: background-color 0.2s ease;
}

.sidebar-item:hover .sidebar-link {
  background-color: rgba(0, 0, 0, 0.05);
}

.sidebar-item.active .sidebar-link {
  background-color: var(--primary-color);
  color: white;
}

.sidebar-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.sidebar-icon .icon-wrapper {
  width: 20px;
  height: 20px;
}

.sidebar-text {
  font-size: 15px;
  font-weight: 500;
}

/* Área de conteúdo */
.content-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: var(--secondary-color);
}

.content-header {
  padding: 12px 16px;
  background-color: white;
}

@media (min-width: 768px) {
  .content-header {
    padding: 12px 24px;
  }
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
  position: relative; /* Para posicionar elementos absolutos */
}

.user-info {
  display: none; /* Oculto no mobile */
  min-width: 60px;
  flex-shrink: 1;
  overflow: hidden;
}

@media (min-width: 768px) {
  .user-info {
    display: block; /* Mostrar no desktop */
  min-width: 200px;
  }
}

.user-name {
  font-size: 12px;
  font-weight: 500;
  color: #444;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

@media (min-width: 768px) {
  .user-name {
    font-size: 16px;
  }
}

.header-logo-container {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
}

.header-logo {
  height: 40px;
  width: auto;
  object-fit: contain;
}

/* Mobile: logo centralizada, ações à direita */
@media (max-width: 767px) {
  .header-content {
    justify-content: center;
    align-items: center;
    position: relative;
  }
  
  .header-logo-container {
    flex: 1;
    justify-content: center;
  }
  
  .header-logo {
    height: 32px;
  }
  
  .header-actions {
    position: absolute;
    right: 16px;
    flex: 0 0 auto;
  }
}

@media (min-width: 768px) {
  .header-logo-container {
    justify-content: center;
  }
  
  .header-logo {
    height: 48px;
  }
  
  .header-content {
    justify-content: space-between;
  }
  
  .header-actions {
    position: static;
  }
}

.header-actions {
  display: flex;
  gap: 8px;
  min-width: 60px;
  justify-content: flex-end;
}

@media (min-width: 768px) {
  .header-actions {
    gap: 12px;
    min-width: 200px;
  }
}

.icon-button {
  background: none;
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s;
}

@media (min-width: 768px) {
  .icon-button {
    width: 36px;
    height: 36px;
  }
}

.icon-button:hover {
  background-color: var(--color-background-light);
}

.icon-button .icon {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}

@media (min-width: 768px) {
  .icon-button .icon {
  font-size: 18px;
}
}

.icon-button .icon .icon-wrapper {
  width: 18px;
  height: 18px;
}

@media (min-width: 768px) {
.icon-button .icon .icon-wrapper {
  width: 20px;
  height: 20px;
  }
}

.content-divider {
  border: none;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  margin: 0;
}

.content-main {
  flex: 1;
  padding: 12px;
  padding-bottom: 80px; /* Espaço para bottom nav no mobile */
  background-color: var(--color-background-light);
  overflow-y: auto;
  overflow-x: hidden;
  width: 100%;
  max-width: 100vw;
  box-sizing: border-box;
}

@media (min-width: 768px) {
  .content-main {
    padding: 20px;
    padding-bottom: 20px; /* Remove padding extra no desktop */
  }
}

.content-container {
  background-color: white;
  border-radius: 12px;
  padding: 16px;
  height: 100%;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  overflow-x: hidden;
  position: relative;
}

@media (min-width: 768px) {
  .content-container {
    border-radius: 16px;
    padding: 24px;
  }
}

.flex {
  display: flex;
}

.items-center {
  align-items: center;
}

.justify-center {
  justify-content: center;
}

.h-full {
  height: 100%;
}

.text-gray-600 {
  color: #4b5563;
}

/* ========================================
   BOTTOM NAVIGATION - MOBILE ONLY
   ======================================== */
.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 65px;
  background-color: var(--secondary-color);
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-around;
  align-items: center;
  z-index: 100;
  padding: 0 8px;
}

/* Ocultar bottom-nav no desktop */
@media (min-width: 768px) {
  .bottom-nav {
    display: none !important; /* Garantir que fica oculto no desktop */
  }
}

.bottom-nav-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  padding: 8px 4px;
  text-decoration: none;
  color: #666;
  transition: all 0.2s;
  border-radius: 8px;
  background: none;
  border: none;
  cursor: pointer;
  min-height: 44px; /* Touch target adequado */
}

.bottom-nav-item:active {
  background-color: rgba(0, 0, 0, 0.05);
}

.bottom-nav-item.active {
  color: var(--primary-color);
}

.bottom-nav-item.active .bottom-nav-icon {
  background-color: rgba(var(--primary-color-rgb, 93, 181, 120), 0.1);
}

.bottom-nav-icon {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.bottom-nav-icon .icon-wrapper {
  width: 20px;
  height: 20px;
}

.bottom-nav-label {
  font-size: 11px;
  font-weight: 500;
  text-align: center;
  line-height: 1;
}

/* Ajustar cores RGB para efeito de active no bottom nav */
@media (max-width: 767px) {
  .app-layout {
    --primary-color-rgb: 93, 181, 120; /* green default */
  }
}
</style>
