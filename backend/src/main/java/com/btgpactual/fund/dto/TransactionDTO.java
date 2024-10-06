package com.btgpactual.fund.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Davidparro
 */
public class TransactionDTO {
    private String id; 
    private String fundName;
    private String type;
    private LocalDate date;
    private BigDecimal amount;

    // Constructor
    public TransactionDTO(String id, String fundName, String type, LocalDate date, BigDecimal amount) {
        this.id = id;
        this.fundName = fundName;
        this.type = type;
        this.date = date;
        this.amount = amount;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}