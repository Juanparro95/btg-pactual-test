package com.btgpactual.fund.service;

import com.btgpactual.fund.dto.TransactionDTO;
import com.btgpactual.fund.model.Fund;
import com.btgpactual.fund.service.FundService;
import com.btgpactual.fund.model.Transaction;
import com.btgpactual.fund.model.User;
import com.btgpactual.fund.repository.IFundRepository;
import com.btgpactual.fund.repository.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.stream.Collectors;


/**
 *
 * @author Davidparro
 */

@Service
public class TransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private IFundRepository fundRepository;    

    @Autowired
    private FundService fundService;

    // Obtener todas las transacciones de un fondo
    public List<Transaction> getTransactionsByFund(String fundId) {
        return transactionRepository.findByFundId(fundId);
    }

    // Crear una nueva transacción
    public Transaction saveTransaction(Transaction transaction, String userId) {
        BigDecimal clientBalance = fundService.getClientBalance(userId);

        // Validar si el balance del cliente es suficiente
        if (clientBalance.compareTo(transaction.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance for this transaction");
        }

        // Validar el tipo de transacción
        if (!transaction.getType().equalsIgnoreCase("INVEST") && !transaction.getType().equalsIgnoreCase("WITHDRAW")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction type");
        }

        // Guardar la transacción
        return transactionRepository.save(transaction);
    }
    
    // Obtener todas las transacciones de un usuario
    public List<TransactionDTO> getTransactionsByUser(User user) {
        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());

        // Convertir cada Transaction en un TransactionDTO
        List<TransactionDTO> transactionDTOs = transactions.stream()
            .map(transaction -> {
                Fund fund = fundRepository.findById(transaction.getFundId()).orElse(null);
                String fundName = (fund != null) ? fund.getName() : "Fondo Desconocido";
                return new TransactionDTO(transaction.getId(), fundName, transaction.getType(), transaction.getDate(), transaction.getAmount());
            })
            .collect(Collectors.toList());

        return transactionDTOs;
    }


    // Eliminar una transacción por su ID
    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);
    }
}
