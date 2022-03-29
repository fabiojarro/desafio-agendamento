package br.com.desafio.agenda.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.agenda.bean.ErrorMessage;
import br.com.desafio.agenda.entity.Cliente;
import br.com.desafio.agenda.exception.ApiException;
import br.com.desafio.agenda.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/clientes")
public class ClienteController {

	private ClienteService clienteService;
	
	@Autowired
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	@Operation(summary = "Criar cliente.",tags = {"cliente"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "201", description = "Cliente criado.", 
					 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Cliente.class))}),
			@ApiResponse(responseCode = "400", description = "Os parâmetros recebidos são inválidos",
						 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))})
	})
	@PostMapping @ResponseStatus(code = HttpStatus.CREATED)
	public Cliente create(@RequestBody Cliente cliente) {
		if(cliente.getId() == null) {
			return clienteService.save(cliente);
		}
		
		throw new ApiException("Os dados recebidos são inválidos.");
	}
	
	@Operation(summary = "Alterar cliente.",tags = {"cliente"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Cliente alterado.", 
					 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Cliente.class))}),
			@ApiResponse(responseCode = "400", description = "Os parâmetros recebidos são inválidos",
						 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))}),
			@ApiResponse(responseCode = "404", description = "O cliente não foi encontrado para a alteração.",
			 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))})
	})
	@PutMapping @ResponseStatus(code = HttpStatus.OK)
	public Cliente update(@RequestBody Cliente cliente) {
		Optional<Cliente> clienteOptional = clienteService.getById(cliente.getId());
		if(clienteOptional.isPresent()) {
			return clienteService.save(cliente);
		}
		
		String message = new StringBuilder("O cliente com id ").append(cliente.getId()).append(" não foi encontrado.").toString();
		
		throw new ApiException(message, HttpStatus.NOT_FOUND);
	}
	
	@Operation(summary = "Listar clientes.",tags = {"cliente"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Lista de clientes retornada.", 
					content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))})
	})
	@GetMapping
	public Collection<Cliente> list() {
		return clienteService.list();
	}
	
	@Operation(summary = "Remover cliente.",tags = {"cliente"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "204", description = "Cliente removido.", 
					 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Cliente.class))}),
			@ApiResponse(responseCode = "400", description = "Parâmetros recebidos são inválidos",
			 content = { @Content (mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))})
	})
	@DeleteMapping("/{id}") @ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void remove(@PathVariable Integer id) {
		clienteService.remove(id);
	}
}
