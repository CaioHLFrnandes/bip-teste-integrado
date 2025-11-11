package com.example.backend.dto;

import java.math.BigDecimal;

public class BeneficioDTO {
    private Long id;
    private String titular;
    private BigDecimal saldo;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
}
