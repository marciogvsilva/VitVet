# Projeto VitVet - API Backend

Sistema para automatizar a comunicação entre médicos veterinários e patologistas na requisição de exames, como parte do projeto da disciplina de SSC536 (ICMC-USP).

## Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3**
* **Spring Security & JWT:** Para autenticação e autorização.
* **Spring Data JPA** (Hibernate): Para persistência de dados.
* **Spring AOP:** Para logs de auditoria (RF12).
* **PostgreSQL:** Base de dados para o ambiente de produção.
* **H2 Database:** Base de dados em memória para o ambiente de desenvolvimento (dev).
* **Swagger** (Springdoc): Para documentação e teste da API.
* **Maven:** Para gestão de dependências e build.
* **Lombok:** Para reduzir código repetitivo (getters, setters, etc.).

---

## Pré-requisitos

Para executar este projeto localmente, irá precisar de:

* JDK 21 (SDKMAN recomendado: `sdk install java 21.0.8-graal`)
* Apache Maven
* Uma IDE Java (IntelliJ IDEA é recomendado)
* (Opcional para modo de produção) Uma instância do PostgreSQL a correr.

---

## Como Executar a Aplicação (Modo de Desenvolvimento)

Este projeto está configurado com Perfis (Profiles) do Spring. O perfil `dev` foi criado para agilizar o desenvolvimento:

* Não exige autenticação (toda a segurança é desativada).
* Usa uma base de dados H2 em memória (não precisa do Docker ou PostgreSQL).
* A base de dados é pré-populada com dados de teste (definidos em `data.sql`).

### Instruções para IntelliJ IDEA (Método Recomendado)

A execução do script `run-dev.sh` está a falhar devido a um erro de codificação de ficheiros (MalformedInputException). O método mais fiável é usar a configuração de arranque da IDE.

**Executar:**

* Basta clicar no botão "Run" (Play ▶️) ou "Debug" (🐞).
* A aplicação irá arrancar em segundos, sem segurança e pronta a usar.

---

## Aceder aos Recursos (Em Modo dev)

Quando a aplicação estiver a correr no perfil `dev`, pode aceder a tudo diretamente no seu browser:

### 4.1. Documentação da API (Swagger)

Use o Swagger para ver e testar todos os endpoints de forma interativa.

* **URL:** `http://localhost:8080/api/docs`
* **Nota:** No modo `dev`, não precisa de se autenticar. Todos os endpoints (incluindo os de Tutor e Animal) estarão abertos para teste.

### 4.2. Base de Dados (H2 Console)

Pode ver as tabelas e os dados (pré-carregados pelo `data.sql`) diretamente na consola H2.

* **URL:** `http://localhost:8080/h2-console`
* **Configuração de Login:** É crucial usar os dados exatos do seu ficheiro `application-dev.properties` para se ligar:
    * **JDBC URL:** `jdbc:h2:mem:vitvet_db`
    * **User Name:** `sa`
    * **Password:** (deixe em branco)

### 4.3. Dados de Teste (Injetados por data.sql)

A base de dados `dev` já arranca com estes dados:

* **Veterinário:** `vet@vitvet.com` (senha: `123`)
* **Patologista:** `pato@vitvet.com` (senha: `123`)
* **Tutor:** "Ana Silva" (ID: 1)
* **Animal:** "Pipoca" (ID: 1)
* **Exames:** Hemograma, Urina, Raio-X.
* **Solicitação:** Uma solicitação de exemplo do "Dr. Veterinário" para o "Pipoca".