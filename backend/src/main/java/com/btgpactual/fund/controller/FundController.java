package com.btgpactual.fund.controller;

import com.btgpactual.fund.model.Fund;
import com.btgpactual.fund.service.FundService;
import com.btgpactual.fund.util.ServiceResponse;
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
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Tag(name = "Controlador de Fondos", description = "Operaciones relacionadas con los fondos")
@RestController
@RequestMapping("/api/funds")
/**
 *
 * @author Davidparro
 */
public class FundController {

    @Autowired
    private FundService fundService;

    @Operation(summary = "Obtener todos los fondos", description = "Devuelve una lista con todos los fondos disponibles")
    @ApiResponse(responseCode = "200", description = "Fondos obtenidos correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Fund.class)))
    @GetMapping
    public ResponseEntity<List<Fund>> getAllFunds() {
        List<Fund> funds = fundService.getAllFunds();
        return ResponseEntity.ok(funds);
    }

    @Operation(summary = "Obtener un fondo por su ID", description = "Devuelve los detalles de un fondo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fondo encontrado", content = @Content(schema = @Schema(implementation = Fund.class))),
            @ApiResponse(responseCode = "404", description = "Fondo no encontrado")})
    @GetMapping("/{id}")
    public ResponseEntity<Fund> getFundById(@Parameter(description = "ID del fondo a obtener") @PathVariable String id) {
        Optional<Fund> fund = fundService.getFundById(id);
        return fund.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo fondo", description = "Crea un nuevo fondo en la base de datos")
    @ApiResponse(responseCode = "200", description = "Fondo creado correctamente", content = @Content(schema = @Schema(implementation = Fund.class)))
    @PostMapping
    public ResponseEntity<Fund> createFund(@RequestBody Fund fund) {
        Fund createdFund = fundService.saveFund(fund);
        return ResponseEntity.ok(createdFund);
    }

    @Operation(summary = "Actualizar un fondo existente", description = "Actualiza los detalles de un fondo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fondo actualizado correctamente", content = @Content(schema = @Schema(implementation = Fund.class))),
            @ApiResponse(responseCode = "404", description = "Fondo no encontrado")})
    @PutMapping("/{id}")
    public ResponseEntity<Fund> updateFund(@Parameter(description = "ID del fondo a actualizar") @PathVariable String id, @RequestBody Fund fundDetails) {
        Optional<Fund> fund = fundService.getFundById(id);
        if (fund.isPresent()) {
            Fund updatedFund = fundService.saveFund(fundDetails);
            return ResponseEntity.ok(updatedFund);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un fondo", description = "Elimina un fondo por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fondo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Fondo no encontrado")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFund(@Parameter(description = "ID del fondo a eliminar") @PathVariable String id) {
        Optional<Fund> fund = fundService.getFundById(id);
        if (fund.isPresent()) {
            fundService.deleteFund(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener balance de usuario", description = "Devuelve el balance del cliente dado su ID de usuario")
    @ApiResponse(responseCode = "200", description = "Balance obtenido correctamente")
    @GetMapping("/balance")
    public ResponseEntity<?> getClientBalance(@Parameter(description = "ID del usuario para obtener el balance") @RequestParam String userId) {
        return ResponseEntity.ok(fundService.getClientBalance(userId));
    }

    @Operation(summary = "Suscribirse a un fondo", description = "Permite a un usuario suscribirse a un fondo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción realizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en la suscripción")})
    @PostMapping("/subscribe/{fundId}/{userId}")
    public ResponseEntity<Map<String, String>> subscribeToFund(@Parameter(description = "ID del fondo") @PathVariable String fundId,
                                                               @Parameter(description = "ID del usuario") @PathVariable String userId,
                                                               @RequestBody Map<String, String> requestBody) {
        String methodSelect = requestBody.get("methodSelect");
        ServiceResponse result = fundService.subscribeToFund(fundId, userId, methodSelect);

        Map<String, String> response = new HashMap<>();
        response.put("message", result.getMessage());

        if (result.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Desuscribirse de un fondo", description = "Permite a un usuario desuscribirse de un fondo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desuscripción realizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en la desuscripción")})
    @PostMapping("/unsubscribe/{fundId}/{userId}")
    public ResponseEntity<Map<String, String>> unSubscribeToFund(@Parameter(description = "ID del fondo") @PathVariable String fundId,
                                                                 @Parameter(description = "ID del usuario") @PathVariable String userId) {
        ServiceResponse result = fundService.unsubscribeFromFund(fundId, userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", result.getMessage());

        if (result.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Obtener los fondos suscritos por un usuario", description = "Devuelve los fondos a los que un usuario está suscrito")
    @ApiResponse(responseCode = "200", description = "Fondos suscritos obtenidos correctamente")
    @GetMapping("/subscribed/{userId}")
    public ResponseEntity<List<Fund>> getSubscribedFundsByUser(@Parameter(description = "ID del usuario") @PathVariable String userId) {
        List<Fund> subscribedFunds = fundService.getSubscribedFundsByUser(userId);
        return ResponseEntity.ok(subscribedFunds);
    }
}
