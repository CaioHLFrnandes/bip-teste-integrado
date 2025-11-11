package com.example.backend.service;


import com.example.backend.domain.Beneficio;
import com.example.backend.dto.TransferRequestDTO;
import com.example.backend.exception.BusinessException;
import com.example.backend.repository.BeneficioJpaRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BeneficioService {

    private final BeneficioJpaRepository repo;

    public BeneficioService(BeneficioJpaRepository repo) {
        this.repo = repo;
    }

    public List<Beneficio> findAll() {
        return repo.findAll();
    }

    public Optional<Beneficio> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Beneficio create(Beneficio b) {
        if (b.getSaldo() == null) b.setSaldo(BigDecimal.ZERO);
        return repo.save(b);
    }

    @Transactional
    public Beneficio update(Long id, Beneficio payload) {
        Beneficio existing = repo.findById(id).orElseThrow(() -> new BusinessException("Benefício não encontrado"));
        existing.setTitular(payload.getTitular());
        existing.setSaldo(payload.getSaldo());
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Transactional
    public void transferWithRetry(TransferRequestDTO req) {
        int maxAttempts = 3;
        int attempt = 0;
        while (true) {
            try {
                attempt++;
                transfer(req);
                return;
            } catch (OptimisticLockException | CannotAcquireLockException ex) {
                if (attempt >= maxAttempts) {
                    throw new BusinessException("Falha na transferência por concorrência. Tente novamente.", ex);
                }
                // small backoff
                try { Thread.sleep(100L * attempt); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
    }

    @Transactional
    public void transfer(TransferRequestDTO req) {
        if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor inválido para transferência");
        }
        if (req.getFromId().equals(req.getToId())) {
            throw new BusinessException("Conta de origem e destino iguais");
        }

        // Lock rows pessimistically to avoid concurrent updates
        Beneficio from = repo.findByIdForUpdate(req.getFromId())
                .orElseThrow(() -> new BusinessException("Conta origem não encontrada"));

        Beneficio to = repo.findByIdForUpdate(req.getToId())
                .orElseThrow(() -> new BusinessException("Conta destino não encontrada"));

        if (from.getSaldo().compareTo(req.getAmount()) < 0) {
            throw new BusinessException("Saldo insuficiente");
        }

        from.setSaldo(from.getSaldo().subtract(req.getAmount()));
        to.setSaldo(to.getSaldo().add(req.getAmount()));

        // Save updates (enclosed in transaction). Version will handle optimistic collisions too.
        repo.save(from);
        repo.save(to);
    }
}
