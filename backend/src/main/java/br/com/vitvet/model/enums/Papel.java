package br.com.vitvet.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Papel implements GrantedAuthority {
    VETERINARIO,
    PATOLOGISTA;

    @Override
    public String getAuthority() {
        return this.name(); // Retorna o nome do enum como a "authority"
    }
}