package com.viceri.test.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.viceri.test.data.DetalheUsuarioData;
import com.viceri.test.model.Usuario;
import com.viceri.test.repository.UsuarioRepository;

@Component
public class DetalheUsuarioServiceImpl implements UserDetailsService {
	
	private final UsuarioRepository repository;

	public DetalheUsuarioServiceImpl(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByEmail(username);
		if (usuario.isEmpty()) {
			throw new UsernameNotFoundException("Usuário [" + username + "] não encontrado");
		}
		
		return new DetalheUsuarioData(usuario);
	}
}
