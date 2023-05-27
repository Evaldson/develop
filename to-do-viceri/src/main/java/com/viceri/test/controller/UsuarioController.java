package com.viceri.test.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.viceri.test.model.ToDoItem;
import com.viceri.test.model.Usuario;
import com.viceri.test.repository.ToDoItemRepository;
import com.viceri.test.repository.UsuarioRepository;
import com.viceri.test.service.ToDoItemService;
import com.viceri.test.service.TokenService;
import com.viceri.test.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/usuarios")
@Tag(name = "To-Do List")
public class UsuarioController {

	private UsuarioService usuarioService;
	private ToDoItemService itemService;
	private TokenService tokenService;
	private UsuarioRepository usuarioRepository;
	private ToDoItemRepository itemRespository;
	private PasswordEncoder encoder;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Adiciona um usuário no banco de dados", method = "POST")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Cria o usuário"),
			@ApiResponse(responseCode = "403", description = "Token inválido"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar o cadastro"), })
	public ResponseEntity<Usuario> adicionar(@Valid @RequestBody Usuario usuario,
			@Parameter(in = ParameterIn.HEADER, description = "Token de acesso", required = true) @RequestHeader("Authorization") String authorizationHeader) {

		Optional<Usuario> isUsuarioAlreadyCreated = usuarioRepository.findByEmail(usuario.getEmail());

		if (isUsuarioAlreadyCreated.isEmpty()) {
			
			usuario.setPassword(encoder.encode(usuario.getPassword()));
			Usuario usuarioCreated = usuarioService.salvar(usuario);
			
			return ResponseEntity.ok(usuarioCreated);
		}

		return ResponseEntity.badRequest().build();
	}

	@GetMapping("/task/unfinished")
	@Operation(summary = "Busca a lista de tarefas não finalizadas do usuário", method = "GET")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Obtem a lista de tasks"),
			@ApiResponse(responseCode = "403", description = "Token inválido"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar a busca"), })
	public ResponseEntity<List<ToDoItem>> filtroItem(@Valid @RequestParam(defaultValue = "") String priority,
			@Parameter(in = ParameterIn.HEADER, description = "Token de acesso", required = true) @RequestHeader("Authorization") String authorizationHeader) {

		Optional<Usuario> usuarioLogado = usuarioRepository.findByEmail(getUser());

		if (usuarioLogado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		if (priority.isEmpty()) {
			return ResponseEntity.ok(itemService.getTasks(usuarioLogado.get()));
		} else {
			return ResponseEntity.ok(itemService.getTasksByPriority(usuarioLogado.get(), priority));
		}

	}

	@PutMapping("/task")
	@Operation(summary = "Adiciona uma nova task ", method = "PUT")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Adicionou a task para o usuário logado"),
			@ApiResponse(responseCode = "403", description = "Token inválido"),
			@ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro ao adicionar a task"), })
	public ResponseEntity<Usuario> adicionarItem(@Valid @RequestBody ToDoItem item,
			@Parameter(in = ParameterIn.HEADER, description = "Token de acesso", required = true) @RequestHeader("Authorization") String authorizationHeader) {

		Optional<Usuario> usuarioLogado = usuarioRepository.findByEmail(getUser());

		if (usuarioLogado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		item.setUsuario(usuarioLogado.get());

		ToDoItem savedItem = itemRespository.save(item);

		usuarioLogado.get().getTasks().add(savedItem);

		usuarioService.salvar(usuarioLogado.get());

		return ResponseEntity.ok(usuarioLogado.get());
	}

	@PatchMapping("/task/{id}/completed")
	@Operation(summary = "Completar task", method = "PATCH")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Modifica a task para completed"),
			@ApiResponse(responseCode = "403", description = "Token inválido"),
			@ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro ao modificar a task"), })
	public ResponseEntity<Usuario> concluirItem(@Valid @PathVariable Long id,
			@Parameter(in = ParameterIn.HEADER, description = "Token de acesso", required = true) @RequestHeader("Authorization") String authorizationHeader) {

		Optional<Usuario> usuarioLogado = usuarioRepository.findByEmail(getUser());

		if (usuarioLogado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Optional<ToDoItem> foundTask = findTask(id, usuarioLogado);

		if (foundTask.isPresent()) {
			ToDoItem task = foundTask.get();
			task.setCompleted(true);
			itemRespository.save(task);
		} else {
			return ResponseEntity.notFound().build();
		}

		usuarioService.salvar(usuarioLogado.get());

		return ResponseEntity.ok(usuarioLogado.get());
	}

	@PatchMapping("/task/{id}")
	@Operation(summary = "Altera a task selecionada", method = "PATCH")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Alterou a task selecionada"),
			@ApiResponse(responseCode = "403", description = "Token inválido"),
			@ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro ao alterar a task"), })
	public ResponseEntity<Usuario> alterarItem(@PathVariable Long id, @Valid @RequestBody ToDoItem item,
			@Parameter(in = ParameterIn.HEADER, description = "Token de acesso", required = true) @RequestHeader("Authorization") String authorizationHeader) {

		Optional<Usuario> usuarioLogado = usuarioRepository.findByEmail(getUser());

		if (usuarioLogado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Optional<ToDoItem> foundTask = findTask(id, usuarioLogado);

		if (foundTask.isPresent()) {
			ToDoItem task = foundTask.get();
			task.setPriority(item.getPriority());
			task.setDescription(item.getDescription());
			itemRespository.save(task);
		} else {
			return ResponseEntity.notFound().build();
		}

		usuarioService.salvar(usuarioLogado.get());

		return ResponseEntity.ok(usuarioLogado.get());
	}

	@DeleteMapping("/task/{id}")
	@Operation(summary = "Deleta a task selecionada", method = "DELETE")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deletou com sucesso a task selecionada"),
			@ApiResponse(responseCode = "403", description = "Token inválido"),
			@ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro ao deletar a task"), })
	public ResponseEntity<Usuario> excluirItem(@Valid @PathVariable Long id,
			@Parameter(in = ParameterIn.HEADER, description = "Token de acesso", required = true) @RequestHeader("Authorization") String authorizationHeader) {

		Optional<Usuario> usuarioLogado = usuarioRepository.findByEmail(getUser());

		if (usuarioLogado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Optional<ToDoItem> foundTask = findTask(id, usuarioLogado);

		if (foundTask.isPresent()) {
			usuarioLogado.get().getTasks().removeIf(task -> task.getId() == id);
			ToDoItem task = foundTask.get();
			itemRespository.delete(task);
		} else {
			return ResponseEntity.notFound().build();
		}

		usuarioService.salvar(usuarioLogado.get());

		return ResponseEntity.ok(usuarioLogado.get());
	}

	private Optional<ToDoItem> findTask(Long id, Optional<Usuario> usuarioLogado) {
		Optional<ToDoItem> foundTask = usuarioLogado.get().getTasks().stream().filter(task -> task.getId() == id)
				.findFirst();
		return foundTask;
	}

	private String getUser() {
		return tokenService.extractUserFromToken(tokenService.getToken());
	}

}
