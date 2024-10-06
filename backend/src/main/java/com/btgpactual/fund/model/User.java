package com.btgpactual.fund.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

/**
 *
 * @author Davidparro
 */
@Document(collection = "users")
public class User {

    @Id
    private String id; 
    private String name;
    private String email;
    private BigDecimal totalTransactions = BigDecimal.ZERO;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(BigDecimal totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
}
