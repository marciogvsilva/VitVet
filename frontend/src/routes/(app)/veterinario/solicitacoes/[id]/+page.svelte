<script>
import { page } from '$app/stores';

// Dados mockados para simular a solicitação selecionada
const solicitacaoMap = {
  '01': {
    id: '01',
    paciente: 'Paçoca',
    especie: 'Canino',
    tutor: 'Roberta',
    contato: '(XX) XXXXX-XXXX',
    endereco: 'Rua Inexistente, 40',
    bairro: 'Nenhum',
    cidade: 'Irreal',
    cep: 'XXXXX-XXX',
    exames: [
      { nome: 'Hemograma completo', checked: true },
      { nome: 'Urina', checked: true },
      { nome: 'Fezes', checked: true }
    ],
    status: 'concluido'
  },
  '02': {
    id: '02',
    paciente: 'Mia',
    especie: 'Felino',
    tutor: 'Priscilla',
    contato: '(XX) XXXXX-XXXX',
    endereco: 'Rua Dos Exemplos, 22',
    bairro: 'Centro',
    cidade: 'Modelópolis',
    cep: 'XXXXX-XXX',
    exames: [
      { nome: 'Ultrassonografia', checked: true }
    ],
    status: 'pendente'
  },
  '03': {
    id: '03',
    paciente: 'Thor',
    especie: 'Canino',
    tutor: 'Thiago',
    contato: '(XX) XXXXX-XXXX',
    endereco: 'Avenida Principal, 302',
    bairro: 'Vila Nova',
    cidade: 'Irreal',
    cep: 'XXXXX-XXX',
    exames: [
      { nome: 'Hemograma completo', checked: true },
      { nome: 'Urina', checked: true },
      { nome: 'Raio-X', checked: true }
    ],
    status: 'pendente'
  },
  '04': {
    id: '04',
    paciente: 'Luna',
    especie: 'Felino',
    tutor: 'Carlos',
    contato: '(XX) XXXXX-XXXX',
    endereco: 'Alameda das Flores, 126',
    bairro: 'Jardim',
    cidade: 'Modelópolis',
    cep: 'XXXXX-XXX',
    exames: [
      { nome: 'Raio-X', checked: true },
      { nome: 'Análise de sangue', checked: true }
    ],
    status: 'cancelado'
  }
};

// Id da solicitação baseado na URL
$: id = $page.params.id;
$: solicitacao = solicitacaoMap[id] || null;

function handleVoltar() {
  history.back();
}
</script>

{#if solicitacao}
  <div class="solicitacao-detail">
    <div class="page-header">
      <button class="voltar-btn" on:click={handleVoltar}>
        <span class="arrow">←</span>
      </button>
      <h1>Pedido #{solicitacao.id}</h1>
    </div>
    
    <div class="content-box">
      <div class="info-section">
        <div class="info-row">
          <div class="info-col">
            <p class="info-label">Paciente: <span class="info-value">{solicitacao.paciente}</span></p>
            <p class="info-label">Espécie: <span class="info-value">{solicitacao.especie}</span></p>
          </div>
          
          <div class="info-col">
            <p class="info-label">Tutor: <span class="info-value">{solicitacao.tutor}</span></p>
            <p class="info-label">Contato: <span class="info-value">{solicitacao.contato}</span></p>
          </div>
        </div>
      </div>
      
      <div class="info-section">
        <h2>Exames:</h2>
        <div class="exames-list">
          {#each solicitacao.exames as exame}
            <div class="exame-item">
              <div class="checkbox checked">✓</div>
              <span>{exame.nome}</span>
            </div>
          {/each}
        </div>
      </div>
      
      <div class="info-section">
        <h2>Dados do Tutor:</h2>
        <p class="info-label">Endereço: <span class="info-value">{solicitacao.endereco}</span></p>
        <p class="info-label">Bairro: <span class="info-value">{solicitacao.bairro}</span>, Cidade: <span class="info-value">{solicitacao.cidade}</span></p>
        <p class="info-label">CEP: <span class="info-value">{solicitacao.cep}</span></p>
      </div>
      
      <div class="actions">
        <button class="edit-btn">Editar</button>
        <button class="resultado-btn {solicitacao.status === 'concluido' ? 'active' : ''}" disabled={solicitacao.status === 'cancelado'}>
          {#if solicitacao.status === 'pendente'}
            Aguardando resultados
          {:else if solicitacao.status === 'cancelado'}
            Solicitação cancelada
          {:else}
            Visualizar resultados
          {/if}
        </button>
      </div>
    </div>
  </div>
{:else}
  <div class="error-msg">
    <h2>Solicitação não encontrada</h2>
    <p>A solicitação #{id} não foi encontrada no sistema.</p>
    <button class="voltar-btn" on:click={handleVoltar}>Voltar para lista de solicitações</button>
  </div>
{/if}

<style>
  .solicitacao-detail {
    padding: 0;
  }
  
  .page-header {
    display: flex;
    align-items: center;
    margin-bottom: 24px;
  }
  
  .voltar-btn {
    background: none;
    border: none;
    display: flex;
    align-items: center;
    margin-right: 12px;
    padding: 5px;
    cursor: pointer;
  }
  
  .arrow {
    font-size: 20px;
  }
  
  h1 {
    font-size: 24px;
    font-weight: 600;
    margin: 0;
    color: #333;
  }
  
  .content-box {
    background-color: #f2f2f2;
    border-radius: 8px;
    padding: 20px;
  }
  
  .info-section {
    margin-bottom: 24px;
  }
  
  .info-row {
    display: flex;
    justify-content: space-between;
  }
  
  .info-col {
    flex: 1;
  }
  
  .info-label {
    font-size: 14px;
    color: #555;
    margin: 5px 0;
    font-weight: 500;
  }
  
  .info-value {
    font-weight: 400;
  }
  
  h2 {
    font-size: 18px;
    margin: 0 0 12px 0;
    color: #333;
  }
  
  .exames-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  .exame-item {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .checkbox {
    width: 20px;
    height: 20px;
    border: 2px solid #ccc;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: white;
  }
  
  .checkbox.checked {
    background-color: var(--color-primary-green);
    border-color: var(--color-primary-green);
  }
  
  .actions {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    margin-top: 30px;
  }
  
  .edit-btn {
    background-color: #ccc;
    border: none;
    border-radius: 20px;
    padding: 8px 24px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.2s;
  }
  
  .edit-btn:hover {
    background-color: #bbb;
  }
  
  .resultado-btn {
    background-color: #ccc;
    border: none;
    border-radius: 20px;
    padding: 8px 24px;
    font-size: 14px;
    font-weight: 500;
    color: #444;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .resultado-btn.active {
    background-color: var(--color-primary-green);
    color: white;
  }
  
  .resultado-btn.active:hover {
    background-color: var(--color-dark-green);
  }
  
  .resultado-btn:disabled {
    background-color: #e0e0e0;
    color: #888;
    cursor: not-allowed;
  }
  
  .error-msg {
    padding: 20px;
    text-align: center;
  }
</style>
