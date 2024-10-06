package com.btgpactual.fund.controller;

import com.btgpactual.fund.model.User;
import com.btgpactual.fund.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.springframework.http.HttpStatus;

@Tag(name = "Controlador de Usuarios", description = "Operaciones relacionadas con los usuarios")
@RestController
@RequestMapping("/api/users")
/**
 *
 * @author Davidparro
 */
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Crear un nuevo usuario", description = "Permite la creación de un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario creado con éxito"),
        @ApiResponse(responseCode = "400", description = "Error en la creación del usuario")
    })
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return ResponseEntity.ok(newUser);
    }

    @Operation(summary = "Obtener un usuario por ID", description = "Devuelve la información de un usuario por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@Parameter(description = "ID del usuario") @PathVariable String userId) {
        Optional<User> user = userService.findUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar un usuario existente", description = "Permite actualizar la información de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
        @Parameter(description = "ID del usuario") @PathVariable String userId, 
        @RequestBody User updatedUser) {
        Optional<User> existingUser = userService.findUserById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setTotalTransactions(updatedUser.getTotalTransactions());
            return ResponseEntity.ok(userService.updateUser(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un usuario", description = "Permite eliminar un usuario del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado con éxito"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID del usuario") @PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener un usuario por email", description = "Devuelve la información de un usuario por su correo electrónico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@Parameter(description = "Correo electrónico del usuario") @PathVariable String email) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
