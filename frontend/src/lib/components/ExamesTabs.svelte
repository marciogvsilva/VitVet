<script>
import FaCheck from 'svelte-icons/fa/FaCheck.svelte';
import FaBox from 'svelte-icons/fa/FaBox.svelte';
import FaTint from 'svelte-icons/fa/FaTint.svelte';
import FaFlask from 'svelte-icons/fa/FaFlask.svelte';
import FaSearch from 'svelte-icons/fa/FaSearch.svelte';
import FaBolt from 'svelte-icons/fa/FaBolt.svelte';
import FaVial from 'svelte-icons/fa/FaVial.svelte';
import FaBone from 'svelte-icons/fa/FaBone.svelte';

let { exames, examesSelecionados, onToggleExame } = $props();

// Categorias em ordem
const categorias = [
  { id: 'COMBOS', nome: 'Combos', icon: FaBox },
  { id: 'HEMATOLOGIA', nome: 'Hematologia', icon: FaTint },
  { id: 'BIOQUIMICA', nome: 'Bioquímica', icon: FaFlask },
  { id: 'DERMATOLOGIA', nome: 'Dermatologia', icon: FaSearch },
  { id: 'TESTES_RAPIDOS', nome: 'Testes Rápidos', icon: FaBolt },
  { id: 'URINA', nome: 'Urina', icon: FaVial },
  { id: 'FEZES', nome: 'Fezes', icon: FaFlask },
  { id: 'MEDULA_OSSEA', nome: 'Medula Óssea', icon: FaBone }
];

let abaAtiva = $state('COMBOS');

// Agrupar exames por categoria
let examesPorCategoria = $derived.by(() => {
  const agrupados = {};
  // Garantir que exames seja um array válido
  if (!Array.isArray(exames) || exames.length === 0) {
    categorias.forEach(cat => {
      agrupados[cat.id] = [];
    });
    return agrupados;
  }
  
  categorias.forEach(cat => {
    agrupados[cat.id] = exames.filter(e => e && e.categoria === cat.id);
  });
  return agrupados;
});

// Selecionar automaticamente a primeira aba com exames
$effect(() => {
  if (Array.isArray(exames) && exames.length > 0) {
    const primeiraCategoriaComExames = categorias.find(cat => {
      const examesCategoria = examesPorCategoria[cat.id] || [];
      return examesCategoria.length > 0;
    });
    if (primeiraCategoriaComExames && abaAtiva !== primeiraCategoriaComExames.id) {
      // Só atualiza se a aba atual não tiver exames
      const examesAtuais = examesPorCategoria[abaAtiva] || [];
      if (examesAtuais.length === 0) {
        abaAtiva = primeiraCategoriaComExames.id;
      }
    }
  }
});

// Contar exames selecionados por categoria
function contarSelecionados(categoriaId) {
  const examesCategoria = examesPorCategoria[categoriaId] || [];
  return examesCategoria.filter(e => examesSelecionados.includes(e.id)).length;
}

// Verificar se exame está selecionado
function isSelecionado(exameId) {
  return examesSelecionados.includes(exameId);
}

// Toggle exame individual
function toggleExame(exame) {
  // Criar uma cópia do array atual (props são read-only)
  const selecionadosAtuais = [...examesSelecionados];
  let novosSelecionados;
  
  if (exame.isCombo) {
    // Se for combo, seleciona/deseleciona todos os exames incluídos
    if (isSelecionado(exame.id)) {
      // Desmarcar combo e todos os exames incluídos
      novosSelecionados = selecionadosAtuais.filter(id => 
        id !== exame.id && !exame.examesIncluidos.includes(id)
      );
    } else {
      // Marcar combo e todos os exames incluídos (sem duplicatas)
      novosSelecionados = [...new Set([...selecionadosAtuais, exame.id, ...exame.examesIncluidos])];
    }
  } else {
    // Exame individual
    if (isSelecionado(exame.id)) {
      novosSelecionados = selecionadosAtuais.filter(id => id !== exame.id);
      // Se estava em um combo, desmarcar o combo também
      exames.forEach(combo => {
        if (combo.isCombo && combo.examesIncluidos && combo.examesIncluidos.includes(exame.id)) {
          const index = novosSelecionados.indexOf(combo.id);
          if (index > -1) {
            novosSelecionados.splice(index, 1);
          }
        }
      });
    } else {
      novosSelecionados = [...selecionadosAtuais, exame.id];
    }
  }
  
  // Notificar o componente pai através do callback
  onToggleExame?.(novosSelecionados);
}
</script>

<div class="exames-tabs">
  <!-- Abas -->
  <div class="tabs-container">
    <div class="tabs-scroll">
      {#each categorias as categoria}
        {@const count = contarSelecionados(categoria.id)}
        {@const temExames = (examesPorCategoria[categoria.id] || []).length > 0}
        {#if temExames}
          <button
            type="button"
            class="tab"
            class:active={abaAtiva === categoria.id}
            onclick={() => abaAtiva = categoria.id}
          >
            <span class="tab-icon"><svelte:component this={categoria.icon} /></span>
            <span class="tab-label">{categoria.nome}</span>
            {#if count > 0}
              <span class="tab-badge">{count}</span>
            {/if}
          </button>
        {/if}
      {/each}
    </div>
  </div>

  <!-- Conteúdo das Abas -->
  <div class="tab-content">
    {#each categorias as categoria}
      {#if abaAtiva === categoria.id}
        {@const examesCategoria = examesPorCategoria[categoria.id] || []}
        {#if examesCategoria.length === 0}
          <div class="empty-category">
            <p>Nenhum exame disponível nesta categoria.</p>
          </div>
        {:else}
          <div class="exames-grid">
            {#each examesCategoria as exame}
              <button
                type="button"
                class="exame-card"
                class:selected={isSelecionado(exame.id)}
                class:combo={exame.isCombo}
                onclick={() => toggleExame(exame)}
              >
                <div class="exame-check">
                  {#if isSelecionado(exame.id)}
                    <span class="check-icon"><FaCheck /></span>
                  {/if}
                </div>
                <div class="exame-info">
                  <strong>{exame.nome}</strong>
                  <p>{exame.descricao}</p>
                  {#if exame.isCombo && exame.examesIncluidos}
                    <span class="combo-badge">Combo</span>
                  {/if}
                </div>
              </button>
            {/each}
          </div>
        {/if}
      {/if}
    {/each}
  </div>
</div>

<style>
.exames-tabs {
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  overflow-x: hidden;
  position: relative;
}

/* Container de Abas */
.tabs-container {
  border-bottom: 2px solid #e5e7eb;
  overflow-x: auto;
  overflow-y: hidden;
  -webkit-overflow-scrolling: touch;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  position: relative;
}

.tabs-scroll {
  display: flex;
  gap: 4px;
  padding: 0 4px;
  box-sizing: border-box;
}

/* Aba Individual */
.tab {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 12px 16px;
  background: none;
  border: none;
  border-bottom: 3px solid transparent;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
  font-size: 14px;
  font-weight: 500;
  color: #6b7280;
  position: relative;
  flex-shrink: 0;
  box-sizing: border-box;
}

.tab:hover {
  color: #374151;
  background: #f9fafb;
}

.tab.active {
  color: #5DB578;
  border-bottom-color: #5DB578;
  font-weight: 600;
}

.tab-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
}

.tab-icon :global(svg) {
  width: 16px;
  height: 16px;
}

.tab-label {
  font-size: 14px;
}

.tab-badge {
  background: #5DB578;
  color: white;
  border-radius: 12px;
  padding: 2px 8px;
  font-size: 11px;
  font-weight: 600;
  min-width: 20px;
  text-align: center;
}

.tab.active .tab-badge {
  background: #4a9163;
}

/* Conteúdo da Aba */
.tab-content {
  min-height: 200px;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  overflow-x: hidden;
  position: relative;
}

.empty-category {
  padding: 40px 20px;
  text-align: center;
  color: #9ca3af;
}

/* Grid de Exames */
.exames-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 12px;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  overflow-x: hidden;
}

@media (min-width: 768px) {
  .exames-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
}

@media (min-width: 1024px) {
  .exames-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

/* Card de Exame */
.exame-card {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  background: white;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  text-align: left;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  overflow: hidden;
  position: relative;
  min-width: 0;
}

.exame-card:hover {
  border-color: #5DB578;
  box-shadow: 0 2px 8px rgba(93, 181, 120, 0.1);
}

.exame-card.selected {
  border-color: #5DB578;
  background: #f0fdf4;
}

.exame-card.combo {
  border-left: 4px solid #fbbf24;
}

.exame-card.combo.selected {
  border-left-color: #f59e0b;
}

.exame-check {
  width: 24px;
  height: 24px;
  min-width: 24px;
  border: 2px solid #d1d5db;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  transition: all 0.2s;
  flex-shrink: 0;
}

.exame-card.selected .exame-check {
  background: #5DB578;
  border-color: #5DB578;
}

.check-icon {
  color: white;
  font-size: 14px;
  display: flex;
  align-items: center;
}

.exame-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 0;
  max-width: 100%;
  overflow: hidden;
  box-sizing: border-box;
}

.exame-info strong {
  font-size: 15px;
  font-weight: 600;
  color: #1f2937;
  line-height: 1.4;
  word-wrap: break-word;
  overflow-wrap: break-word;
  max-width: 100%;
  overflow: hidden;
}

.exame-info p {
  font-size: 13px;
  color: #6b7280;
  margin: 0;
  line-height: 1.4;
  word-wrap: break-word;
  overflow-wrap: break-word;
  max-width: 100%;
  overflow: hidden;
}

.combo-badge {
  display: inline-block;
  background: #fef3c7;
  color: #92400e;
  font-size: 11px;
  font-weight: 600;
  padding: 2px 8px;
  border-radius: 4px;
  margin-top: 4px;
  width: fit-content;
}

/* Mobile: Abas scrolláveis */
@media (max-width: 767px) {
  .exames-tabs {
    gap: 12px;
  }
  
  .tabs-container {
    margin-left: 0;
    margin-right: 0;
    padding-left: 0;
    padding-right: 0;
  }
  
  .tabs-scroll {
    padding: 0 4px;
  }
  
  .tab {
    padding: 10px 12px;
    font-size: 13px;
    flex-shrink: 0;
  }
  
  .tab-icon {
    width: 14px;
    height: 14px;
    flex-shrink: 0;
  }
  
  .tab-icon :global(svg) {
    width: 14px;
    height: 14px;
  }
  
  .tab-label {
    font-size: 13px;
    white-space: nowrap;
  }
  
  .tab-content {
    min-height: 150px;
  }
  
  .exames-grid {
    gap: 10px;
  }
  
  .exame-card {
    padding: 12px;
    gap: 10px;
  }
  
  .exame-check {
    width: 20px;
    height: 20px;
    min-width: 20px;
    flex-shrink: 0;
  }
  
  .exame-info {
    min-width: 0;
  }
  
  .exame-info strong {
    font-size: 14px;
    line-height: 1.3;
  }
  
  .exame-info p {
    font-size: 12px;
    line-height: 1.3;
  }
  
  .combo-badge {
    font-size: 10px;
    padding: 2px 6px;
    margin-top: 2px;
  }
}
</style>

