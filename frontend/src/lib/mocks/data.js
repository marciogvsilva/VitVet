// Base de dados mockada - Simula o backend
// Dados iniciais baseados no data.sql do backend

export const mockData = {
  usuarios: [
    {
      id: 1,
      nome: "Dr. Rafael Mendes",
      email: "rafael.mendes@vitvet.com",
      senha: "123", // Em produção isso viria hasheado
      crmv: "CRMV-SP-18452",
      especialidade: "Clínico Geral",
      papel: "VETERINARIO",
      dataCriacao: "2025-11-27T05:36:21.000Z",
      dataAtualizacao: "2025-11-27T05:36:21.000Z"
    },
    {
      id: 2,
      nome: "Dra. Juliana Santos",
      email: "juliana.santos@vitvet.com",
      senha: "123",
      crmv: "CRMV-SP-19234",
      especialidade: "Patologia Clínica",
      papel: "PATOLOGISTA",
      dataCriacao: "2025-11-27T05:36:21.000Z",
      dataAtualizacao: "2025-11-27T05:36:21.000Z"
    }
  ],

  tutores: [
    {
      id: 1,
      nomeCompleto: "Ana Paula Silva",
      cpf: "12345678901",
      email: "ana.silva@gmail.com",
      telefone: "11987654321"
    },
    {
      id: 2,
      nomeCompleto: "Carlos Eduardo Oliveira",
      cpf: "98765432100",
      email: "carlos.oliveira@hotmail.com",
      telefone: "11976543210"
    },
    {
      id: 3,
      nomeCompleto: "Mariana Costa",
      cpf: "45678912345",
      email: "mariana.costa@outlook.com",
      telefone: "11965432109"
    },
    {
      id: 4,
      nomeCompleto: "Roberto Almeida",
      cpf: "78912345678",
      email: "roberto.almeida@gmail.com",
      telefone: "11954321098"
    },
    {
      id: 5,
      nomeCompleto: "Fernanda Rodrigues",
      cpf: "32165498732",
      email: "fernanda.rodrigues@yahoo.com",
      telefone: "11943210987"
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
      nome: "Luna",
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
    },
    {
      id: 4,
      nome: "Mel",
      rgAnimal: "RG004",
      especie: "Cachorro",
      raca: "Golden Retriever",
      sexo: "FEMEA",
      dataNascimento: "2023-03-12",
      tutor: { id: 3 }
    },
    {
      id: 5,
      nome: "Bolinha",
      rgAnimal: "RG005",
      especie: "Gato",
      raca: "Persa",
      sexo: "MACHO",
      dataNascimento: "2019-11-08",
      tutor: { id: 4 }
    },
    {
      id: 6,
      nome: "Max",
      rgAnimal: "RG006",
      especie: "Cachorro",
      raca: "Bulldog Francês",
      sexo: "MACHO",
      dataNascimento: "2022-07-25",
      tutor: { id: 5 }
    }
  ],

  tiposExames: [
    // ========== COMBOS ==========
    {
      id: 1,
      nome: "Anemia",
      descricao: "Hemograma completo + Contagem de Reticulócitos",
      categoria: "COMBOS",
      isCombo: true,
      examesIncluidos: [10, 11] // Hemograma completo, Contagem de Reticulócitos
    },
    {
      id: 2,
      nome: "Check Up 01",
      descricao: "Hemograma completo + Creatina + ALT/TGP",
      categoria: "COMBOS",
      isCombo: true,
      examesIncluidos: [10, 25, 19] // Hemograma (10), Creatina (25), ALT/TGP (19)
    },
    {
      id: 3,
      nome: "Check Up 02",
      descricao: "Hemograma completo + Creatina + ALT/TGP + FA + Ureia",
      categoria: "COMBOS",
      isCombo: true,
      examesIncluidos: [10, 25, 19, 26, 34] // Hemograma (10), Creatina (25), ALT/TGP (19), FA (26), Ureia (34)
    },
    {
      id: 4,
      nome: "Pré-operatório 01",
      descricao: "Hemograma completo + Creatina + ALT/TGP + FA + Ureia + Glicose + Albumina + Proteína total",
      categoria: "COMBOS",
      isCombo: true,
      examesIncluidos: [10, 25, 19, 26, 34, 29, 18, 31] // Hemograma (10), Creatina (25), ALT/TGP (19), FA (26), Ureia (34), Glicose (29), Albumina (18), Proteína total (31)
    },
    {
      id: 5,
      nome: "Pré-operatório 02",
      descricao: "Hemograma completo + Creatina + ALT/TGP + FA + Ureia + Glicose + Albumina + Urinálise",
      categoria: "COMBOS",
      isCombo: true,
      examesIncluidos: [10, 25, 19, 26, 34, 29, 18, 60] // Hemograma (10), Creatina (25), ALT/TGP (19), FA (26), Ureia (34), Glicose (29), Albumina (18), Urinálise (60)
    },
    {
      id: 6,
      nome: "Renal 01",
      descricao: "Hemograma completo + Creatina + Ureia + Urinálise",
      categoria: "COMBOS",
      isCombo: true,
      examesIncluidos: [10, 25, 34, 60] // Hemograma (10), Creatina (25), Ureia (34), Urinálise (60)
    },
    {
      id: 7,
      nome: "Renal 02",
      descricao: "Hemograma completo + Creatina + Ureia + Urinálise + UP/C",
      categoria: "COMBOS",
      isCombo: true,
      examesIncluidos: [10, 25, 34, 60, 61] // Hemograma (10), Creatina (25), Ureia (34), Urinálise (60), UP/C (61)
    },
    {
      id: 8,
      nome: "Hepático",
      descricao: "Hemograma completo + ALT/TGP + FA + Albumina + Proteína total",
      categoria: "COMBOS",
      isCombo: true,
      examesIncluidos: [10, 19, 26, 18, 31] // Hemograma (10), ALT/TGP (19), FA (26), Albumina (18), Proteína total (31)
    },
    
    // ========== HEMATOLOGIA ==========
    {
      id: 10,
      nome: "Hemograma completo",
      descricao: "Análise completa de células sanguíneas (eritrócitos, leucócitos, plaquetas)",
      categoria: "HEMATOLOGIA",
      isCombo: false
    },
    {
      id: 11,
      nome: "Contagem de Reticulócitos",
      descricao: "Quantificação de reticulócitos no sangue periférico",
      categoria: "HEMATOLOGIA",
      isCombo: false
    },
    {
      id: 12,
      nome: "Pesquisa direta de hemoparasitas",
      descricao: "Identificação de hemoparasitas em esfregaço sanguíneo",
      categoria: "HEMATOLOGIA",
      isCombo: false
    },
    {
      id: 13,
      nome: "Contagem de plaquetas",
      descricao: "Quantificação de plaquetas no sangue",
      categoria: "HEMATOLOGIA",
      isCombo: false
    },
    {
      id: 14,
      nome: "Revisão de Lâmina",
      descricao: "Análise microscópica detalhada de lâmina de sangue",
      categoria: "HEMATOLOGIA",
      isCombo: false
    },
    {
      id: 15,
      nome: "Teste de Aglutinação em solução salina",
      descricao: "Teste para detecção de anticorpos anti-eritrocitários",
      categoria: "HEMATOLOGIA",
      isCombo: false
    },
    {
      id: 16,
      nome: "TTP e TTPA",
      descricao: "Tempo de Tromboplastina Parcial e Tempo de Tromboplastina Ativada",
      categoria: "HEMATOLOGIA",
      isCombo: false
    },
    
    // ========== BIOQUÍMICA ==========
    {
      id: 18,
      nome: "Albumina",
      descricao: "Dosagem de albumina sérica",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 19,
      nome: "ALT/TGP",
      descricao: "Alanina Aminotransferase / Transaminase Glutâmico-Pirúvica",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 20,
      nome: "AST/TGO",
      descricao: "Aspartato Aminotransferase / Transaminase Glutâmico-Oxalacética",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 21,
      nome: "Bilirrubinas (total e frações)",
      descricao: "Dosagem de bilirrubina total, direta e indireta",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 22,
      nome: "Cálcio",
      descricao: "Dosagem de cálcio sérico",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 23,
      nome: "Colesterol",
      descricao: "Dosagem de colesterol total",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 24,
      nome: "Cloro",
      descricao: "Dosagem de cloro sérico",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 25,
      nome: "Creatina",
      descricao: "Dosagem de creatinina sérica",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 26,
      nome: "Fosfatase Alcalina",
      descricao: "Dosagem de fosfatase alcalina (FA)",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 27,
      nome: "Fósforo",
      descricao: "Dosagem de fósforo sérico",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 28,
      nome: "Gama GT",
      descricao: "Gama Glutamil Transferase",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 29,
      nome: "Glicose (tubo fluoreto)",
      descricao: "Dosagem de glicose sérica",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 30,
      nome: "Potássio",
      descricao: "Dosagem de potássio sérico",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 31,
      nome: "Proteína total",
      descricao: "Dosagem de proteínas totais séricas",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 32,
      nome: "Sódio",
      descricao: "Dosagem de sódio sérico",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 33,
      nome: "Triglicérides",
      descricao: "Dosagem de triglicérides séricos",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 34,
      nome: "Ureia",
      descricao: "Dosagem de ureia sérica",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    {
      id: 35,
      nome: "Outros",
      descricao: "Outros exames bioquímicos (especificar nos comentários)",
      categoria: "BIOQUIMICA",
      isCombo: false
    },
    
    // ========== DERMATOLOGIA ==========
    {
      id: 40,
      nome: "Auricular (Orelha Esquerda)",
      descricao: "Citologia de secreção auricular - orelha esquerda",
      categoria: "DERMATOLOGIA",
      isCombo: false
    },
    {
      id: 41,
      nome: "Auricular (Orelha Direita)",
      descricao: "Citologia de secreção auricular - orelha direita",
      categoria: "DERMATOLOGIA",
      isCombo: false
    },
    {
      id: 42,
      nome: "Raspado de pele",
      descricao: "Análise microscópica de raspado cutâneo",
      categoria: "DERMATOLOGIA",
      isCombo: false
    },
    {
      id: 43,
      nome: "Imprint",
      descricao: "Citologia por impressão de lesão cutânea",
      categoria: "DERMATOLOGIA",
      isCombo: false
    },
    {
      id: 44,
      nome: "Citologia vaginal",
      descricao: "Análise citológica de secreção vaginal",
      categoria: "DERMATOLOGIA",
      isCombo: false
    },
    
    // ========== TESTES RÁPIDOS ==========
    {
      id: 50,
      nome: "Erliquiose Ac",
      descricao: "Teste rápido para anticorpos anti-Erlichia (Sangue total, plasma ou soro)",
      categoria: "TESTES_RAPIDOS",
      isCombo: false
    },
    {
      id: 51,
      nome: "Cinomose Ag",
      descricao: "Teste rápido para antígeno de Cinomose (Swab de secreção ocular ou nasal)",
      categoria: "TESTES_RAPIDOS",
      isCombo: false
    },
    {
      id: 52,
      nome: "Parvovirose Ag",
      descricao: "Teste rápido para antígeno de Parvovirose (Swab de fezes coletadas da ampola retal)",
      categoria: "TESTES_RAPIDOS",
      isCombo: false
    },
    {
      id: 53,
      nome: "Fiv Ac / Felv Ag",
      descricao: "Teste rápido para FIV (anticorpos) e FeLV (antígeno) (Sangue total, plasma ou soro)",
      categoria: "TESTES_RAPIDOS",
      isCombo: false
    },
    {
      id: 54,
      nome: "Leishmaniose Ac",
      descricao: "Teste rápido para anticorpos anti-Leishmania (Sangue total, plasma ou soro)",
      categoria: "TESTES_RAPIDOS",
      isCombo: false
    },
    
    // ========== URINA ==========
    {
      id: 60,
      nome: "Urinálise",
      descricao: "Análise física, química e microscópica da urina",
      categoria: "URINA",
      isCombo: false
    },
    {
      id: 61,
      nome: "Razão Proteína/Creatinina (UP/C)",
      descricao: "Razão proteína/creatinina urinária para avaliação de proteinúria",
      categoria: "URINA",
      isCombo: false
    },
    {
      id: 62,
      nome: "Urocultura",
      descricao: "Cultura bacteriana de urina com antibiograma",
      categoria: "URINA",
      isCombo: false
    },
    {
      id: 63,
      nome: "Citologia de sedimento urinário",
      descricao: "Análise citológica do sedimento urinário",
      categoria: "URINA",
      isCombo: false
    },
    
    // ========== FEZES ==========
    {
      id: 70,
      nome: "Coproparasitológico (1 amostra)",
      descricao: "Pesquisa de parasitas em amostra única de fezes",
      categoria: "FEZES",
      isCombo: false
    },
    {
      id: 71,
      nome: "Coproparasitológico seriado (3 amostras)",
      descricao: "Pesquisa de parasitas em 3 amostras seriadas de fezes",
      categoria: "FEZES",
      isCombo: false
    },
    {
      id: 72,
      nome: "Pesquisa de sangue oculto",
      descricao: "Teste para detecção de sangue oculto nas fezes",
      categoria: "FEZES",
      isCombo: false
    },
    {
      id: 73,
      nome: "Tripsina fecal",
      descricao: "Dosagem de tripsina nas fezes",
      categoria: "FEZES",
      isCombo: false
    },
    {
      id: 74,
      nome: "Análise de líquor",
      descricao: "Análise do líquido cefalorraquidiano",
      categoria: "FEZES",
      isCombo: false
    },
    {
      id: 75,
      nome: "Análise de líquido sinovial",
      descricao: "Análise do líquido sinovial articular",
      categoria: "FEZES",
      isCombo: false
    },
    {
      id: 76,
      nome: "Análise de líquidos (efusões)",
      descricao: "Análise de líquidos de efusões (pleural, peritoneal, etc.)",
      categoria: "FEZES",
      isCombo: false
    },
    
    // ========== MEDULA ÓSSEA ==========
    {
      id: 80,
      nome: "Coleta do material",
      descricao: "Coleta de material de medula óssea",
      categoria: "MEDULA_OSSEA",
      isCombo: false
    },
    {
      id: 81,
      nome: "Mielograma",
      descricao: "Análise citológica da medula óssea",
      categoria: "MEDULA_OSSEA",
      isCombo: false
    },
    {
      id: 82,
      nome: "PCR",
      descricao: "Reação em Cadeia da Polimerase (informar quais antígenos nos comentários)",
      categoria: "MEDULA_OSSEA",
      isCombo: false
    }
  ],

  solicitacoes: [
    {
      id: 1,
      protocolo: "2025-A001",
      suspeitaClinica: "Paciente canino, macho, 3 anos, SRD. Apresenta tosse seca persistente há 15 dias, associada a apatia e redução do apetite. Ao exame físico: mucosas levemente pálidas, ausculta pulmonar com crepitações bilaterais. Temperatura retal: 39,2°C. Suspeita de processo inflamatório/infeccioso respiratório. Solicitado hemograma completo e radiografia de tórax para avaliação.",
      status: "RECEBIDO",
      dataSolicitacao: "2025-11-27T08:15:00.000Z",
      animal: { id: 1 },
      veterinarioSolicitante: { id: 1 },
      exames: [
        { id: 10 }, // Hemograma completo
        { id: 12 } // Pesquisa direta de hemoparasitas
      ]
    },
    {
      id: 2,
      protocolo: "2025-A002",
      suspeitaClinica: "Paciente felino, fêmea, 4 anos, Siamês. História de vômitos intermitentes há 1 semana, associados a letargia. Vômitos contendo bile e restos alimentares. Ao exame: desidratação leve, abdômen tenso à palpação. Suspeita de gastrite ou corpo estranho. Solicitado exame de urina e ultrassom abdominal para investigação.",
      status: "EM_ANALISE",
      dataSolicitacao: "2025-11-26T14:30:00.000Z",
      animal: { id: 2 },
      veterinarioSolicitante: { id: 1 },
      exames: [
        { id: 60 }, // Urinálise
        { id: 19 }, // ALT/TGP
        { id: 26 } // Fosfatase Alcalina
      ]
    },
    {
      id: 3,
      protocolo: "2025-A003",
      suspeitaClinica: "Paciente canino, macho, 5 anos, Labrador. Apresenta claudicação de membro posterior direito há 3 semanas, com piora progressiva. Ao exame ortopédico: dor à palpação da articulação do joelho, crepitação articular. Suspeita de luxação patelar ou lesão ligamentar. Solicitado radiografia de tórax (duas projeções) para avaliação.",
      status: "CONCLUIDO",
      dataSolicitacao: "2025-11-25T09:20:00.000Z",
      animal: { id: 3 },
      veterinarioSolicitante: { id: 1 },
      exames: [
        { id: 75 } // Análise de líquido sinovial
      ],
      resultado: {
        id: 3,
        observacoes: "Radiografia de tórax em duas projeções (DV e lateral) realizada. Observa-se leve deslocamento da patela em projeção lateral, compatível com luxação patelar grau II. Não há evidências de fraturas ou lesões ósseas. Recomenda-se avaliação ortopédica especializada e possível correção cirúrgica.",
        urlLaudoPdf: "/mock/laudos/laudo-003.pdf",
        dataLaudo: "2025-11-25T16:45:00.000Z",
        patologistaResponsavel: { id: 2 }
      }
    },
    {
      id: 4,
      protocolo: "2025-A004",
      suspeitaClinica: "Paciente canino, fêmea, 2 anos, Golden Retriever. Aumento de volume abdominal progressivo há 1 mês, associado a polidipsia e poliúria. Ao exame: distensão abdominal moderada, sopro cardíaco leve. Suspeita de ascite ou massa abdominal. Solicitado hemograma completo, bioquímica renal e ultrassom abdominal.",
      status: "RECEBIDO",
      dataSolicitacao: "2025-11-27T10:45:00.000Z",
      animal: { id: 4 },
      veterinarioSolicitante: { id: 1 },
      exames: [
        { id: 10 }, // Hemograma completo
        { id: 6 }  // Renal 01 (combo: Hemograma + Creatina + Ureia + Urinálise)
      ]
    },
    {
      id: 5,
      protocolo: "2025-A005",
      suspeitaClinica: "Paciente felino, macho, 6 anos, Persa. História de dificuldade urinária, micção frequente com pequeno volume, presença de sangue na urina. Ao exame: bexiga distendida, sensível à palpação. Suspeita de cistite ou urolitíase. Solicitado exame de urina tipo 1 e bioquímica renal.",
      status: "EM_ANALISE",
      dataSolicitacao: "2025-11-26T11:20:00.000Z",
      animal: { id: 5 },
      veterinarioSolicitante: { id: 1 },
      exames: [
        { id: 60 }, // Urinálise
        { id: 7 }   // Renal 02 (combo: Hemograma + Creatina + Ureia + Urinálise + UP/C)
      ]
    },
    {
      id: 6,
      protocolo: "2025-A006",
      suspeitaClinica: "Paciente canino, macho, 2 anos, Bulldog Francês. Apresenta dispneia e tosse após exercício, associada a cianose de mucosas. Ao exame: estertores pulmonares, taquicardia. Suspeita de síndrome braquicefálica ou processo respiratório. Solicitado hemograma completo e radiografia de tórax.",
      status: "CONCLUIDO",
      dataSolicitacao: "2025-11-24T13:10:00.000Z",
      animal: { id: 6 },
      veterinarioSolicitante: { id: 1 },
      exames: [
        { id: 10 }, // Hemograma completo
        { id: 14 }  // Revisão de Lâmina
      ],
      resultado: {
        id: 6,
        observacoes: "Hemograma completo: leve leucocitose (15.200/μL) com neutrofilia. Radiografia de tórax: traqueia colapsada em região cervical, compatível com síndrome braquicefálica. Pulmões sem alterações significativas. Recomenda-se manejo conservador, evitar exercícios em dias quentes e considerar correção cirúrgica se sintomas persistirem.",
        urlLaudoPdf: "/mock/laudos/laudo-006.pdf",
        dataLaudo: "2025-11-24T18:30:00.000Z",
        patologistaResponsavel: { id: 2 }
      }
    }
  ],

  resultados: [
    {
      id: 3,
      observacoes: "Radiografia de tórax em duas projeções (DV e lateral) realizada. Observa-se leve deslocamento da patela em projeção lateral, compatível com luxação patelar grau II. Não há evidências de fraturas ou lesões ósseas. Recomenda-se avaliação ortopédica especializada e possível correção cirúrgica.",
      urlLaudoPdf: "/mock/laudos/laudo-003.pdf",
      dataLaudo: "2025-11-25T16:45:00.000Z",
      solicitacao: { id: 3 },
      patologistaResponsavel: { id: 2 }
    },
    {
      id: 6,
      observacoes: "Hemograma completo: leve leucocitose (15.200/μL) com neutrofilia. Radiografia de tórax: traqueia colapsada em região cervical, compatível com síndrome braquicefálica. Pulmões sem alterações significativas. Recomenda-se manejo conservador, evitar exercícios em dias quentes e considerar correção cirúrgica se sintomas persistirem.",
      urlLaudoPdf: "/mock/laudos/laudo-006.pdf",
      dataLaudo: "2025-11-24T18:30:00.000Z",
      solicitacao: { id: 6 },
      patologistaResponsavel: { id: 2 }
    }
  ],

  notificacoes: [
    {
      id: 1,
      titulo: "Nova Solicitação",
      mensagem: "Nova solicitação de exame recebida - Protocolo 2025-A001",
      lida: false,
      dataCriacao: "2025-11-27T08:15:00.000Z",
      destinatario: { id: 2 }
    },
    {
      id: 2,
      titulo: "Nova Solicitação",
      mensagem: "Nova solicitação de exame recebida - Protocolo 2025-A004",
      lida: false,
      dataCriacao: "2025-11-27T10:45:00.000Z",
      destinatario: { id: 2 }
    }
  ]
};

// Contadores para IDs auto-incrementais
export let nextId = {
  usuario: 3,
  tutor: 6,
  animal: 7,
  tipoExame: 83, // Último ID usado: 82 (PCR)
  solicitacao: 7,
  resultado: 7,
  notificacao: 3
};

