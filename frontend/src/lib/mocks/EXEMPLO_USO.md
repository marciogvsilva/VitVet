# Exemplos Práticos de Uso dos Mocks

## Exemplo 1: Integrar Login na Página Principal

```svelte
<script>
// routes/+page.svelte
import { goto } from '$app/navigation';
import { authService } from '$lib/mocks/services';
import { authStore } from '$lib/stores/auth';

let userType = "patologista";
let email = "";
let senha = "";
let loading = false;
let erro = "";

async function handleLogin(event) {
  event.preventDefault();
  loading = true;
  erro = "";
  
  try {
    // Chama o serviço de login mock
    const response = await authService.login(email, senha);
    
    // Busca dados do usuário
    const usuario = await authService.getUsuarioLogado(response.token);
    
    // Salva no store
    authStore.login(response.token, usuario);
    
    // Redireciona baseado no papel
    if (usuario.papel === 'PATOLOGISTA') {
      goto('/patologista/home');
    } else {
      goto('/veterinario/home');
    }
  } catch (error) {
    erro = error.message || "Erro ao fazer login";
  } finally {
    loading = false;
  }
}
</script>

<form on:submit={handleLogin}>
  <input type="email" bind:value={email} required />
  <input type="password" bind:value={senha} required />
  
  {#if erro}
    <p class="error">{erro}</p>
  {/if}
  
  <button type="submit" disabled={loading}>
    {loading ? 'Entrando...' : 'Entrar'}
  </button>
</form>
```

## Exemplo 2: Proteção de Rota

```svelte
<script>
// routes/(app)/+layout.svelte
import { onMount } from 'svelte';
import { goto } from '$app/navigation';
import { authStore } from '$lib/stores/auth';

let { children } = $props();
let isAuthenticated = $derived($authStore.isAuthenticated);

onMount(() => {
  if (!isAuthenticated) {
    goto('/');
  }
});
</script>

{#if isAuthenticated}
  {@render children()}
{:else}
  <p>Redirecionando...</p>
{/if}
```

## Exemplo 3: Listar Solicitações do Veterinário

```svelte
<script>
// routes/(app)/veterinario/solicitacoes/+page.svelte
import { onMount } from 'svelte';
import { solicitacaoService } from '$lib/mocks/services';

let solicitacoes = $state([]);
let loading = $state(true);
let filtroStatus = $state('');

async function carregarSolicitacoes() {
  loading = true;
  try {
    const filtros = {};
    if (filtroStatus) {
      filtros.status = filtroStatus;
    }
    
    solicitacoes = await solicitacaoService.listarSolicitacoes(filtros);
  } catch (error) {
    console.error('Erro ao carregar:', error);
  } finally {
    loading = false;
  }
}

onMount(() => {
  carregarSolicitacoes();
});
</script>

<select bind:value={filtroStatus} onchange={carregarSolicitacoes}>
  <option value="">Todos os status</option>
  <option value="RECEBIDO">Recebido</option>
  <option value="EM_ANALISE">Em Análise</option>
  <option value="CONCLUIDO">Concluído</option>
</select>

{#if loading}
  <p>Carregando...</p>
{:else}
  {#each solicitacoes as sol}
    <div class="card">
      <h3>Protocolo: {sol.protocolo}</h3>
      <p>Animal: {sol.animal.nome}</p>
      <p>Status: {sol.status}</p>
      <p>Data: {new Date(sol.dataSolicitacao).toLocaleDateString()}</p>
    </div>
  {/each}
{/if}
```

## Exemplo 4: Criar Nova Solicitação

```svelte
<script>
import { goto } from '$app/navigation';
import { solicitacaoService, animalService, tipoExameService } from '$lib/mocks/services';
import { authStore } from '$lib/stores/auth';

let animais = $state([]);
let tiposExames = $state([]);
let loading = $state(false);

let form = $state({
  animalId: null,
  examesSelecionados: [],
  suspeitaClinica: ''
});

async function carregarDados() {
  animais = await animalService.listarAnimais();
  tiposExames = await tipoExameService.listarTiposExames();
}

async function handleSubmit() {
  loading = true;
  
  try {
    const novaSolicitacao = await solicitacaoService.criarSolicitacao({
      animal: { id: form.animalId },
      veterinarioSolicitante: { id: $authStore.usuario.id },
      exames: form.examesSelecionados.map(id => ({ id })),
      suspeitaClinica: form.suspeitaClinica
    });
    
    alert(`Solicitação criada! Protocolo: ${novaSolicitacao.protocolo}`);
    goto(`/veterinario/solicitacoes/${novaSolicitacao.id}`);
  } catch (error) {
    alert(`Erro: ${error.message}`);
  } finally {
    loading = false;
  }
}

onMount(carregarDados);
</script>
```

## Exemplo 5: Patologista Enviando Laudo

```svelte
<script>
import { solicitacaoService } from '$lib/mocks/services';

let { id } = $props(); // ID da solicitação
let observacoes = $state('');
let arquivo = $state(null);
let loading = $state(false);

async function iniciarAnalise() {
  loading = true;
  try {
    await solicitacaoService.atualizarStatus(id, 'EM_ANALISE');
    alert('Análise iniciada!');
  } catch (error) {
    alert(`Erro: ${error.message}`);
  } finally {
    loading = false;
  }
}

async function enviarLaudo() {
  loading = true;
  try {
    await solicitacaoService.enviarResultado(id, observacoes, arquivo);
    alert('Laudo enviado com sucesso!');
  } catch (error) {
    alert(`Erro: ${error.message}`);
  } finally {
    loading = false;
  }
}
</script>

<button onclick={iniciarAnalise} disabled={loading}>
  Iniciar Análise
</button>

<textarea bind:value={observacoes} placeholder="Observações do laudo..."></textarea>

<input type="file" accept=".pdf" onchange={(e) => arquivo = e.target.files[0]} />

<button onclick={enviarLaudo} disabled={loading || !observacoes}>
  Enviar Laudo
</button>
```

## Exemplo 6: Usar Store de Autenticação

```svelte
<script>
import { authStore } from '$lib/stores/auth';

// Reatividade automática
let usuario = $derived($authStore.usuario);
let isVeterinario = $derived($authStore.usuario?.papel === 'VETERINARIO');

function fazerLogout() {
  authStore.logout();
  goto('/');
}
</script>

{#if usuario}
  <p>Bem-vindo, {usuario.nome}</p>
  <p>Papel: {usuario.papel}</p>
  <button onclick={fazerLogout}>Sair</button>
{/if}
```

