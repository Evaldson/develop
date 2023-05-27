package com.viceri.test.service;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.viceri.test.model.Usuario;
import com.viceri.test.repository.UsuarioRepository;


import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UsuarioService {

	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public Usuario salvar(@Valid Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

}
