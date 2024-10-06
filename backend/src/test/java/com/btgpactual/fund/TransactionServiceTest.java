package com.btgpactual.fund;

import com.btgpactual.fund.model.Fund;
import com.btgpactual.fund.model.Transaction;
import com.btgpactual.fund.repository.ITransactionRepository;
import com.btgpactual.fund.service.FundService;
import com.btgpactual.fund.service.TransactionService;
import org.springframework.web.server.ResponseStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private FundService fundService; 

    private BigDecimal initialClientBalance;
    private String userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        initialClientBalance = new BigDecimal("500000");  // Saldo inicial de prueba
        userId = "1";  // Id del usuario simulado
        // Simulamos el saldo del cliente
        when(fundService.getClientBalance(userId)).thenReturn(initialClientBalance);
    }

    // Test: Registrar una transacción exitosamente
    @Test
    void testRegisterTransactionSuccess() {
        Fund mockFund = new Fund("Fondo de Inversión A", new BigDecimal("100000"), "FPV");
        mockFund.setId("1");

        Transaction mockTransaction = new Transaction();
        mockTransaction.setId("1");
        mockTransaction.setAmount(new BigDecimal("100000"));
        mockTransaction.setDate(LocalDate.now());
        mockTransaction.setType("INVEST");
        mockTransaction.setFundId(mockFund.getId());
        mockTransaction.setUserId(userId);  // Asegurarse de incluir el userId en la transacción

        when(transactionRepository.save(any(Transaction.class))).thenReturn(mockTransaction);

        Transaction result = transactionService.saveTransaction(mockTransaction, userId);

        assertEquals(mockTransaction, result);  // Verificamos que la transacción devuelta sea correcta
        verify(transactionRepository, times(1)).save(any(Transaction.class));  // Verificamos que se guarda la transacción
    }

    // Test: Transacción fallida por saldo insuficiente
    @Test
    void testRegisterTransactionInsufficientBalance() {
        Fund mockFund = new Fund("Fondo de Inversión B", new BigDecimal("600000"), "FPV");
        mockFund.setId("2");

        Transaction mockTransaction = new Transaction();
        mockTransaction.setId("2");
        mockTransaction.setAmount(new BigDecimal("600000"));
        mockTransaction.setDate(LocalDate.now());
        mockTransaction.setType("INVEST");
        mockTransaction.setFundId(mockFund.getId());
        mockTransaction.setUserId(userId);  // Asegurarse de incluir el userId en la transacción

        when(fundService.getClientBalance(userId)).thenReturn(new BigDecimal("500000"));

        // Verificamos que se lanza ResponseStatusException cuando no hay saldo suficiente
        assertThrows(ResponseStatusException.class, () -> {
            transactionService.saveTransaction(mockTransaction, userId);
        });

        verify(transactionRepository, times(0)).save(any(Transaction.class));  // No se debería guardar la transacción
    }

    // Test: Transacción con tipo inválido
    @Test
    void testRegisterTransactionInvalidType() {
        Fund mockFund = new Fund("Fondo de Inversión C", new BigDecimal("50000"), "FPV");

        Transaction mockTransaction = new Transaction();
        mockTransaction.setId("3");
        mockTransaction.setAmount(new BigDecimal("50000"));
        mockTransaction.setDate(LocalDate.now());
        mockTransaction.setType("INVALID_TYPE");
        mockTransaction.setFundId(mockFund.getId());
        mockTransaction.setUserId(userId);  // Asegurarse de incluir el userId en la transacción

        // Verificamos que se lanza ResponseStatusException cuando el tipo de transacción es inválido
        assertThrows(ResponseStatusException.class, () -> {
            transactionService.saveTransaction(mockTransaction, userId);
        });

        verify(transactionRepository, times(0)).save(any(Transaction.class));  // No se debería guardar la transacción
    }

    // Test: Límite superior en la transacción
    @Test
    void testRegisterTransactionWithLargeAmount() {
        Fund mockFund = new Fund("Fondo de Inversión D", new BigDecimal("50000"), "FPV");
        mockFund.setId("4");

        Transaction mockTransaction = new Transaction();
        mockTransaction.setId("4");
        mockTransaction.setAmount(new BigDecimal("10000000"));  // Monto extremadamente alto
        mockTransaction.setDate(LocalDate.now());
        mockTransaction.setType("INVEST");
        mockTransaction.setFundId(mockFund.getId());
        mockTransaction.setUserId(userId);  // Asegurarse de incluir el userId en la transacción

        // Verificamos que se lanza ResponseStatusException cuando el monto excede el límite
        assertThrows(ResponseStatusException.class, () -> {
            transactionService.saveTransaction(mockTransaction, userId);
        });

        verify(transactionRepository, times(0)).save(any(Transaction.class));  // No se debería guardar la transacción
    }

    // Test: Registrar una transacción con el saldo exacto
    @Test
    void testRegisterTransactionWithExactBalance() {
        Fund mockFund = new Fund("Fondo de Inversión E", new BigDecimal("500000"), "FPV");
        mockFund.setId("5");

        Transaction mockTransaction = new Transaction();
        mockTransaction.setId("5");
        mockTransaction.setAmount(new BigDecimal("500000"));  // Monto exactamente igual al saldo
        mockTransaction.setDate(LocalDate.now());
        mockTransaction.setType("INVEST");
        mockTransaction.setFundId(mockFund.getId());
        mockTransaction.setUserId(userId);  // Asegurarse de incluir el userId en la transacción

        // Simulamos que el balance del cliente es exactamente 500000
        when(fundService.getClientBalance(userId)).thenReturn(new BigDecimal("500000"));

        // Simulamos que el repository guarda y devuelve la misma transacción
        when(transactionRepository.save(any(Transaction.class))).thenReturn(mockTransaction);

        // Ejecutamos el servicio de transacciones
        Transaction result = transactionService.saveTransaction(mockTransaction, userId);

        // Verificamos que la transacción devuelta sea la misma que se pasó
        assertEquals(mockTransaction, result, "La transacción devuelta debe ser la misma que se guardó.");
        verify(transactionRepository, times(1)).save(any(Transaction.class));  // Verificamos que se guarda la transacción

        // Verificamos que el saldo ahora es cero
        verify(fundService, times(1)).getClientBalance(userId);
    }
}
