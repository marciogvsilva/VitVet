<script>
import { onMount } from 'svelte';
import { goto } from '$app/navigation';
import { page } from '$app/stores';
import { solicitacaoService } from '$lib/mocks/services';
import StatusBadge from '$lib/components/StatusBadge.svelte';
import LoadingSpinner from '$lib/components/LoadingSpinner.svelte';
import FaExclamationTriangle from 'svelte-icons/fa/FaExclamationTriangle.svelte';
import FaClipboardList from 'svelte-icons/fa/FaClipboardList.svelte';
import FaPaw from 'svelte-icons/fa/FaPaw.svelte';
import FaUser from 'svelte-icons/fa/FaUser.svelte';
import FaMicroscope from 'svelte-icons/fa/FaMicroscope.svelte';
import FaStethoscope from 'svelte-icons/fa/FaStethoscope.svelte';
import FaCheckCircle from 'svelte-icons/fa/FaCheckCircle.svelte';
import FaBolt from 'svelte-icons/fa/FaBolt.svelte';
import FaUpload from 'svelte-icons/fa/FaUpload.svelte';
import FaPlay from 'svelte-icons/fa/FaPlay.svelte';
import FaFilePdf from 'svelte-icons/fa/FaFilePdf.svelte';

let id = $derived($page.params.id);
let solicitacao = $state(null);
let loading = $state(true);
let erro = $state('');

// Para enviar laudo
let mostrarFormLaudo = $state(false);
let observacoes = $state('');
let arquivo = $state(null);
let enviandoLaudo = $state(false);
let atualizandoStatus = $state(false);

async function carregarSolicitacao() {
  loading = true;
  erro = '';
  
  try {
    solicitacao = await solicitacaoService.buscarSolicitacao(id);
  } catch (error) {
    console.error('Erro ao carregar solicitação:', error);
    erro = error.message || 'Erro ao carregar solicitação';
  } finally {
    loading = false;
  }
}

async function iniciarAnalise() {
  atualizandoStatus = true;
  try {
    await solicitacaoService.atualizarStatus(id, 'EM_ANALISE');
    await carregarSolicitacao();
    alert('✅ Análise iniciada!');
  } catch (error) {
    alert(`❌ Erro: ${error.message}`);
  } finally {
    atualizandoStatus = false;
  }
}

async function enviarLaudo() {
  if (!observacoes.trim()) {
    alert('Por favor, preencha as observações do laudo.');
    return;
  }
  
  enviandoLaudo = true;
  try {
    await solicitacaoService.enviarResultado(id, observacoes, arquivo);
    await carregarSolicitacao();
    alert('✅ Laudo enviado com sucesso!');
    mostrarFormLaudo = false;
    observacoes = '';
    arquivo = null;
  } catch (error) {
    alert(`❌ Erro ao enviar laudo: ${error.message}`);
  } finally {
    enviandoLaudo = false;
  }
}

function formatarData(dataISO) {
  if (!dataISO) return 'N/A';
  const data = new Date(dataISO);
  return data.toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

function voltar() {
  goto('/patologista/solicitacoes');
}

onMount(() => {
  carregarSolicitacao();
});
</script>

<div class="detalhes-page">
  {#if loading}
    <LoadingSpinner color="#1D6088" size={50} />
  {:else if erro}
    <div class="erro-container">
      <div class="erro-icon">
        <FaExclamationTriangle />
      </div>
      <h2>Ops! Algo deu errado</h2>
      <p>{erro}</p>
      <div class="erro-acoes">
        <button onclick={voltar} class="btn-voltar">
          ← Voltar para lista
        </button>
        <button onclick={carregarSolicitacao} class="btn-retry">
          🔄 Tentar novamente
        </button>
      </div>
    </div>
  {:else if solicitacao}
    <div class="header-detalhes">
      <button onclick={voltar} class="btn-back">
        ← Voltar
      </button>
      <div class="header-info">
        <h1>Solicitação #{solicitacao.protocolo}</h1>
        <StatusBadge status={solicitacao.status} />
      </div>
    </div>

    <div class="content-grid">
      <!-- Coluna Esquerda: Informações -->
      <div class="info-section">
        <div class="card">
          <h2 class="card-title">
            <span class="title-icon"><FaClipboardList /></span>
            Informações da Solicitação
          </h2>
          <div class="info-grid">
            <div class="info-item">
              <span class="label">Protocolo:</span>
              <span class="value">{solicitacao.protocolo}</span>
            </div>
            <div class="info-item">
              <span class="label">Status:</span>
              <span class="value"><StatusBadge status={solicitacao.status} /></span>
            </div>
            <div class="info-item">
              <span class="label">Data da Solicitação:</span>
              <span class="value">{formatarData(solicitacao.dataSolicitacao)}</span>
            </div>
            <div class="info-item">
              <span class="label">Veterinário:</span>
              <span class="value">{solicitacao.veterinarioSolicitante?.nome || 'N/A'}</span>
            </div>
          </div>
        </div>

        <div class="card">
          <h2 class="card-title">
            <span class="title-icon"><FaPaw /></span>
            Dados do Animal
          </h2>
          <div class="info-grid">
            <div class="info-item">
              <span class="label">Nome:</span>
              <span class="value">{solicitacao.animal?.nome || 'N/A'}</span>
            </div>
            <div class="info-item">
              <span class="label">Espécie:</span>
              <span class="value">{solicitacao.animal?.especie || 'N/A'}</span>
            </div>
            <div class="info-item">
              <span class="label">Raça:</span>
              <span class="value">{solicitacao.animal?.raca || 'N/A'}</span>
            </div>
            <div class="info-item">
              <span class="label">Sexo:</span>
              <span class="value">{solicitacao.animal?.sexo || 'N/A'}</span>
            </div>
            <div class="info-item">
              <span class="label">Data de Nascimento:</span>
              <span class="value">{solicitacao.animal?.dataNascimento ? new Date(solicitacao.animal.dataNascimento).toLocaleDateString('pt-BR') : 'N/A'}</span>
            </div>
          </div>
        </div>

        <div class="card">
          <h2 class="card-title">
            <span class="title-icon"><FaUser /></span>
            Tutor
          </h2>
          <div class="info-grid">
            <div class="info-item">
              <span class="label">Nome:</span>
              <span class="value">{solicitacao.animal?.tutor?.nomeCompleto || 'N/A'}</span>
            </div>
            <div class="info-item">
              <span class="label">CPF:</span>
              <span class="value">{solicitacao.animal?.tutor?.cpf || 'N/A'}</span>
            </div>
            <div class="info-item">
              <span class="label">Email:</span>
              <span class="value">{solicitacao.animal?.tutor?.email || 'N/A'}</span>
            </div>
            <div class="info-item">
              <span class="label">Telefone:</span>
              <span class="value">{solicitacao.animal?.tutor?.telefone || 'N/A'}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Coluna Direita: Exames e Ações -->
      <div class="exames-section">
        <div class="card">
          <h2 class="card-title">
            <span class="title-icon"><FaMicroscope /></span>
            Exames Solicitados
          </h2>
          <ul class="exames-list">
            {#each solicitacao.exames || [] as exame}
              <li class="exame-item">
                <strong>{exame.nome}</strong>
                <p>{exame.descricao}</p>
              </li>
            {/each}
          </ul>
        </div>

        <div class="card">
          <h2 class="card-title">
            <span class="title-icon"><FaStethoscope /></span>
            Suspeita Clínica
          </h2>
          <p class="suspeita-texto">{solicitacao.suspeitaClinica}</p>
        </div>

        <!-- Ações baseadas no status -->
        {#if solicitacao.status === 'RECEBIDO'}
          <div class="card acoes-card">
            <h2 class="card-title">
              <span class="title-icon"><FaBolt /></span>
              Ações
            </h2>
            <p class="acoes-descricao">Esta solicitação está aguardando análise. Clique no botão abaixo para iniciar.</p>
            <button 
              onclick={iniciarAnalise} 
              class="btn-acao primario"
              disabled={atualizandoStatus}
            >
              {#if atualizandoStatus}
                Iniciando...
              {:else}
                <span class="btn-icon"><FaPlay /></span>
                Iniciar Análise
              {/if}
            </button>
          </div>
        {:else if solicitacao.status === 'EM_ANALISE'}
          <div class="card acoes-card">
            <h2 class="card-title">
              <span class="title-icon"><FaUpload /></span>
              Enviar Laudo
            </h2>
            
            {#if !mostrarFormLaudo}
              <p class="acoes-descricao">Análise em andamento. Quando concluir, envie o laudo com suas observações.</p>
              <button 
                onclick={() => mostrarFormLaudo = true} 
                class="btn-acao primario"
              >
                📝 Preencher Laudo
              </button>
            {:else}
              <div class="form-laudo">
                <div class="form-grupo">
                  <label for="observacoes">Observações do Laudo:</label>
                  <textarea
                    id="observacoes"
                    bind:value={observacoes}
                    placeholder="Descreva os achados do exame..."
                    rows="6"
                    class="textarea-input"
                  ></textarea>
                </div>

                <div class="form-grupo">
                  <label for="arquivo">Anexar PDF (opcional):</label>
                  <input
                    id="arquivo"
                    type="file"
                    accept=".pdf"
                    onchange={(e) => arquivo = e.target.files[0]}
                    class="file-input"
                  />
                  {#if arquivo}
                    <p class="arquivo-selecionado">
                      <span class="arquivo-icon"><FaFilePdf /></span>
                      {arquivo.name}
                    </p>
                  {/if}
                </div>

                <div class="form-acoes">
                  <button 
                    onclick={() => { mostrarFormLaudo = false; observacoes = ''; arquivo = null; }} 
                    class="btn-acao secundario"
                    disabled={enviandoLaudo}
                  >
                    Cancelar
                  </button>
                  <button 
                    onclick={enviarLaudo} 
                    class="btn-acao primario"
                    disabled={enviandoLaudo || !observacoes.trim()}
                  >
                    {#if enviandoLaudo}
                      Enviando...
                    {:else}
                      <span class="btn-icon"><FaCheckCircle /></span>
                      Enviar Laudo
                    {/if}
                  </button>
                </div>
              </div>
            {/if}
          </div>
        {:else if solicitacao.status === 'CONCLUIDO'}
          <div class="card resultado-card">
            <h2 class="card-title">
              <span class="title-icon"><FaCheckCircle /></span>
              Laudo Enviado
            </h2>
            <div class="info-grid">
              <div class="info-item">
                <span class="label">Data do Laudo:</span>
                <span class="value">{formatarData(solicitacao.resultado?.dataLaudo)}</span>
              </div>
            </div>
            
            <div class="observacoes-box">
              <h3>Suas Observações:</h3>
              <p>{solicitacao.resultado?.observacoes}</p>
            </div>
          </div>
        {/if}
      </div>
    </div>
  {/if}
</div>

<style>
/* Mesmos estilos base da página do veterinário */
.detalhes-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
  height: 100%;
  overflow-y: auto;
}

@media (min-width: 768px) {
  .detalhes-page {
    gap: 24px;
  }
}

.header-detalhes {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

@media (min-width: 768px) {
  .header-detalhes {
    gap: 16px;
  }
}

.btn-back {
  align-self: flex-start;
  padding: 6px 12px;
  background: #f3f4f6;
  border: none;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 600;
  color: #374151;
  cursor: pointer;
  transition: all 0.2s;
}

@media (min-width: 768px) {
  .btn-back {
    padding: 8px 16px;
    border-radius: 8px;
    font-size: 14px;
  }
}

.btn-back:hover {
  background: #e5e7eb;
}

.header-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.header-info h1 {
  font-size: 22px;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

@media (min-width: 768px) {
  .header-info h1 {
    font-size: 32px;
  }
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
}

@media (min-width: 768px) {
  .content-grid {
    grid-template-columns: 1fr 1fr;
    gap: 24px;
  }
}

.card {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 16px;
  margin-bottom: 16px;
}

@media (min-width: 768px) {
  .card {
    border-radius: 12px;
    padding: 24px;
    margin-bottom: 20px;
  }
}

.card-title {
  font-size: 16px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 16px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

@media (min-width: 768px) {
  .card-title {
    font-size: 18px;
    margin: 0 0 20px 0;
    gap: 12px;
  }
}

.title-icon {
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
  color: #1D6088;
  flex-shrink: 0;
}

@media (min-width: 768px) {
  .title-icon {
    width: 20px;
    height: 20px;
  }
}

.info-grid {
  display: grid;
  gap: 12px;
}

@media (min-width: 768px) {
  .info-grid {
    gap: 16px;
  }
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

@media (min-width: 768px) {
  .info-item {
    gap: 4px;
  }
}

.label {
  font-size: 11px;
  font-weight: 600;
  color: #6b7280;
  text-transform: uppercase;
  letter-spacing: 0.3px;
}

@media (min-width: 768px) {
  .label {
    font-size: 13px;
    letter-spacing: 0.5px;
  }
}

.value {
  font-size: 14px;
  color: #1f2937;
  font-weight: 500;
  word-break: break-word;
}

@media (min-width: 768px) {
  .value {
    font-size: 15px;
  }
}

.exames-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.exame-item {
  padding: 12px;
  background: #f9fafb;
  border-radius: 8px;
  border-left: 3px solid #1D6088;
}

.exame-item strong {
  display: block;
  color: #1f2937;
  margin-bottom: 4px;
}

.exame-item p {
  margin: 0;
  font-size: 13px;
  color: #6b7280;
}

.suspeita-texto {
  margin: 0;
  padding: 16px;
  background: #fef3c7;
  border-radius: 8px;
  color: #78350f;
  line-height: 1.6;
}

/* Ações */
.acoes-card {
  border: 2px solid #DBEAFE;
  background: #EFF6FF;
}

.acoes-descricao {
  margin: 0 0 16px 0;
  color: #6b7280;
  line-height: 1.6;
}

.btn-acao {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  min-height: 44px;
}

@media (min-width: 768px) {
  .btn-acao {
    padding: 14px;
    font-size: 15px;
  }
}

.btn-acao.primario {
  background: #1D6088;
  color: white;
}

.btn-acao.primario {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-acao.primario:hover:not(:disabled) {
  background: #165070;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(29, 96, 136, 0.3);
}

.btn-acao.secundario {
  background: #e5e7eb;
  color: #374151;
}

.btn-icon {
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
}

.btn-acao.secundario:hover:not(:disabled) {
  background: #d1d5db;
}

.btn-acao:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Form Laudo */
.form-laudo {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

@media (min-width: 768px) {
  .form-laudo {
    gap: 20px;
  }
}

.form-grupo {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

@media (min-width: 768px) {
  .form-grupo {
    gap: 8px;
  }
}

.form-grupo label {
  font-size: 13px;
  font-weight: 600;
  color: #374151;
}

@media (min-width: 768px) {
  .form-grupo label {
    font-size: 14px;
  }
}

.textarea-input {
  padding: 10px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
  font-family: inherit;
  resize: vertical;
  transition: border-color 0.2s;
  min-height: 120px;
}

@media (min-width: 768px) {
  .textarea-input {
    padding: 12px;
  }
}

.textarea-input:focus {
  outline: none;
  border-color: #1D6088;
  box-shadow: 0 0 0 3px rgba(29, 96, 136, 0.1);
}

.file-input {
  padding: 8px;
  border: 2px dashed #d1d5db;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
}

@media (min-width: 768px) {
  .file-input {
    padding: 10px;
    font-size: 14px;
  }
}

.arquivo-selecionado {
  margin: 4px 0 0 0;
  font-size: 12px;
  color: #059669;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 6px;
}

@media (min-width: 768px) {
  .arquivo-selecionado {
    font-size: 13px;
  }
}

.arquivo-icon {
  width: 14px;
  height: 14px;
  display: flex;
  align-items: center;
}

.form-acoes {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

@media (min-width: 768px) {
  .form-acoes {
    flex-direction: row;
    gap: 12px;
  }
}

.resultado-card {
  border: 2px solid #D1FAE5;
  background: #F0FDF4;
}

.observacoes-box {
  margin: 20px 0 0 0;
  padding: 16px;
  background: white;
  border-radius: 8px;
}

.observacoes-box h3 {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 8px 0;
}

.observacoes-box p {
  margin: 0;
  color: #1f2937;
  line-height: 1.6;
}

/* Erro */
.erro-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.erro-icon {
  width: 64px;
  height: 64px;
  display: flex;
  align-items: center;
  color: #f59e0b;
  margin-bottom: 16px;
}

.erro-container h2 {
  font-size: 24px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 8px 0;
}

.erro-container p {
  color: #991b1b;
  font-weight: 500;
  margin: 0 0 24px 0;
}

.erro-acoes {
  display: flex;
  gap: 12px;
}

.btn-voltar,
.btn-retry {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-voltar {
  background: #e5e7eb;
  color: #374151;
}

.btn-voltar:hover {
  background: #d1d5db;
}

.btn-retry {
  background: #ef4444;
  color: white;
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
}

.btn-retry:hover {
  background: #dc2626;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.4);
}
</style>
