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
import FaMicroscope from 'svelte-icons/fa/FaMicroscope.svelte';
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

<div class="app-layout">
  <!-- Menu lateral -->
  <aside class="sidebar" style="--primary-color: {primaryColor}; --secondary-color: {secondaryColor}; --dark-primary-color: {darkPrimaryColor};">
    <!-- Logo do site -->
    <div class="logo-container">
      <div class="logo" style="color: {darkPrimaryColor};">
        <span class="logo-icon">
          <div class="icon-wrapper">
            <FaMicroscope />
          </div>
        </span>
        <span class="logo-text">VitVet</span>
      </div>
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
        <h1 class="header-title">Exames - VitVet</h1>
        <div class="header-actions">
          <button class="icon-button" aria-label="Configurações">
            <span class="icon">
              <div class="icon-wrapper">
                <FaCog />
              </div>
            </span>
          </button>
          <button class="icon-button" aria-label="Sair" on:click={handleLogout}>
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
}

/* Menu lateral */
.sidebar {
  width: 250px;
  background-color: var(--secondary-color);
  display: flex;
  flex-direction: column;
  border-right: 1px solid rgba(0, 0, 0, 0.1);
}

.logo-container {
  padding: 20px 15px;
  margin-bottom: 10px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: 700;
  font-size: 24px;
}

.logo-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
}

.icon-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-icon .icon-wrapper {
  width: 28px;
  height: 28px;
}

.logo-text {
  letter-spacing: -0.5px;
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
  padding: 12px 24px;
  background-color: white;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  min-width: 200px;
}

.user-name {
  font-size: 16px;
  font-weight: 500;
  color: #444;
}

.header-title {
  font-size: 24px;
  font-weight: 500;
  color: #333;
  margin: 0;
  text-align: center;
}

.header-actions {
  display: flex;
  gap: 12px;
  min-width: 200px;
  justify-content: flex-end;
}

.icon-button {
  background: none;
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s;
}

.icon-button:hover {
  background-color: var(--color-background-light);
}

.icon-button .icon {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.icon-button .icon .icon-wrapper {
  width: 20px;
  height: 20px;
}

.content-divider {
  border: none;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  margin: 0;
}

.content-main {
  flex: 1;
  padding: 20px;
  background-color: var(--color-background-light);
  overflow-y: auto;
}

.content-container {
  background-color: white;
  border-radius: 16px;
  padding: 24px;
  height: 100%;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
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
</style>
