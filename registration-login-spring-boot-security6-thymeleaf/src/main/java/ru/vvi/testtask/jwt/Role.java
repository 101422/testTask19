package ru.vvi.testtask.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }

}
