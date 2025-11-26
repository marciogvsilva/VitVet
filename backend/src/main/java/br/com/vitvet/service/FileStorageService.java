package br.com.vitvet.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // Pasta onde os ficheiros serão guardados (na raiz do projeto)
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileStorageService() {
        try {
            // Cria a pasta se ela não existir
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o diretório para upload de ficheiros.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normaliza o nome do ficheiro
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Verifica se o nome do ficheiro contém caracteres inválidos
            if (fileName.contains("..")) {
                throw new RuntimeException("Nome do ficheiro inválido: " + fileName);
            }

            // Gera um nome único para evitar conflitos
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

            // Copia o ficheiro para a pasta de destino (substituindo se já existir com o mesmo nome)
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível guardar o ficheiro " + fileName + ". Tente novamente!", ex);
        }
    }
}