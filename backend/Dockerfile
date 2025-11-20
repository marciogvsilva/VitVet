# Etapa 1: Construção (Build)
# Usamos uma imagem Maven com JDK 21 para compilar o projeto
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o ficheiro de dependências (pom.xml) para o container
COPY pom.xml .

# Baixa as dependências do projeto (cache das dependências para builds mais rápidos)
RUN mvn dependency:go-offline -B

# Copia todo o código-fonte para o container
COPY src ./src

# Compila o projeto e cria o ficheiro .jar (pula os testes para agilizar o build no Docker)
RUN mvn clean package -DskipTests

# Etapa 2: Execução (Runtime)
# Usamos uma imagem leve do OpenJDK 21 apenas para rodar a aplicação
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o ficheiro .jar gerado na etapa de construção para a etapa de execução
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080 (onde o Spring Boot roda)
EXPOSE 8080

# Define o perfil padrão como "prod" (assumindo que usará o PostgreSQL em produção)
# Você pode sobrescrever isso ao rodar o container (ex: -e SPRING_PROFILES_ACTIVE=dev)
ENV SPRING_PROFILES_ACTIVE=prod

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]