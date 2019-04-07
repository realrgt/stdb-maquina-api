package com.ergito.stbmaquinasapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ergito.stbmaquinasapi.model.Maquina;
import com.ergito.stbmaquinasapi.repository.MaquinaRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({"/maquinas"})
public class MaquinaController {

	@Autowired
	private MaquinaRepository repository; // Control inversion or D.I

	public MaquinaController(MaquinaRepository maquinaRepository) {
		this.repository = maquinaRepository;
	}

	// CRUD methods

	@SuppressWarnings("rawtypes")
	@GetMapping
	public List findAll() {
		return this.repository.findAll();
	}

	@GetMapping(path = { "/{id}" })
	public ResponseEntity<Maquina> findById(@PathVariable long id) {
		return repository.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Maquina create(@RequestBody Maquina maquina) {
		return repository.save(maquina);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Maquina> update(@PathVariable("id") long id, @RequestBody Maquina maquina) {
		return repository.findById(id).map(record -> {
			record.setTipoPagamento(maquina.getTipoPagamento());
			record.setMarca(maquina.getMarca());
			record.setModelo(maquina.getModelo());
			record.setValor(maquina.getValor());
			record.setMensalidade(maquina.getMensalidade());
			record.setImage(maquina.getImage());
			Maquina updated = repository.save(record);
			return ResponseEntity.ok().body(updated);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = { "/{id}" })
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		return repository.findById(id).map(record -> {
			repository.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
