<script>
import { onMount } from 'svelte';
import { goto } from '$app/navigation';
import { solicitacaoService } from '$lib/mocks/services';
import SolicitacaoCard from '$lib/components/SolicitacaoCard.svelte';
import LoadingSpinner from '$lib/components/LoadingSpinner.svelte';
import FaInbox from 'svelte-icons/fa/FaInbox.svelte';
import FaClock from 'svelte-icons/fa/FaClock.svelte';
import FaCheckCircle from 'svelte-icons/fa/FaCheckCircle.svelte';
import FaMicroscope from 'svelte-icons/fa/FaMicroscope.svelte';
import FaExclamationCircle from 'svelte-icons/fa/FaExclamationCircle.svelte';

let solicitacoes = $state([]);
let loading = $state(true);
let erro = $state('');

// Filtros
let filtroStatus = $state('');
let busca = $state('');

async function carregarSolicitacoes() {
  loading = true;
  erro = '';
  
  try {
    const filtros = {};
    
    if (filtroStatus) {
      filtros.status = filtroStatus;
    }
    
    if (busca.trim()) {
      filtros.animal = busca.trim();
    }
    
    solicitacoes = await solicitacaoService.listarSolicitacoes(filtros);
  } catch (error) {
    console.error('Erro ao carregar solicitações:', error);
    erro = error.message || 'Erro ao carregar solicitações';
  } finally {
    loading = false;
  }
}

function verDetalhes(id) {
  goto(`/patologista/solicitacoes/${id}`);
}

function limparFiltros() {
  filtroStatus = '';
  busca = '';
  carregarSolicitacoes();
}

// Atalho para ver apenas as novas (recebidas)
function verNovas() {
  filtroStatus = 'RECEBIDO';
  busca = '';
  carregarSolicitacoes();
}

function aplicarFiltroRapido(status) {
  filtroStatus = status;
  busca = '';
  carregarSolicitacoes();
}

onMount(() => {
  carregarSolicitacoes();
});
</script>

<div class="solicitacoes-page">
  <div class="page-header">
    <h1>Solicitações de Exames</h1>
    <p class="subtitle">Analise e envie laudos para as solicitações recebidas</p>
  </div>

  <!-- Atalhos rápidos -->
  <div class="atalhos">
    <button 
      onclick={() => aplicarFiltroRapido('RECEBIDO')} 
      class="atalho-btn recebido"
      class:ativo={filtroStatus === 'RECEBIDO'}
    >
      <span class="atalho-icon"><FaInbox /></span>
      Ver Novas Solicitações
    </button>
    <button 
      onclick={() => aplicarFiltroRapido('EM_ANALISE')} 
      class="atalho-btn analise"
      class:ativo={filtroStatus === 'EM_ANALISE'}
    >
      <span class="atalho-icon"><FaClock /></span>
      Em Análise
    </button>
    <button 
      onclick={() => aplicarFiltroRapido('CONCLUIDO')} 
      class="atalho-btn concluido"
      class:ativo={filtroStatus === 'CONCLUIDO'}
    >
      <span class="atalho-icon"><FaCheckCircle /></span>
      Concluídos
    </button>
  </div>

  <!-- Filtros -->
  <div class="filtros">
    <div class="filtro-grupo">
      <label for="status">Status:</label>
      <select 
        id="status" 
        bind:value={filtroStatus} 
        onchange={carregarSolicitacoes}
        class="select-input"
      >
        <option value="">Todos os status</option>
        <option value="RECEBIDO">Recebido</option>
        <option value="EM_ANALISE">Em Análise</option>
        <option value="CONCLUIDO">Concluído</option>
        <option value="CANCELADO">Cancelado</option>
      </select>
    </div>

    <div class="filtro-grupo">
      <label for="busca">Buscar animal:</label>
      <input
        id="busca"
        type="text"
        bind:value={busca}
        onkeyup={(e) => e.key === 'Enter' && carregarSolicitacoes()}
        placeholder="Nome do animal..."
        class="text-input"
      />
    </div>

    <div class="filtro-acoes">
      <button onclick={carregarSolicitacoes} class="btn-buscar">
        Buscar
      </button>
      <button onclick={limparFiltros} class="btn-limpar">
        Limpar
      </button>
    </div>
  </div>

  <!-- Conteúdo -->
  <div class="content">
    {#if loading}
      <LoadingSpinner color="#1D6088" />
    {:else if erro}
      <div class="erro-box">
        <div class="erro-icon-box">
          <FaExclamationCircle />
        </div>
        <p>{erro}</p>
        <button onclick={carregarSolicitacoes} class="btn-retry">
          Tentar novamente
        </button>
      </div>
    {:else if solicitacoes.length === 0}
      <div class="empty-state">
        <div class="empty-icon">
          <FaMicroscope />
        </div>
        <h2>Nenhuma solicitação encontrada</h2>
        <p>
          {#if filtroStatus || busca}
            Tente ajustar os filtros para ver mais resultados.
          {:else}
            Não há solicitações de exames no momento.
          {/if}
        </p>
      </div>
    {:else}
      <div class="resultados-info">
        <p>
          {solicitacoes.length} 
          {solicitacoes.length === 1 ? 'solicitação encontrada' : 'solicitações encontradas'}
        </p>
      </div>
      
      <div class="solicitacoes-grid">
        {#each solicitacoes as solicitacao (solicitacao.id)}
          <SolicitacaoCard 
            {solicitacao} 
            onclick={() => verDetalhes(solicitacao.id)}
          />
        {/each}
      </div>
    {/if}
  </div>
</div>

<style>
.solicitacoes-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
  height: 100%;
}

.page-header h1 {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 8px 0;
}

.subtitle {
  color: #6b7280;
  margin: 0;
  font-size: 14px;
}

/* Atalhos */
.atalhos {
  display: flex;
  gap: 12px;
}

.atalho-btn {
  flex: 1;
  padding: 16px;
  border: 2px solid;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.atalho-icon {
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
}

.atalho-btn.recebido {
  border-color: #DBEAFE;
  color: #1E40AF;
}

.atalho-btn.recebido:hover {
  background: #DBEAFE;
}

.atalho-btn.recebido.ativo {
  background: #DBEAFE;
  border-width: 3px;
  font-weight: 700;
}

.atalho-btn.analise {
  border-color: #FEF3C7;
  color: #92400E;
}

.atalho-btn.analise:hover {
  background: #FEF3C7;
}

.atalho-btn.analise.ativo {
  background: #FEF3C7;
  border-width: 3px;
  font-weight: 700;
}

.atalho-btn.concluido {
  border-color: #D1FAE5;
  color: #065F46;
}

.atalho-btn.concluido:hover {
  background: #D1FAE5;
}

.atalho-btn.concluido.ativo {
  background: #D1FAE5;
  border-width: 3px;
  font-weight: 700;
}

/* Filtros */
.filtros {
  display: flex;
  gap: 16px;
  align-items: flex-end;
  padding: 20px;
  background: #f9fafb;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
}

.filtro-grupo {
  display: flex;
  flex-direction: column;
  gap: 6px;
  flex: 1;
}

.filtro-grupo label {
  font-size: 13px;
  font-weight: 600;
  color: #374151;
}

.select-input,
.text-input {
  padding: 10px 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
  transition: border-color 0.2s;
}

.select-input:focus,
.text-input:focus {
  outline: none;
  border-color: #1D6088;
  box-shadow: 0 0 0 3px rgba(29, 96, 136, 0.1);
}

.filtro-acoes {
  display: flex;
  gap: 8px;
}

.btn-buscar,
.btn-limpar {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-buscar {
  background: #1D6088;
  color: white;
}

.btn-buscar:hover {
  background: #165070;
}

.btn-limpar {
  background: #e5e7eb;
  color: #374151;
}

.btn-limpar:hover {
  background: #d1d5db;
}

/* Conteúdo */
.content {
  flex: 1;
  overflow-y: auto;
}

.resultados-info {
  margin-bottom: 16px;
  color: #6b7280;
  font-size: 14px;
  font-weight: 500;
}

.solicitacoes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 16px;
}

/* Empty State */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.empty-icon {
  width: 64px;
  height: 64px;
  display: flex;
  align-items: center;
  color: #9ca3af;
  margin-bottom: 16px;
}

.erro-icon-box {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  color: #ef4444;
  margin-bottom: 12px;
}

.empty-state h2 {
  font-size: 20px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 8px 0;
}

.empty-state p {
  color: #6b7280;
  margin: 0;
  max-width: 400px;
}

/* Erro */
.erro-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  text-align: center;
}

.erro-box p {
  color: #991b1b;
  font-weight: 600;
  margin: 0 0 16px 0;
}

.btn-retry {
  padding: 12px 24px;
  background: #ef4444;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
}

.btn-retry:hover {
  background: #dc2626;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.4);
}
</style>
