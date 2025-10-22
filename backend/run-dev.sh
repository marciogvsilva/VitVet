#!/bin/bash
# Este script inicia a aplicação Spring Boot com o perfil 'dev' ativo.

echo "Iniciando a aplicação VitVet em modo de DESENVOLVIMENTO..."
echo "A segurança estará DESABILITADA."

./mvnw spring-boot:run -Dspring-boot.run.profiles=dev