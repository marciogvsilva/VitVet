# VitVet - Sistema de Requisição de Exames

## 📄 Sobre o Projeto
Este projeto visa a automatização da comunicação entre médicos veterinários e patologistas para a requisição de exames na Clínica VitVet. O sistema foi desenvolvido no contexto da disciplina **SSC536 - Projeto e Desenvolvimento de Sistemas de Informação** do ICMC - USP.

O principal objetivo é substituir o processo manual e manuscrito de solicitação de exames por uma plataforma web centralizada, garantindo maior agilidade, segurança, rastreabilidade e redução de erros.

## 🎯 Propósito e Escopo
O sistema permite que médicos veterinários solicitem exames de maneira estruturada e que patologistas recebam e gerenciem esses pedidos

**Funcionalidades Principais:**
* **Gestão de Usuários:** Cadastro de médicos veterinários e patologistas com controle de acesso e segurança.
* **Solicitação de Exames:** Módulo padronizado com campos obrigatórios e upload de arquivos (PDF e imagens).
* **Notificações:** Alertas automáticos sobre atualizações no status dos exames (via sistema ou e-mail).
* **Histórico e Relatórios:** Registro de solicitações com número de protocolo e geração de relatórios por cliente, médico ou exame.
* **Segurança:** Autenticação via JWT e perfis de acesso definidos.

> **Nota:** Integrações com sistemas de terceiros e funcionalidades financeiras não fazem parte do escopo atual.

## 🚀 Tecnologias Utilizadas

### Backend
* **Linguagem:** Java 21 (LTS).
* **Framework:** Spring Boot (APIs RESTful, Spring Security, Spring Data).
* **Comunicação:** API RESTful com JSON.

### Frontend
* **Biblioteca:** React.
* **UI/Estilização:** Material-UI para interface responsiva.
* **Gerenciamento de Estado:** Redux ou Context API.

### Banco de Dados & Infraestrutura
* **Banco de Dados:** PostgreSQL.
* **Containerização:** Docker (para serviços e banco de dados).
* **Orquestração/Deploy:** VPS configurada com Traefik e Portainer.

## 👥 Equipe de Desenvolvimento
O projeto é desenvolvido por uma equipe multidisciplinar responsável por todas as etapas, desde a documentação até a infraestrutura:

* **Amália Vitória de Melo:** Comunicação com cliente e gerência de tarefas.
* **Jade Bortot de Paiva:** Documentação e escrita.
* **Ketlen Victória Martins de Souza:** Prototipagem. e UI/UX
* **Lorena Bitencourt Salvador:** Desenvolvimento Back-end e Banco de dados.
* **Márcio Guilherme Vieira Silva:** Desenvolvimento Back-end e Banco de dados.
* **Otávio Augusto Colucci de Oliveira:** Desenvolvimento Front-end e Infraestrutura.
