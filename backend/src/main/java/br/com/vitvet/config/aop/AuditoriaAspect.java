package br.com.vitvet.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import br.com.vitvet.config.anotation.LogDeAuditoria;

import java.util.Arrays;

@Aspect
@Component
@Profile("!dev") // Só ativa este componente se o perfil NÃO for "dev"
public class AuditoriaAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaAspect.class);

    @Around("@annotation(br.com.vitvet.config.anotation.LogDeAuditoria)")
    public Object auditarMetodo(ProceedingJoinPoint joinPoint) throws Throwable {

        String nomeUtilizador = "SISTEMA";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            nomeUtilizador = authentication.getName();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LogDeAuditoria logDeAuditoria = signature.getMethod().getAnnotation(LogDeAuditoria.class);
        String acao = logDeAuditoria.acao();
        String argumentos = Arrays.toString(joinPoint.getArgs());

        logger.info("[AUDITORIA] Utilizador [{}]: Iniciou a ação [{}]. Argumentos: {}",
                nomeUtilizador, acao, argumentos);

        try {
            Object resultado = joinPoint.proceed();

            logger.info("[AUDITORIA] Utilizador [{}]: Concluiu a ação [{}].",
                    nomeUtilizador, acao);

            return resultado;

        } catch (Throwable e) {
            logger.error("[AUDITORIA] Utilizador [{}]: Falha na ação [{}]. Erro: {}",
                    nomeUtilizador, acao, e.getMessage());
            throw e;
        }
    }
}