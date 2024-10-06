package com.btgpactual.fund.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Davidparro
 */
@Document(collection = "fund")
public class Fund {
    @Id
    private String id; 
    private String name;
    private BigDecimal minimumAmount;
    private String category;
    
    public Fund(String name, BigDecimal minimumAmount, String category) {
        this.name = name;
        this.minimumAmount = minimumAmount;
        this.category = category;
    }
    
    private List<Transaction> transactions;

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
