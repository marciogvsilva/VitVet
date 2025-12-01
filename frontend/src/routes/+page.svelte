<script>
import { goto } from '$app/navigation';
import { authService } from '$lib/services';
import { authStore } from '$lib/stores/auth';
import { toastStore } from '$lib/stores/toast';
import ToastContainer from '$lib/components/ToastContainer.svelte';

let userType = "patologista";
let email = "";
let senha = "";
let loading = false;
let erro = "";

// Preenche credenciais baseado no tipo selecionado
function selectUserType(type) {
  userType = type;
  if (type === 'patologista') {
    email = 'juliana.santos@vitvet.com';
    senha = '123';
  } else {
    email = 'rafael.mendes@vitvet.com';
    senha = '123';
  }
}

async function handleLogin(event) {
  event.preventDefault();
  loading = true;
  erro = "";
  
  try {
    // Chama o serviço de login (Mock ou API Real, dependendo do .env)
    const response = await authService.login(email, senha);
    
    // Para compatibilidade com mock e API real
    let token, usuario;
    
    if (response.token && response.usuario) {
      // Formato da API real
      token = response.token;
      usuario = response.usuario;
    } else if (response.token) {
      // Formato do mock (precisa buscar usuário)
      token = response.token;
      usuario = await authService.getUsuarioLogado?.(token) || { 
        email, 
        papel: userType.toUpperCase(),
        nome: email.split('@')[0]
      };
    }
    
    // Salva no store (persiste no localStorage)
    authStore.login(token, usuario);
    
    // Mostra toast de sucesso
    toastStore.success(`Bem-vindo, ${usuario.nome || usuario.email}!`);
    
    // Redireciona baseado no papel do usuário
    if (usuario.papel === 'PATOLOGISTA') {
      goto('/patologista/home');
    } else if (usuario.papel === 'VETERINARIO') {
      goto('/veterinario/home');
    } else {
      // Fallback se o papel não for reconhecido
      goto('/veterinario/home');
    }
  } catch (error) {
    erro = error.message || "Erro ao fazer login. Verifique suas credenciais.";
    toastStore.error(erro);
    console.error('Erro no login:', error);
  } finally {
    loading = false;
  }
}

// Inicializa com credenciais do patologista
selectUserType('patologista');
</script>

<div class="flex min-h-screen items-center justify-center bg-background-light font-sans px-4 md:px-6 lg:px-0">
	<div class="w-full max-w-[calc(100%-2rem)] md:max-w-md space-y-0 rounded-xl bg-white shadow-lg login-card">
		<!-- Seletor de tipo de usuário -->
		<div class="flex w-full overflow-hidden rounded-t-xl">
			<button 
				class="flex-1 py-3 px-3 md:py-4 md:px-4 text-center text-sm md:text-base font-medium transition-colors {userType === 'patologista' ? 'bg-primary-blue text-white' : 'bg-secondary-blue text-gray-700 hover:bg-secondary-blue/80'}" 
				on:click={() => selectUserType('patologista')}
			>
				Patologista
			</button>
			<button 
				class="flex-1 py-3 px-3 md:py-4 md:px-4 text-center text-sm md:text-base font-medium transition-colors {userType === 'veterinario' ? 'bg-primary-green text-white' : 'bg-secondary-green text-gray-700 hover:bg-secondary-green/80'}" 
				on:click={() => selectUserType('veterinario')}
			>
				Veterinário
			</button>
		</div>
		
		<!-- Formulário de login -->
		<div class="p-6 md:p-8 lg:p-10 space-y-6 md:space-y-8">
			<div>
				<h2 class="text-center text-2xl md:text-3xl font-bold tracking-tight text-gray-900">Exames - VitVet</h2>
				<p class="mt-2 text-center text-sm text-gray-600">Faça login na sua conta</p>
			</div>
			<form class="mt-6 md:mt-8 space-y-5 md:space-y-6" on:submit={handleLogin}>
				<div class="space-y-4 rounded-md">
					<div>
						<label for="email-address" class="sr-only">Email</label>
						<input
							id="email-address"
							name="email"
							type="email"
							autocomplete="email"
							required
							bind:value={email}
							disabled={loading}
							class="form-input block w-full rounded-md border-gray-300 py-2.5 md:py-3 placeholder-gray-500 shadow-sm text-sm md:text-base {userType === 'patologista' ? 'focus:border-primary-blue focus:ring-primary-blue' : 'focus:border-primary-green focus:ring-primary-green'} disabled:opacity-50 disabled:cursor-not-allowed"
							placeholder="Endereço de e-mail"
						/>
					</div>
					<div>
						<label for="password" class="sr-only">Senha</label>
						<input
							id="password"
							name="password"
							type="password"
							autocomplete="current-password"
							required
							bind:value={senha}
							disabled={loading}
							class="form-input block w-full rounded-md border-gray-300 py-2.5 md:py-3 placeholder-gray-500 shadow-sm text-sm md:text-base {userType === 'patologista' ? 'focus:border-primary-blue focus:ring-primary-blue' : 'focus:border-primary-green focus:ring-primary-green'} disabled:opacity-50 disabled:cursor-not-allowed"
							placeholder="Senha"
						/>
					</div>
				</div>

				{#if erro}
					<div class="rounded-md bg-red-50 p-3">
						<p class="text-sm text-red-800">{erro}</p>
					</div>
				{/if}

				<div class="flex flex-col gap-3 md:flex-row md:items-center md:justify-between md:gap-0">
					<div class="flex items-center">
						<input
							id="remember-me"
							name="remember-me"
							type="checkbox"
							class="form-checkbox h-4 w-4 rounded border-gray-300 {userType === 'patologista' ? 'text-primary-blue focus:ring-primary-blue' : 'text-primary-green focus:ring-primary-green'}"
						/>
						<label for="remember-me" class="ml-2 block text-sm text-gray-900">Lembrar-me</label>
					</div>

					<div class="text-sm">
						<a href="/" class="font-medium {userType === 'patologista' ? 'text-primary-blue hover:text-blue-700' : 'text-primary-green hover:text-green-700'}">
							Esqueceu sua senha?
						</a>
					</div>
				</div>

				<div>
					<button
						type="submit"
						disabled={loading}
						class="group relative flex w-full justify-center rounded-full border border-transparent px-4 py-2.5 md:py-3 text-sm font-semibold text-white shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 {userType === 'patologista' ? 'bg-primary-blue hover:bg-blue-800 focus:ring-primary-blue' : 'bg-primary-green hover:bg-green-700 focus:ring-primary-green'} disabled:opacity-50 disabled:cursor-not-allowed"
					>
						{loading ? 'Entrando...' : 'Entrar'}
					</button>
				</div>
			</form>
		</div>
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
  --color-primary-green: #5DB578;
  --color-secondary-green: #BBDCC5;
}

/* Aplicando as cores ao estilo geral */
.bg-background-light {
  background-color: var(--color-background-light);
}

.bg-primary-blue {
  background-color: var(--color-primary-blue);
}

.bg-secondary-blue {
  background-color: var(--color-secondary-blue);
}

.bg-primary-green {
  background-color: var(--color-primary-green);
}

.bg-secondary-green {
  background-color: var(--color-secondary-green);
}

.text-primary-blue {
  color: var(--color-primary-blue);
}

.text-primary-green {
  color: var(--color-primary-green);
}

.hover\:bg-blue-800:hover {
  background-color: #165070; /* Uma tonalidade mais escura do azul primário */
}

.hover\:bg-green-700:hover {
  background-color: #4a9163; /* Uma tonalidade mais escura do verde primário */
}

.text-gray-900 {
  color: #1a202c;
}

.text-gray-600 {
  color: #4b5563;
}

/* ========================================
   RESPONSIVIDADE - MEDIA QUERIES
   ======================================== */

/* Mobile (< 768px) - Ajustes específicos */
@media (max-width: 767px) {
  .login-card {
    margin: 1rem 0;
  }
  
  /* Garante que inputs sejam legíveis sem zoom */
  input[type="email"],
  input[type="password"] {
    font-size: 16px; /* Previne zoom no iOS */
  }
  
  /* Ajusta espaçamento do formulário */
  form {
    margin-top: 1.5rem !important;
  }
}

/* Tablet (768px - 1024px) - Ajustes intermediários */
@media (min-width: 768px) and (max-width: 1024px) {
  .login-card {
    max-width: 28rem;
  }
}

/* Desktop (> 1024px) - Mantém estilos originais */
@media (min-width: 1025px) {
  .login-card {
    max-width: 28rem;
  }
}

/* Ajuste para telas muito pequenas (< 360px) */
@media (max-width: 359px) {
  .login-card {
    border-radius: 0.75rem;
  }
  
  h2 {
    font-size: 1.25rem !important;
  }
}

/* Ajuste para telas em landscape no mobile */
@media (max-height: 600px) and (orientation: landscape) {
  .login-card {
    margin: 0.5rem 0;
  }
  
  /* Reduz padding em landscape */
  .login-card > div:last-child {
    padding-top: 1rem !important;
    padding-bottom: 1rem !important;
  }
}
</style>
