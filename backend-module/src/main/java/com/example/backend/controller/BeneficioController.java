package com.example.backend.controller;

import com.example.backend.service.BeneficioService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.example.backend.domain.Beneficio;
import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferRequestDTO;
import com.example.backend.exception.BusinessException;
import com.example.backend.mapper.BeneficioMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/beneficios")
public class BeneficioController {

    private final BeneficioService service;
    private final BeneficioMapper mapper;

    public BeneficioController(BeneficioService service, BeneficioMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<BeneficioDTO> list() {
        return service.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficioDTO> get(@PathVariable Long id) {
        return service.findById(id).map(mapper::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }



    @PostMapping
    public ResponseEntity<BeneficioDTO> create(@RequestBody @Valid BeneficioDTO dto) {
        Beneficio created = service.create(mapper.toEntity(dto));
        return ResponseEntity.created(URI.create("/api/beneficios/" + created.getId())).body(mapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeneficioDTO> update(@PathVariable Long id, @RequestBody @Valid BeneficioDTO dto) {
        try {
            Beneficio updated = service.update(id, mapper.toEntity(dto));
            return ResponseEntity.ok(mapper.toDto(updated));
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody @Valid TransferRequestDTO req) {
        try {
            service.transferWithRetry(req);
            return ResponseEntity.ok().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
