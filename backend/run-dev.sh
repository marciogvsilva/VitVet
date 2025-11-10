#!/bin/bash

echo "Iniciando a aplicação VitVet em modo de DESENVOLVIMENTO..."
echo "A segurança estará DESABILITADA."

./mvnw spring-boot:run -e -X -Dspring-boot.run.profiles=dev