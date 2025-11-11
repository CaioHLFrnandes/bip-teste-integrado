package java.br.com.bip.controller;

import src.main.java.com.example.*;
import org.testng.annotations.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeneficioController.class)
class BeneficioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeneficioService beneficioService;

    @Test
    void deveListarBeneficios() throws Exception {

        BeneficioDTO dto = new BeneficioDTO();
        dto.setId(1L);
        dto.setSaldo(new BigDecimal("300.00"));

        when(beneficioService.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/beneficios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].saldo").value(300.00));
    }

    @Test
    void deveCriarBeneficio() throws Exception {

        BeneficioDTO dto = new BeneficioDTO();
        dto.setId(1L);
        dto.setSaldo(new BigDecimal("500.00"));

        when(beneficioService.criar(any())).thenReturn(dto);

        mockMvc.perform(
                        post("/beneficios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                 {
                                   "saldo": 500.00
                                 }
                                 """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.saldo").value(500.00));
    }

    @Test
    void deveTransferir() throws Exception {

        Mockito.doNothing().when(beneficioService).transferir(any());

        mockMvc.perform(
                        post("/beneficios/transferir")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                 {
                                   "beneficiarioOrigemId": 1,
                                   "beneficiarioDestinoId": 2,
                                   "valor": 100.00
                                 }
                                 """)
                )
                .andExpect(status().isOk());
    }
}