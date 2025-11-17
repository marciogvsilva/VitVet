<script>
// Dados de exemplo de solicitações
const todasSolicitacoes = [
  {
    id: '01',
    exames: 'hemograma completo, urina e ...',
    paciente: 'Paçoca',
    especie: 'Canino',
    tutor: 'Roberta',
    status: 'concluido'
  },
  {
    id: '02',
    exames: 'ultrassonografia',
    paciente: 'Mia',
    especie: 'Felino',
    tutor: 'Priscilla',
    status: 'pendente'
  },
  {
    id: '03',
    exames: 'hemograma completo, urina, ...',
    paciente: 'Thor',
    especie: 'Canino',
    tutor: 'Thiago',
    status: 'pendente'
  },
  {
    id: '04',
    exames: 'raio-x, análise de sangue',
    paciente: 'Luna',
    especie: 'Felino',
    tutor: 'Carlos',
    status: 'cancelado'
  },
  {
    id: '05',
    exames: 'hemograma, bioquímico, hormonal',
    paciente: 'Max',
    especie: 'Canino',
    tutor: 'Ana Paula',
    status: 'concluido'
  },
  {
    id: '06',
    exames: 'raio-x, urina, fezes',
    paciente: 'Bella',
    especie: 'Felino',
    tutor: 'Roberto',
    status: 'pendente'
  },
  {
    id: '07',
    exames: 'análise dermatológica, biópsia',
    paciente: 'Rex',
    especie: 'Canino',
    tutor: 'Marcelo',
    status: 'concluido'
  }
];

// Paginação
let paginaAtual = 1;
let itensPorPagina = 4;
$: totalPaginas = Math.ceil(todasSolicitacoes.length / itensPorPagina);
$: solicitacoes = todasSolicitacoes.slice(
  (paginaAtual - 1) * itensPorPagina, 
  paginaAtual * itensPorPagina
);

function irParaPagina(pagina) {
  if (pagina >= 1 && pagina <= totalPaginas) {
    paginaAtual = pagina;
  }
}
</script>

<div class="solicitacoes-container">
  <div class="content-area">
    <h1>Solicitações</h1>
    
    <div class="solicitacoes-list">
      {#each solicitacoes as solicitacao}
        <div class="solicitacao-card">
          <div class="solicitacao-info">
            <div class="solicitacao-header">
              <h2>Pedido #{solicitacao.id}</h2>
              <div class="status-badge" class:concluido={solicitacao.status === 'concluido'} class:pendente={solicitacao.status === 'pendente'} class:cancelado={solicitacao.status === 'cancelado'}>
                {solicitacao.status === 'concluido' ? 'Concluído' : 
                solicitacao.status === 'pendente' ? 'Pendente' : 'Cancelado'}
              </div>
            </div>
            <p>Exames: {solicitacao.exames}</p>
          </div>
          
          <div class="solicitacao-details">
            <div class="paciente-info">
              <p>Paciente: {solicitacao.paciente}</p>
              <p>Espécie: {solicitacao.especie}</p>
            </div>
            
            <div class="tutor-info">
              <p>Tutor: {solicitacao.tutor}</p>
            </div>
            
            <div class="action-button">
              <a href="/veterinario/solicitacoes/{solicitacao.id}" class="resultado-btn active">
                Visualizar
              </a>
            </div>
          </div>
        </div>
      {/each}
    </div>
  </div>
  
  <!-- Paginação fixa -->
  <div class="pagination-container">
    <div class="pagination">
      <button 
        class="pagination-btn" 
        on:click={() => irParaPagina(paginaAtual - 1)} 
        disabled={paginaAtual === 1}
      >
        Anterior
      </button>
      
      <span class="pagination-info">
        Página {paginaAtual} de {totalPaginas}
      </span>
      
      <button 
        class="pagination-btn" 
        on:click={() => irParaPagina(paginaAtual + 1)} 
        disabled={paginaAtual === totalPaginas}
      >
        Próxima
      </button>
    </div>
  </div>
</div>

<style>
  .solicitacoes-container {
    padding: 0;
    display: flex;
    flex-direction: column;
    height: 100%;
    position: relative;
  }
  
  .content-area {
    flex: 1;
    overflow-y: auto;
    padding-bottom: 70px; /* Espaço para a paginação */
  }
  
  h1 {
    font-size: 24px;
    font-weight: 600;
    margin-bottom: 24px;
    color: #333;
  }
  
  .solicitacoes-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
  
  .solicitacao-card {
    background-color: #f2f2f2;
    border-radius: 8px;
    padding: 16px 20px;
  }
  
  .solicitacao-info {
    margin-bottom: 12px;
  }
  
  .solicitacao-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 6px;
  }
  
  .solicitacao-info h2 {
    font-size: 18px;
    font-weight: 600;
    margin: 0;
  }
  
  .status-badge {
    font-size: 12px;
    font-weight: 500;
    padding: 4px 10px;
    border-radius: 12px;
  }
  
  .status-badge.pendente {
    background-color: #f0ad4e; /* Amarelo/laranja para pendentes */
    color: white;
  }
  
  .status-badge.concluido {
    background-color: #5DB578; /* Verde para concluídos */
    color: white;
  }
  
  .status-badge.cancelado {
    background-color: #d9534f; /* Vermelho para cancelados */
    color: white;
  }
  
  .solicitacao-info p {
    margin: 0;
    color: #555;
    font-size: 15px;
  }
  
  .solicitacao-details {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .paciente-info p, .tutor-info p {
    margin: 4px 0;
    font-size: 14px;
  }
  
  .resultado-btn {
    background-color: #ccc;
    color: #555;
    padding: 8px 18px;
    border-radius: 20px;
    border: none;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.2s, color 0.2s;
  }
  
  .resultado-btn.active {
    background-color: var(--color-primary-green);
    color: white;
  }
  
  /* Em caso de hover nos botões ativos */
  .resultado-btn.active:hover {
    background-color: var(--color-dark-green);
  }
  
  /* Paginação fixa */
  .pagination-container {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: transparent;
    padding: 16px 0;
    border-top: 1px solid rgba(0, 0, 0, 0.05);
  }
  
  .pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 16px;
  }
  
  .pagination-btn {
    padding: 8px 16px;
    border: 1px solid var(--color-primary-green);
    border-radius: 20px;
    background-color: transparent;
    color: var(--color-primary-green);
    font-size: 14px;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .pagination-btn:hover:not(:disabled) {
    background-color: var(--color-primary-green);
    color: white;
  }
  
  .pagination-btn:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
  
  .pagination-info {
    font-size: 14px;
    color: #555;
  }
</style>
