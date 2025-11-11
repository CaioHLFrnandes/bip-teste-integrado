package br.com.bip.service;

import br.com.bip.domain.Beneficio;
import br.com.bip.dto.TransferRequestDTO;
import br.com.bip.exception.BusinessException;
import br.com.bip.repository.BeneficioJpaRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BeneficioServiceTest {

    @Mock
    private BeneficioJpaRepository repo;

    @InjectMocks
    private BeneficioService service;

    public BeneficioServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void transferInsufficientShouldThrow() {
        Beneficio from = new Beneficio();
        from.setId(1L);
        from.setSaldo(new BigDecimal("10.00"));
        Beneficio to = new Beneficio();
        to.setId(2L);
        to.setSaldo(new BigDecimal("0.00"));

        when(repo.findByIdForUpdate(1L)).thenReturn(Optional.of(from));
        when(repo.findByIdForUpdate(2L)).thenReturn(Optional.of(to));

        TransferRequestDTO req = new TransferRequestDTO();
        req.setFromId(1L);
        req.setToId(2L);
        req.setAmount(new BigDecimal("20.00"));

        BusinessException ex = assertThrows(BusinessException.class, () -> service.transferWithRetry(req));
        assertTrue(ex.getMessage().toLowerCase().contains("saldo insuficiente"));
    }
}
