package br.com.desafio.agenda.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import br.com.desafio.agenda.entity.Servico;
import br.com.desafio.agenda.exception.ApiException;
import br.com.desafio.agenda.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

	private ServicoService servicoService;
	
	@Autowired
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	
	@Operation(summary = "Criar serviço.",tags = {"serviço"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "201", description = "Cliente criado.", 
					 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = Servico.class))}),
			@ApiResponse(responseCode = "400", description = "Os parâmetros recebidos são inválidos",
						 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
	})
	@PostMapping @ResponseStatus(code = HttpStatus.CREATED)
	public Servico create(@RequestBody Servico servico) {
		if(servico.getId() == null) {
			return servicoService.save(servico);
		}
		
		throw new ApiException("Os dados recebidos são inválidos.");
	}
	
	@Operation(summary = "Alterar serviço.",tags = {"serviço"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Serviço alterado.", 
					 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = Servico.class))}),
			@ApiResponse(responseCode = "400", description = "Os parâmetros recebidos são inválidos",
						 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}),
			@ApiResponse(responseCode = "404", description = "O serviço não foi encontrado para a alteração.",
			 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
	})
	@PutMapping @ResponseStatus(code = HttpStatus.OK)
	public Servico update(@RequestBody Servico servico) {
		Optional<Servico> clienteOptional = servicoService.getById(servico.getId());
		if(clienteOptional.isPresent()) {
			return servicoService.save(servico);
		}
		
		String message = new StringBuilder("O cliente com id ").append(servico.getId()).append(" não foi encontrado.").toString();
		
		throw new ApiException(message, HttpStatus.NOT_FOUND);
	}
	
	@Operation(summary = "Listar serviços.",tags = {"serviço"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Lista de serviços.", 
					content = { @Content (mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Servico.class)))})
	})
	@GetMapping
	public Collection<Servico> list() {
		return servicoService.list();
	}
	
	@Operation(summary = "Remover serviço.",tags = {"serviço"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "204", description = "Cliente removido.", 
					 content = { @Content (mediaType = "application/json")}),
			@ApiResponse(responseCode = "400", description = "Parâmetros recebidos são inválidos",
			 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
	})
	@DeleteMapping("/{id}") @ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void remove(@PathVariable Integer id) {
		servicoService.remove(id);
	}
}
