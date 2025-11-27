# Sistema de Mocks - VitVet Frontend

Sistema completo de mocks que simula a API REST do backend.

## 📁 Estrutura

```
lib/mocks/
├── data.js                    # Base de dados mockada
├── mockHelpers.js             # Funções auxiliares
├── services/                  # Serviços que simulam endpoints
│   ├── authService.js
│   ├── solicitacaoService.js
│   ├── animalService.js
│   ├── tutorService.js
│   ├── tipoExameService.js
│   └── index.js
└── README.md
```

## 🚀 Como Usar

### Exemplo: Login

```javascript
import { authService } from '$lib/mocks/services';

try {
  const response = await authService.login('vet@vitvet.com', '123');
  console.log('Token:', response.token);
} catch (error) {
  console.error('Erro no login:', error.message);
}
```

### Exemplo: Criar Solicitação

```javascript
import { solicitacaoService } from '$lib/mocks/services';

const novaSolicitacao = await solicitacaoService.criarSolicitacao({
  suspeitaClinica: "Tosse persistente",
  animal: { id: 1 },
  veterinarioSolicitante: { id: 1 },
  exames: [
    { id: 1 }, // Hemograma
    { id: 3 }  // Raio-X
  ]
});

console.log('Protocolo gerado:', novaSolicitacao.protocolo);
```

### Exemplo: Listar com Filtros

```javascript
import { solicitacaoService } from '$lib/mocks/services';

// Filtrar por status
const recebidas = await solicitacaoService.listarSolicitacoes({ 
  status: 'RECEBIDO' 
});

// Filtrar por nome do animal
const doPipoca = await solicitacaoService.listarSolicitacoes({ 
  animal: 'Pipoca' 
});

// Combinado
const resultado = await solicitacaoService.listarSolicitacoes({ 
  status: 'RECEBIDO',
  animal: 'Rex'
});
```

## ⚙️ Características

### ✅ Simula Latência de Rede
Todos os serviços têm delay de 200-1200ms para simular requisições reais.

### ✅ Erros Realistas
Lança erros HTTP como no backend real:
- `UnauthorizedError` (401)
- `NotFoundError` (404)
- `ValidationError` (400)

### ✅ Expansão de Referências
Objetos relacionados são expandidos automaticamente (como JPA):

```javascript
// Retorna objeto completo com referências expandidas
{
  id: 1,
  animal: {
    id: 1,
    nome: "Pipoca",
    tutor: {
      id: 1,
      nomeCompleto: "Ana Silva",
      ...
    }
  },
  veterinarioSolicitante: {
    id: 1,
    nome: "Dr. Veterinário",
    ...
  }
}
```

### ✅ Persistência Simulada
Dados ficam salvos em memória durante a sessão. Ao recarregar a página, os dados voltam ao estado inicial.

## 🔐 Usuários Pré-cadastrados

```javascript
// Veterinário
email: "vet@vitvet.com"
senha: "123"

// Patologista
email: "pato@vitvet.com"
senha: "123"
```

## 📊 Dados Disponíveis

- **2 Usuários** (1 vet, 1 pato)
- **2 Tutores**
- **3 Animais**
- **5 Tipos de Exames**
- **3 Solicitações** (RECEBIDO, EM_ANALISE, CONCLUIDO)

## 🔄 Migração para API Real

Quando o backend estiver pronto, basta:

1. Criar `lib/api/` com os mesmos serviços
2. Trocar imports de `$lib/mocks/services` para `$lib/api/services`
3. Usar Axios/Fetch com a URL real

**Não precisa mudar nenhuma chamada no código!**

## 🎯 Status Disponíveis

- `RECEBIDO` - Nova solicitação
- `EM_ANALISE` - Patologista analisando
- `CONCLUIDO` - Laudo enviado
- `CANCELADO` - Cancelada

