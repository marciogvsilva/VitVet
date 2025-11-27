// Base de dados mockada - Simula o backend
// Dados iniciais baseados no data.sql do backend

export const mockData = {
  usuarios: [
    {
      id: 1,
      nome: "Dr. Veterinário Exemplo",
      email: "vet@vitvet.com",
      senha: "123", // Em produção isso viria hasheado
      crmv: "CRMV-SP-11111",
      especialidade: "Clínico Geral",
      papel: "VETERINARIO",
      dataCriacao: "2025-11-27T05:36:21.000Z",
      dataAtualizacao: "2025-11-27T05:36:21.000Z"
    },
    {
      id: 2,
      nome: "Dr. Patologista Exemplo",
      email: "pato@vitvet.com",
      senha: "123",
      crmv: "CRMV-SP-22222",
      especialidade: "Patologia Clínica",
      papel: "PATOLOGISTA",
      dataCriacao: "2025-11-27T05:36:21.000Z",
      dataAtualizacao: "2025-11-27T05:36:21.000Z"
    }
  ],

  tutores: [
    {
      id: 1,
      nomeCompleto: "Ana Silva",
      cpf: "11122233300",
      email: "ana.silva@email.com",
      telefone: "11999998888"
    },
    {
      id: 2,
      nomeCompleto: "Carlos Oliveira",
      cpf: "22233344400",
      email: "carlos@email.com",
      telefone: "11988887777"
    }
  ],

  animais: [
    {
      id: 1,
      nome: "Pipoca",
      rgAnimal: "RG001",
      especie: "Cachorro",
      raca: "SRD",
      sexo: "MACHO",
      dataNascimento: "2022-01-15",
      tutor: { id: 1 }
    },
    {
      id: 2,
      nome: "Miau",
      rgAnimal: "RG002",
      especie: "Gato",
      raca: "Siamês",
      sexo: "FEMEA",
      dataNascimento: "2021-05-10",
      tutor: { id: 1 }
    },
    {
      id: 3,
      nome: "Thor",
      rgAnimal: "RG003",
      especie: "Cachorro",
      raca: "Labrador",
      sexo: "MACHO",
      dataNascimento: "2020-08-20",
      tutor: { id: 2 }
    }
  ],

  tiposExames: [
    {
      id: 1,
      nome: "Hemograma Completo",
      descricao: "Análise de células sanguíneas."
    },
    {
      id: 2,
      nome: "Exame de Urina - Tipo 1",
      descricao: "Análise física, química e microscópica da urina."
    },
    {
      id: 3,
      nome: "Raio-X (Tórax)",
      descricao: "Imagem radiográfica do tórax, duas projeções."
    },
    {
      id: 4,
      nome: "Ultrassom Abdominal",
      descricao: "Ultrassonografia abdominal completa."
    },
    {
      id: 5,
      nome: "Bioquímica Renal",
      descricao: "Avaliação de função renal (ureia, creatinina)."
    }
  ],

  solicitacoes: [
    {
      id: 1,
      protocolo: "2025-A001",
      suspeitaClinica: "Tosse persistente e apatia",
      status: "RECEBIDO",
      dataSolicitacao: "2025-11-27T05:36:21.000Z",
      animal: { id: 1 },
      veterinarioSolicitante: { id: 1 },
      exames: [
        { id: 1 },
        { id: 3 }
      ]
    },
    {
      id: 2,
      protocolo: "2025-A002",
      suspeitaClinica: "Vômitos frequentes",
      status: "EM_ANALISE",
      dataSolicitacao: "2025-11-26T10:20:00.000Z",
      animal: { id: 2 },
      veterinarioSolicitante: { id: 1 },
      exames: [
        { id: 2 },
        { id: 4 }
      ]
    },
    {
      id: 3,
      protocolo: "2025-A003",
      suspeitaClinica: "Claudicação posterior direita",
      status: "CONCLUIDO",
      dataSolicitacao: "2025-11-25T14:30:00.000Z",
      animal: { id: 3 },
      veterinarioSolicitante: { id: 1 },
      exames: [
        { id: 3 }
      ],
      resultado: {
        id: 3,
        observacoes: "Raio-X indica possível luxação de patela grau II. Recomenda-se avaliação ortopédica.",
        urlLaudoPdf: "/mock/laudos/laudo-003.pdf",
        dataLaudo: "2025-11-26T16:45:00.000Z",
        patologistaResponsavel: { id: 2 }
      }
    }
  ],

  resultados: [
    {
      id: 3,
      observacoes: "Raio-X indica possível luxação de patela grau II. Recomenda-se avaliação ortopédica.",
      urlLaudoPdf: "/mock/laudos/laudo-003.pdf",
      dataLaudo: "2025-11-26T16:45:00.000Z",
      solicitacao: { id: 3 },
      patologistaResponsavel: { id: 2 }
    }
  ],

  notificacoes: [
    {
      id: 1,
      titulo: "Nova Solicitação",
      mensagem: "Nova solicitação de exame recebida - Protocolo 2025-A001",
      lida: false,
      dataCriacao: "2025-11-27T05:36:21.000Z",
      destinatario: { id: 2 }
    }
  ]
};

// Contadores para IDs auto-incrementais
export let nextId = {
  usuario: 3,
  tutor: 3,
  animal: 4,
  solicitacao: 4,
  resultado: 4,
  notificacao: 2
};

