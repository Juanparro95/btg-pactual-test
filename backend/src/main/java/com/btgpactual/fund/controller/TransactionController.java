package com.btgpactual.fund.controller;

import com.btgpactual.fund.dto.TransactionDTO;
import com.btgpactual.fund.model.Transaction;
import com.btgpactual.fund.model.User;
import com.btgpactual.fund.service.TransactionService;
import com.btgpactual.fund.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Controlador de Transacciones", description = "Operaciones relacionadas con las transacciones")
@RestController
@RequestMapping("/api/transactions")
/**
 *
 * @author Davidparro
 */
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Obtener todas las transacciones de un fondo", description = "Devuelve una lista con todas las transacciones relacionadas a un fondo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones obtenidas correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "404", description = "Fondo no encontrado")})
    @GetMapping("/fund/{fundId}")
    public ResponseEntity<List<Transaction>> getTransactionsByFund(@Parameter(description = "ID del fondo") @PathVariable String fundId) {
        List<Transaction> transactions = transactionService.getTransactionsByFund(fundId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Crear una transacción", description = "Crea una nueva transacción para un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción creada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    @PostMapping("/transaction/{userId}")
    public ResponseEntity<Transaction> createTransaction(
            @Parameter(description = "ID del usuario") @PathVariable String userId,
            @RequestBody Transaction transaction) {

        Transaction createdTransaction = transactionService.saveTransaction(transaction, userId);
        return ResponseEntity.ok(createdTransaction);
    }

    @Operation(summary = "Obtener todas las transacciones de un usuario", description = "Devuelve todas las transacciones relacionadas a un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones obtenidas correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByUser(@Parameter(description = "ID del usuario") @PathVariable String userId) {
        Optional<User> user = userService.findUserById(userId);

        if (user.isPresent()) {
            List<TransactionDTO> transactions = transactionService.getTransactionsByUser(user.get());
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.status(404).body(null); // Si el usuario no existe
        }
    }

    @Operation(summary = "Eliminar una transacción", description = "Elimina una transacción por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transacción eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@Parameter(description = "ID de la transacción a eliminar") @PathVariable String id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
