<script>
import { onMount } from 'svelte';
import { goto } from '$app/navigation';
import { authStore } from '$lib/stores/auth';
import { 
  solicitacaoService, 
  tutorService, 
  animalService, 
  tipoExameService 
} from '$lib/mocks/services';
import LoadingSpinner from '$lib/components/LoadingSpinner.svelte';
import {
  validarCPF,
  validarTelefone,
  validarEmail,
  validarObrigatorio,
  validarDataNascimento,
  formatarCPF,
  formatarTelefone
} from '$lib/utils/validacoes';

// Ícones
import FaUser from 'svelte-icons/fa/FaUser.svelte';
import FaPaw from 'svelte-icons/fa/FaPaw.svelte';
import FaMicroscope from 'svelte-icons/fa/FaMicroscope.svelte';
import FaStethoscope from 'svelte-icons/fa/FaStethoscope.svelte';
import FaClipboardCheck from 'svelte-icons/fa/FaClipboardCheck.svelte';
import FaCheck from 'svelte-icons/fa/FaCheck.svelte';
import FaExclamationTriangle from 'svelte-icons/fa/FaExclamationTriangle.svelte';
import FaArrowLeft from 'svelte-icons/fa/FaArrowLeft.svelte';

let currentStep = $state(1);
let loading = $state(false);
let carregandoDados = $state(true);

// Erros de validação
let erros = $state({
  tutor: {
    nomeCompleto: null,
    cpf: null,
    email: null,
    telefone: null
  },
  animal: {
    nome: null,
    especie: null,
    dataNascimento: null
  },
  exames: null,
  suspeita: null
});

// Dados carregados
let tutores = $state([]);
let animais = $state([]);
let tiposExames = $state([]);

// Formulário
let tutorSelecionado = $state(null);
let criarNovoTutor = $state(false);
let novoTutor = $state({
  nomeCompleto: '',
  cpf: '',
  email: '',
  telefone: ''
});

let animalSelecionado = $state(null);
let criarNovoAnimal = $state(false);
let novoAnimal = $state({
  nome: '',
  especie: '',
  raca: '',
  sexo: '',
  dataNascimento: '',
  rgAnimal: ''
});

let examesSelecionados = $state([]);
let suspeitaClinica = $state('');

let usuario = $derived($authStore.usuario);

async function carregarDados() {
  carregandoDados = true;
  try {
    [tutores, tiposExames] = await Promise.all([
      tutorService.listarTutores(),
      tipoExameService.listarTiposExames()
    ]);
  } catch (error) {
    alert(`Erro ao carregar dados: ${error.message}`);
  } finally {
    carregandoDados = false;
  }
}

async function carregarAnimaisDoTutor(tutorId) {
  const todosAnimais = await animalService.listarAnimais();
  animais = todosAnimais.filter(a => a.tutor.id === tutorId);
}

// Funções de validação específicas
function validarTutorNovo() {
  let temErro = false;
  
  const validacaoNome = validarObrigatorio(novoTutor.nomeCompleto, 'Nome completo');
  erros.tutor.nomeCompleto = validacaoNome.erro;
  if (!validacaoNome.valido) temErro = true;
  
  const validacaoCPF = validarCPF(novoTutor.cpf);
  erros.tutor.cpf = validacaoCPF.erro;
  if (!validacaoCPF.valido) temErro = true;
  
  const validacaoTelefone = validarTelefone(novoTutor.telefone);
  erros.tutor.telefone = validacaoTelefone.erro;
  if (!validacaoTelefone.valido) temErro = true;
  
  if (novoTutor.email) {
    const validacaoEmail = validarEmail(novoTutor.email);
    erros.tutor.email = validacaoEmail.erro;
    if (!validacaoEmail.valido) temErro = true;
  }
  
  return !temErro;
}

function validarAnimalNovo() {
  let temErro = false;
  
  const validacaoNome = validarObrigatorio(novoAnimal.nome, 'Nome');
  erros.animal.nome = validacaoNome.erro;
  if (!validacaoNome.valido) temErro = true;
  
  const validacaoEspecie = validarObrigatorio(novoAnimal.especie, 'Espécie');
  erros.animal.especie = validacaoEspecie.erro;
  if (!validacaoEspecie.valido) temErro = true;
  
  if (novoAnimal.dataNascimento) {
    const validacaoData = validarDataNascimento(novoAnimal.dataNascimento);
    erros.animal.dataNascimento = validacaoData.erro;
    if (!validacaoData.valido) temErro = true;
  }
  
  return !temErro;
}

// Formatação automática
function formatarCamposCPF() {
  novoTutor.cpf = formatarCPF(novoTutor.cpf);
}

function formatarCamposTelefone() {
  novoTutor.telefone = formatarTelefone(novoTutor.telefone);
}

async function proximoStep() {
  if (currentStep === 1) {
    // Valida tutor
    if (!criarNovoTutor && !tutorSelecionado) {
      alert('Selecione um tutor ou crie um novo');
      return;
    }
    
    if (criarNovoTutor) {
      if (!validarTutorNovo()) {
        return;
      }
      
      loading = true;
      try {
        const tutorCriado = await tutorService.criarTutor({
          ...novoTutor,
          cpf: novoTutor.cpf.replace(/\D/g, '') // Remove formatação
        });
        tutorSelecionado = tutorCriado.id;
        tutores.push(tutorCriado);
        criarNovoTutor = false;
      } catch (error) {
        alert(`Erro ao criar tutor: ${error.message}`);
        loading = false;
        return;
      }
      loading = false;
    }
    
    await carregarAnimaisDoTutor(tutorSelecionado);
  }
  
  if (currentStep === 2) {
    // Valida animal
    if (!criarNovoAnimal && !animalSelecionado) {
      alert('Selecione um animal ou crie um novo');
      return;
    }
    
    if (criarNovoAnimal) {
      if (!validarAnimalNovo()) {
        return;
      }
      
      loading = true;
      try {
        const animalCriado = await animalService.criarAnimal({
          ...novoAnimal,
          tutor: { id: tutorSelecionado }
        });
        animalSelecionado = animalCriado.id;
        criarNovoAnimal = false;
      } catch (error) {
        alert(`Erro ao criar animal: ${error.message}`);
        loading = false;
        return;
      }
      loading = false;
    }
  }
  
  if (currentStep === 3) {
    // Valida exames
    if (examesSelecionados.length === 0) {
      erros.exames = 'Selecione pelo menos um exame';
      return;
    }
    erros.exames = null;
  }
  
  if (currentStep === 4) {
    // Valida suspeita
    const validacaoSuspeita = validarObrigatorio(suspeitaClinica, 'Suspeita clínica');
    erros.suspeita = validacaoSuspeita.erro;
    if (!validacaoSuspeita.valido) {
      return;
    }
  }
  
  currentStep++;
}

function voltarStep() {
  if (currentStep > 1) {
    currentStep--;
  }
}

function toggleExame(exameId) {
  if (examesSelecionados.includes(exameId)) {
    examesSelecionados = examesSelecionados.filter(id => id !== exameId);
  } else {
    examesSelecionados = [...examesSelecionados, exameId];
  }
}

async function enviarSolicitacao() {
  loading = true;
  try {
    const novaSolicitacao = await solicitacaoService.criarSolicitacao({
      animal: { id: animalSelecionado },
      veterinarioSolicitante: { id: usuario.id },
      exames: examesSelecionados.map(id => ({ id })),
      suspeitaClinica: suspeitaClinica
    });
    
    alert(`✅ Solicitação criada!\nProtocolo: ${novaSolicitacao.protocolo}`);
    goto(`/veterinario/solicitacoes/${novaSolicitacao.id}`);
  } catch (error) {
    alert(`❌ Erro ao criar solicitação: ${error.message}`);
  } finally {
    loading = false;
  }
}

function handleVoltar() {
  goto('/veterinario/home');
}

onMount(() => {
  carregarDados();
});
</script>

<div class="nova-solicitacao">
  {#if carregandoDados}
    <LoadingSpinner color="#5DB578" size={50} />
  {:else}
    <div class="page-header">
      <button class="voltar-btn" onclick={handleVoltar}>
        <span class="icon-btn">
          <FaArrowLeft />
        </span>
      </button>
      <h1>Nova Solicitação de Exame</h1>
    </div>

    <!-- Indicador de Steps -->
    <div class="steps-indicator">
      <div class="step" class:active={currentStep >= 1} class:completed={currentStep > 1}>
        <div class="step-number">
          {#if currentStep > 1}
            <span class="step-icon"><FaCheck /></span>
          {:else}
            1
          {/if}
        </div>
        <div class="step-label">Tutor</div>
      </div>
      <div class="step-line" class:active={currentStep > 1}></div>
      <div class="step" class:active={currentStep >= 2} class:completed={currentStep > 2}>
        <div class="step-number">
          {#if currentStep > 2}
            <span class="step-icon"><FaCheck /></span>
          {:else}
            2
          {/if}
        </div>
        <div class="step-label">Animal</div>
      </div>
      <div class="step-line" class:active={currentStep > 2}></div>
      <div class="step" class:active={currentStep >= 3} class:completed={currentStep > 3}>
        <div class="step-number">
          {#if currentStep > 3}
            <span class="step-icon"><FaCheck /></span>
          {:else}
            3
          {/if}
        </div>
        <div class="step-label">Exames</div>
      </div>
      <div class="step-line" class:active={currentStep > 3}></div>
      <div class="step" class:active={currentStep >= 4} class:completed={currentStep > 4}>
        <div class="step-number">
          {#if currentStep > 4}
            <span class="step-icon"><FaCheck /></span>
          {:else}
            4
          {/if}
        </div>
        <div class="step-label">Suspeita</div>
      </div>
      <div class="step-line" class:active={currentStep > 4}></div>
      <div class="step" class:active={currentStep >= 5}>
        <div class="step-number">5</div>
        <div class="step-label">Revisar</div>
      </div>
    </div>

    <form onsubmit={(e) => { e.preventDefault(); currentStep === 5 ? enviarSolicitacao() : proximoStep(); }}>
      <!-- STEP 1: Seleção de Tutor -->
      {#if currentStep === 1}
        <section class="form-section">
          <h2>
            <span class="section-icon"><FaUser /></span>
            Seleção de Tutor
          </h2>
          
          <div class="option-toggle">
            <button 
              type="button"
              class="toggle-btn"
              class:active={!criarNovoTutor}
              onclick={() => criarNovoTutor = false}
            >
              Selecionar Existente
            </button>
            <button 
              type="button"
              class="toggle-btn"
              class:active={criarNovoTutor}
              onclick={() => criarNovoTutor = true}
            >
              Criar Novo Tutor
            </button>
          </div>

          {#if !criarNovoTutor}
            <div class="select-list">
              {#each tutores as tutor}
                <button
                  type="button"
                  class="select-card"
                  class:selected={tutorSelecionado === tutor.id}
                  onclick={() => tutorSelecionado = tutor.id}
                >
                  <div class="card-header">
                    <strong>{tutor.nomeCompleto}</strong>
                    {#if tutorSelecionado === tutor.id}
                      <span class="check"><FaCheck /></span>
                    {/if}
                  </div>
                  <p>CPF: {tutor.cpf}</p>
                  <p>Tel: {tutor.telefone}</p>
                </button>
              {/each}
            </div>
          {:else}
            <div class="form-grid">
              <div class="form-group">
                <label for="nome_tutor">Nome Completo: *</label>
                <input 
                  type="text" 
                  id="nome_tutor" 
                  bind:value={novoTutor.nomeCompleto}
                  onblur={() => erros.tutor.nomeCompleto = validarObrigatorio(novoTutor.nomeCompleto, 'Nome completo').erro}
                  required
                  class="form-input"
                  class:erro={erros.tutor.nomeCompleto}
                />
                {#if erros.tutor.nomeCompleto}
                  <span class="erro-msg">{erros.tutor.nomeCompleto}</span>
                {/if}
              </div>
              <div class="form-group">
                <label for="cpf">CPF: *</label>
                <input 
                  type="text" 
                  id="cpf" 
                  bind:value={novoTutor.cpf}
                  onblur={() => { formatarCamposCPF(); erros.tutor.cpf = validarCPF(novoTutor.cpf).erro; }}
                  required
                  class="form-input"
                  class:erro={erros.tutor.cpf}
                  placeholder="000.000.000-00"
                  maxlength="14"
                />
                {#if erros.tutor.cpf}
                  <span class="erro-msg">{erros.tutor.cpf}</span>
                {/if}
              </div>
              <div class="form-group">
                <label for="email_tutor">Email:</label>
                <input 
                  type="email" 
                  id="email_tutor" 
                  bind:value={novoTutor.email}
                  onblur={() => erros.tutor.email = validarEmail(novoTutor.email).erro}
                  class="form-input"
                  class:erro={erros.tutor.email}
                />
                {#if erros.tutor.email}
                  <span class="erro-msg">{erros.tutor.email}</span>
                {/if}
              </div>
              <div class="form-group">
                <label for="telefone_tutor">Telefone: *</label>
                <input 
                  type="tel" 
                  id="telefone_tutor" 
                  bind:value={novoTutor.telefone}
                  onblur={() => { formatarCamposTelefone(); erros.tutor.telefone = validarTelefone(novoTutor.telefone).erro; }}
                  required
                  class="form-input"
                  class:erro={erros.tutor.telefone}
                  placeholder="(00) 00000-0000"
                  maxlength="15"
                />
                {#if erros.tutor.telefone}
                  <span class="erro-msg">{erros.tutor.telefone}</span>
                {/if}
              </div>
            </div>
          {/if}
        </section>
      {/if}

      <!-- STEP 2: Seleção de Animal -->
      {#if currentStep === 2}
        <section class="form-section">
          <h2>
            <span class="section-icon"><FaPaw /></span>
            Seleção de Animal
          </h2>
          
          <div class="option-toggle">
            <button 
              type="button"
              class="toggle-btn"
              class:active={!criarNovoAnimal}
              onclick={() => criarNovoAnimal = false}
            >
              Selecionar Existente
            </button>
            <button 
              type="button"
              class="toggle-btn"
              class:active={criarNovoAnimal}
              onclick={() => criarNovoAnimal = true}
            >
              Criar Novo Animal
            </button>
          </div>

          {#if !criarNovoAnimal}
            {#if animais.length === 0}
              <div class="empty-message">
                <p>Este tutor ainda não tem animais cadastrados.</p>
                <button 
                  type="button"
                  class="btn-small"
                  onclick={() => criarNovoAnimal = true}
                >
                  + Cadastrar Animal
                </button>
              </div>
            {:else}
              <div class="select-list">
                {#each animais as animal}
                  <button
                    type="button"
                    class="select-card"
                    class:selected={animalSelecionado === animal.id}
                    onclick={() => animalSelecionado = animal.id}
                  >
                    <div class="card-header">
                      <strong>{animal.nome}</strong>
                      {#if animalSelecionado === animal.id}
                        <span class="check"><FaCheck /></span>
                      {/if}
                    </div>
                    <p>{animal.especie} • {animal.raca || 'SRD'}</p>
                    <p>Sexo: {animal.sexo || 'N/A'}</p>
                  </button>
                {/each}
              </div>
            {/if}
          {:else}
            <div class="form-grid">
              <div class="form-group">
                <label for="nome_animal">Nome: *</label>
                <input 
                  type="text" 
                  id="nome_animal" 
                  bind:value={novoAnimal.nome}
                  onblur={() => erros.animal.nome = validarObrigatorio(novoAnimal.nome, 'Nome').erro}
                  required
                  class="form-input"
                  class:erro={erros.animal.nome}
                />
                {#if erros.animal.nome}
                  <span class="erro-msg">{erros.animal.nome}</span>
                {/if}
              </div>
              <div class="form-group">
                <label for="especie">Espécie: *</label>
                <input 
                  type="text" 
                  id="especie" 
                  bind:value={novoAnimal.especie}
                  onblur={() => erros.animal.especie = validarObrigatorio(novoAnimal.especie, 'Espécie').erro}
                  required
                  class="form-input"
                  class:erro={erros.animal.especie}
                  placeholder="Ex: Cachorro, Gato..."
                />
                {#if erros.animal.especie}
                  <span class="erro-msg">{erros.animal.especie}</span>
                {/if}
              </div>
              <div class="form-group">
                <label for="raca">Raça:</label>
                <input 
                  type="text" 
                  id="raca" 
                  bind:value={novoAnimal.raca}
                  class="form-input"
                  placeholder="Ex: Labrador, SRD..."
                />
              </div>
              <div class="form-group">
                <label for="sexo">Sexo:</label>
                <select id="sexo" bind:value={novoAnimal.sexo} class="form-input">
                  <option value="">Selecione...</option>
                  <option value="MACHO">Macho</option>
                  <option value="FEMEA">Fêmea</option>
                </select>
              </div>
              <div class="form-group">
                <label for="data_nascimento">Data de Nascimento:</label>
                <input 
                  type="date" 
                  id="data_nascimento" 
                  bind:value={novoAnimal.dataNascimento}
                  onblur={() => erros.animal.dataNascimento = validarDataNascimento(novoAnimal.dataNascimento).erro}
                  class="form-input"
                  class:erro={erros.animal.dataNascimento}
                />
                {#if erros.animal.dataNascimento}
                  <span class="erro-msg">{erros.animal.dataNascimento}</span>
                {/if}
              </div>
              <div class="form-group">
                <label for="rg_animal">RG Animal:</label>
                <input 
                  type="text" 
                  id="rg_animal" 
                  bind:value={novoAnimal.rgAnimal}
                  class="form-input"
                />
              </div>
            </div>
          {/if}
        </section>
      {/if}

      <!-- STEP 3: Seleção de Exames -->
      {#if currentStep === 3}
        <section class="form-section">
          <h2>
            <span class="section-icon"><FaMicroscope /></span>
            Seleção de Exames
          </h2>
          <p class="section-description">Selecione os exames que deseja solicitar:</p>
          
          {#if erros.exames}
            <div class="erro-banner">{erros.exames}</div>
          {/if}
          
          <div class="exames-grid">
            {#each tiposExames as exame}
              <button
                type="button"
                class="exame-card"
                class:selected={examesSelecionados.includes(exame.id)}
                onclick={() => toggleExame(exame.id)}
              >
                <div class="exame-check">
                  {#if examesSelecionados.includes(exame.id)}
                    <span class="check-icon"><FaCheck /></span>
                  {/if}
                </div>
                <div class="exame-info">
                  <strong>{exame.nome}</strong>
                  <p>{exame.descricao}</p>
                </div>
              </button>
            {/each}
          </div>
        </section>
      {/if}

      <!-- STEP 4: Suspeita Clínica -->
      {#if currentStep === 4}
        <section class="form-section">
          <h2>
            <span class="section-icon"><FaStethoscope /></span>
            Suspeita Clínica
          </h2>
          <p class="section-description">Descreva a suspeita clínica e histórico relevante:</p>
          
          <textarea
            bind:value={suspeitaClinica}
            onblur={() => erros.suspeita = validarObrigatorio(suspeitaClinica, 'Suspeita clínica').erro}
            required
            class="textarea-large"
            class:erro={erros.suspeita}
            rows="8"
            placeholder="Descreva os sintomas, histórico clínico, medicações em uso, e qualquer informação relevante para a análise..."
          ></textarea>
          {#if erros.suspeita}
            <span class="erro-msg">{erros.suspeita}</span>
          {/if}
        </section>
      {/if}

      <!-- STEP 5: Revisão -->
      {#if currentStep === 5}
        <section class="form-section">
          <h2>
            <span class="section-icon"><FaClipboardCheck /></span>
            Revisão da Solicitação
          </h2>
          
          <div class="review-section">
            <h3>Tutor</h3>
            <p><strong>{tutores.find(t => t.id === tutorSelecionado)?.nomeCompleto}</strong></p>
            <p>CPF: {tutores.find(t => t.id === tutorSelecionado)?.cpf}</p>
          </div>

          <div class="review-section">
            <h3>Animal</h3>
            {#if criarNovoAnimal}
              <p><strong>{novoAnimal.nome}</strong></p>
              <p>{novoAnimal.especie} • {novoAnimal.raca || 'SRD'}</p>
            {:else}
              {#if animais.find(a => a.id === animalSelecionado)}
                <p><strong>{animais.find(a => a.id === animalSelecionado)?.nome}</strong></p>
                <p>{animais.find(a => a.id === animalSelecionado)?.especie} • {animais.find(a => a.id === animalSelecionado)?.raca || 'SRD'}</p>
              {/if}
            {/if}
          </div>

          <div class="review-section">
            <h3>Exames Selecionados ({examesSelecionados.length})</h3>
            <ul class="review-list">
              {#each examesSelecionados as exameId}
                {#if tiposExames.find(e => e.id === exameId)}
                  <li>{tiposExames.find(e => e.id === exameId)?.nome}</li>
                {/if}
              {/each}
            </ul>
          </div>

          <div class="review-section">
            <h3>Suspeita Clínica</h3>
            <div class="review-text">{suspeitaClinica}</div>
          </div>
        </section>
      {/if}
      
      <div class="form-actions">
        {#if currentStep > 1}
          <button type="button" class="cancel-btn" onclick={voltarStep} disabled={loading}>
            ← Voltar
          </button>
        {:else}
          <button type="button" class="cancel-btn" onclick={handleVoltar} disabled={loading}>
            Cancelar
          </button>
        {/if}
        
        <button type="submit" class="submit-btn" disabled={loading}>
          {#if loading}
            Aguarde...
          {:else if currentStep === 5}
            <span class="btn-icon"><FaCheck /></span>
            Criar Solicitação
          {:else}
            Próximo
          {/if}
        </button>
      </div>
    </form>
  {/if}
</div>

<style>
.nova-solicitacao {
  padding: 0;
  height: 100%;
  overflow-y: auto;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.voltar-btn {
  background: #f3f4f6;
  border: none;
  padding: 10px 12px;
  border-radius: 8px;
  margin-right: 16px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
}

.voltar-btn:hover {
  background: #e5e7eb;
}

.icon-btn {
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  color: #374151;
}

h1 {
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  color: #1f2937;
}

h2 {
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 16px 0;
  color: #1f2937;
  display: flex;
  align-items: center;
  gap: 12px;
}

.section-icon {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  color: #5DB578;
}

h3 {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 8px 0;
}

/* Steps Indicator */
.steps-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40px;
  padding: 20px;
  background: #f9fafb;
  border-radius: 12px;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.step-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 2px solid #d1d5db;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  color: #9ca3af;
  transition: all 0.3s;
}

.step-icon {
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
}

.step.active .step-number {
  border-color: #5DB578;
  color: #5DB578;
  background: #D1FAE5;
}

.step.completed .step-number {
  border-color: #5DB578;
  background: #5DB578;
  color: white;
}

.step-label {
  font-size: 13px;
  font-weight: 500;
  color: #6b7280;
}

.step.active .step-label {
  color: #1f2937;
  font-weight: 600;
}

.step-line {
  width: 60px;
  height: 2px;
  background: #d1d5db;
  transition: all 0.3s;
}

.step-line.active {
  background: #5DB578;
}

/* Form */
.form-section {
  background: #f9fafb;
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 24px;
  border: 1px solid #e5e7eb;
}

.section-description {
  color: #6b7280;
  margin: -8px 0 20px 0;
  font-size: 14px;
}

/* Toggle Buttons */
.option-toggle {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.toggle-btn {
  flex: 1;
  padding: 12px;
  border: 2px solid #e5e7eb;
  background: white;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.toggle-btn.active {
  border-color: #5DB578;
  background: #D1FAE5;
  color: #065F46;
}

.toggle-btn:hover:not(.active) {
  border-color: #d1d5db;
}

/* Select Cards */
.select-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 12px;
}

.select-card {
  padding: 16px;
  border: 2px solid #e5e7eb;
  background: white;
  border-radius: 8px;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s;
}

.select-card:hover {
  border-color: #5DB578;
}

.select-card.selected {
  border-color: #5DB578;
  background: #F0FDF4;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.card-header strong {
  color: #1f2937;
}

.check {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  color: #5DB578;
}

.select-card p {
  margin: 4px 0;
  font-size: 13px;
  color: #6b7280;
}

/* Form Grid */
.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

label {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
}

.form-input,
select {
  padding: 10px 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
  background: white;
  transition: border-color 0.2s;
}

.form-input:focus,
select:focus {
  outline: none;
  border-color: #5DB578;
  box-shadow: 0 0 0 3px rgba(93, 181, 120, 0.1);
}

/* Exames Grid */
.exames-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 12px;
}

.exame-card {
  display: flex;
  gap: 12px;
  padding: 16px;
  border: 2px solid #e5e7eb;
  background: white;
  border-radius: 8px;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s;
}

.exame-card:hover {
  border-color: #5DB578;
}

.exame-card.selected {
  border-color: #5DB578;
  background: #F0FDF4;
}

.exame-check {
  width: 24px;
  height: 24px;
  border: 2px solid #d1d5db;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.2s;
}

.check-icon {
  width: 14px;
  height: 14px;
  display: flex;
  align-items: center;
  color: #5DB578;
}

.exame-card.selected .exame-check {
  border-color: #5DB578;
  background: #5DB578;
}

.exame-card.selected .check-icon {
  color: white;
}

.exame-info strong {
  display: block;
  color: #1f2937;
  margin-bottom: 4px;
}

.exame-info p {
  margin: 0;
  font-size: 13px;
  color: #6b7280;
}

/* Textarea */
.textarea-large {
  width: 100%;
  padding: 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
  font-family: inherit;
  line-height: 1.6;
  resize: vertical;
  background: white;
  transition: border-color 0.2s;
}

.textarea-large:focus {
  outline: none;
  border-color: #5DB578;
  box-shadow: 0 0 0 3px rgba(93, 181, 120, 0.1);
}

/* Review */
.review-section {
  padding: 16px;
  background: white;
  border-radius: 8px;
  margin-bottom: 16px;
  border-left: 3px solid #5DB578;
}

.review-section p {
  margin: 4px 0;
  color: #1f2937;
}

.review-list {
  list-style: none;
  padding: 0;
  margin: 8px 0 0 0;
}

.review-list li {
  padding: 8px;
  background: #f9fafb;
  border-radius: 4px;
  margin-bottom: 4px;
  color: #1f2937;
}

.review-text {
  padding: 12px;
  background: #fef3c7;
  border-radius: 8px;
  color: #78350f;
  line-height: 1.6;
  margin-top: 8px;
}

/* Empty State */
.empty-message {
  text-align: center;
  padding: 40px 20px;
  color: #6b7280;
}

.btn-small {
  padding: 8px 16px;
  background: #5DB578;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 12px;
}

.btn-small:hover {
  background: #4a9163;
}

/* Actions */
.form-actions {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e5e7eb;
}

.cancel-btn,
.submit-btn {
  padding: 12px 32px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.cancel-btn {
  border: 1px solid #d1d5db;
  background: white;
  color: #374151;
}

.cancel-btn:hover:not(:disabled) {
  background: #f3f4f6;
}

.submit-btn {
  border: none;
  background: #5DB578;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.submit-btn:hover:not(:disabled) {
  background: #4a9163;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(93, 181, 120, 0.3);
}

.btn-icon {
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
}

.cancel-btn:disabled,
.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Validação e Erros */
.form-input.erro,
.textarea-large.erro,
select.erro {
  border-color: #ef4444;
  background: #fef2f2;
}

.form-input.erro:focus,
.textarea-large.erro:focus {
  border-color: #dc2626;
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
}

.erro-msg {
  display: block;
  color: #dc2626;
  font-size: 13px;
  font-weight: 500;
  margin-top: 4px;
}

.erro-banner {
  padding: 12px 16px;
  background: #fef2f2;
  border: 1px solid #fecaca;
  border-radius: 8px;
  color: #991b1b;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 20px;
}
</style>
