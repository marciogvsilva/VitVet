<script>
// Importação necessária para navegação programática
import { goto } from '$app/navigation';

// A lógica para a página de login ficará aqui.
let userType = "patologista"; // Valor padrão: patologista

function handleLogin() {
  // Aqui irá a lógica de login quando implementarmos o backend
  console.log('Tentativa de login como', userType);
  
  // Redireciona para a página apropriada com base no tipo de usuário
  if (userType === 'patologista') {
    goto('/patologista/home');
  } else {
    goto('/veterinario/home');
  }
}

function selectUserType(type) {
  userType = type;
}
</script>

<div class="flex min-h-screen items-center justify-center bg-background-light font-sans">
	<div class="w-full max-w-md space-y-0 rounded-xl bg-white shadow-lg">
		<!-- Seletor de tipo de usuário -->
		<div class="flex w-full overflow-hidden rounded-t-xl">
			<button 
				class="flex-1 py-4 px-4 text-center font-medium transition-colors {userType === 'patologista' ? 'bg-primary-blue text-white' : 'bg-secondary-blue text-gray-700 hover:bg-secondary-blue/80'}" 
				on:click={() => selectUserType('patologista')}
			>
				Patologista
			</button>
			<button 
				class="flex-1 py-4 px-4 text-center font-medium transition-colors {userType === 'veterinario' ? 'bg-primary-green text-white' : 'bg-secondary-green text-gray-700 hover:bg-secondary-green/80'}" 
				on:click={() => selectUserType('veterinario')}
			>
				Veterinário
			</button>
		</div>
		
		<!-- Formulário de login -->
		<div class="p-10 space-y-8">
			<div>
				<h2 class="text-center text-3xl font-bold tracking-tight text-gray-900">Exames - VitVet</h2>
				<p class="mt-2 text-center text-sm text-gray-600">Faça login na sua conta</p>
			</div>
			<form class="mt-8 space-y-6" action="#" method="POST">
				<div class="space-y-4 rounded-md">
					<div>
						<label for="email-address" class="sr-only">Email</label>
						<input
							id="email-address"
							name="email"
							type="email"
							autocomplete="email"
							required
							class="form-input block w-full rounded-md border-gray-300 py-3 placeholder-gray-500 shadow-sm {userType === 'patologista' ? 'focus:border-primary-blue focus:ring-primary-blue' : 'focus:border-primary-green focus:ring-primary-green'} sm:text-sm"
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
							class="form-input block w-full rounded-md border-gray-300 py-3 placeholder-gray-500 shadow-sm {userType === 'patologista' ? 'focus:border-primary-blue focus:ring-primary-blue' : 'focus:border-primary-green focus:ring-primary-green'} sm:text-sm"
							placeholder="Senha"
						/>
					</div>
				</div>

				<div class="flex items-center justify-between">
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
						class="group relative flex w-full justify-center rounded-full border border-transparent px-4 py-3 text-sm font-semibold text-white shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 {userType === 'patologista' ? 'bg-primary-blue hover:bg-blue-800 focus:ring-primary-blue' : 'bg-primary-green hover:bg-green-700 focus:ring-primary-green'}"
						on:click|preventDefault={handleLogin}
					>
						Entrar
					</button>
				</div>
			</form>
		</div>
	</div>
</div>

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
</style>
