INSERT INTO usuarios (id, nome, email, senha, crmv, especialidade, papel, data_criacao, data_atualizacao)
VALUES (1, 'Dr. Veterinário Exemplo', 'vet@vitvet.com', '$2a$10$fP8.KIa.X.53.iNl2Sg6mOrz17S.D.qia.dG.NN/J5P.OMjPjQv.S', 'CRMV-SP-11111', 'Clínico Geral', 'VETERINARIO', NOW(), NOW());

INSERT INTO usuarios (id, nome, email, senha, crmv, especialidade, papel, data_criacao, data_atualizacao)
VALUES (2, 'Dr. Patologista Exemplo', 'pato@vitvet.com', '$2a$10$fP8.KIa.X.53.iNl2Sg6mOrz17S.D.qia.dG.NN/J5P.OMjPjQv.S', 'CRMV-SP-22222', 'Patologia Clínica', 'PATOLOGISTA', NOW(), NOW());

INSERT INTO tutores (id, nome_completo, cpf, email, telefone)
VALUES (1, 'Ana Silva', '11122233300', 'ana.silva@email.com', '11999998888');

INSERT INTO animais (id, nome, especie, raca, sexo, data_nascimento, tutor_id)
VALUES (1, 'Pipoca', 'Cachorro', 'SRD', 'MACHO', '2022-01-15', 1);

INSERT INTO tipos_exames (id, nome, descricao) VALUES (1, 'Hemograma Completo', 'Análise de células sanguíneas.');
INSERT INTO tipos_exames (id, nome, descricao) VALUES (2, 'Exame de Urina - Tipo 1', 'Análise física, química e microscópica da urina.');
INSERT INTO tipos_exames (id, nome, descricao) VALUES (3, 'Raio-X (Tórax)', 'Imagem radiográfica do tórax, duas projeções.');

INSERT INTO solicitacoes_exames (id, protocolo, suspeita_clinica, status, data_solicitacao, animal_id, veterinario_id)
VALUES (1, '2025-A001', 'Tosse persistente e apatia', 'RECEBIDO', NOW(), 1, 1);

INSERT INTO solicitacao_tipo_exame (solicitacao_id, tipo_exame_id) VALUES (1, 1);
INSERT INTO solicitacao_tipo_exame (solicitacao_id, tipo_exame_id) VALUES (1, 3);