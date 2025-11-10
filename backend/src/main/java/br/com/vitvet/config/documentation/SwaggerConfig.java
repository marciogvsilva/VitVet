package br.com.vitvet.config.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        info = @Info(
                title = "VitVet",
                description = """
                         ```DOCUMENTAÇÃO DA API DA VITVET (somente a versão v3 do Swagger).```
                        \s
                         **ATENÇÃO:** O Swagger consegue mapear apenas APIs REST.
                        \s
                         Caso o controller esteja anotado com ```@Controller```, ele não será identificado automaticamente.\s
                         Para resolver isso, substitua por ```@RestController``` ou adicione a anotação ```@Operation``` diretamente\s
                         no endpoint que deseja documentar. Ex: AutenticadorOIDCController.java
                        \s
                         Dica:
                         - ```@Controller``` → Usado para retornar páginas HTML/Thymeleaf, não serializa objetos automaticamente.
                         - ```@RestController``` → É um atalho para @Controller + @ResponseBody, usado para APIs REST que retornam JSON.
                        \s"""
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OperationCustomizer customizeOperationTags() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            String className = handlerMethod.getBeanType().getSimpleName();

            String tag = convertToTitleCaseWithSpaces(removeControllerSuffix(className));

            operation.setTags(List.of(tag));
            return operation;
        };
    }

    private String removeControllerSuffix(String className) {
        return className.replaceAll("Controller$", "");
    }

    private String convertToTitleCaseWithSpaces(String input) {
        return input.replaceAll("([a-z])([A-Z])", "$1 $2")
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1 $2")
                .trim()
                .replaceAll("\\s+", " ");
    }
}
