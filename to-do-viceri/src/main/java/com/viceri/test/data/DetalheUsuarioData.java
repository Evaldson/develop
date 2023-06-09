package com.viceri.test.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.viceri.test.model.Usuario;

public class DetalheUsuarioData implements UserDetails{
	
	private final Optional<Usuario> usuario;
	
	public DetalheUsuarioData(Optional<Usuario> usuario) {
        this.usuario = usuario;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	@Override
	public String getPassword() {
		 return usuario.orElse(new Usuario()).getPassword();
	}

	@Override
	public String getUsername() {
		  return usuario.orElse(new Usuario()).getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
