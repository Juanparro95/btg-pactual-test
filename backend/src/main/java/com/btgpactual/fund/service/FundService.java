package com.btgpactual.fund.service;

import com.btgpactual.fund.model.User;
import com.btgpactual.fund.model.Fund;
import com.btgpactual.fund.model.Transaction;
import com.btgpactual.fund.repository.IFundRepository;
import com.btgpactual.fund.repository.ITransactionRepository;
import com.btgpactual.fund.util.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.stream.Collectors;

/**
 *
 * @author Davidparro
 */
@Service
public class FundService {

    @Autowired
    private IFundRepository fundRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private JavaMailSender mailSender;

    // Obtener todos los fondos
    public List<Fund> getAllFunds() {
        return fundRepository.findAll();
    }

    // Buscar un fondo por su ID
    public Optional<Fund> getFundById(String id) {
        return fundRepository.findById(id);
    }

    // Crear o actualizar un fondo
    public Fund saveFund(Fund fund) {
        return fundRepository.save(fund);
    }

    // Eliminar un fondo por su ID
    public void deleteFund(String id) {
        fundRepository.deleteById(id);
    }

    // Suscribirse a un fondo
    public ServiceResponse subscribeToFund(String fundId, String userId, String methodSelect) {
        Optional<Fund> fundOptional = getFundById(fundId);
        Optional<User> userOptional = userService.findUserById(userId);

        if (fundOptional.isPresent() && userOptional.isPresent()) {
            Fund fund = fundOptional.get();
            User user = userOptional.get();
            BigDecimal fundMinimum = fund.getMinimumAmount();

            // Obtener el saldo actual del usuario desde la base de datos (totalTransactions)
            BigDecimal userBalance = user.getTotalTransactions();

            // Verificar si el saldo del usuario es suficiente
            if (userBalance.compareTo(fundMinimum) >= 0) {
                // Actualizamos el saldo del usuario
                user.setTotalTransactions(userBalance.subtract(fundMinimum));
                userService.updateUser(user);

                // Guardamos la transacción
                Transaction transaction = new Transaction();
                transaction.setFundId(fund.getId());
                transaction.setUserId(user.getId());
                transaction.setAmount(fundMinimum);
                transaction.setType("INVEST");
                transaction.setDate(LocalDate.now());

                transactionRepository.save(transaction);

                try {
                sendNotificationWithHtml(fund.getName(), user.getEmail(), user.getName(), methodSelect);
                } catch (MessagingException e) {
                    return new ServiceResponse("Error al enviar notificación por correo para el fondo: " + fund.getName(), false);
                }

                return new ServiceResponse("Suscripción exitosa al fondo: " + fund.getName(), true);
            } else {
                return new ServiceResponse("Saldo insuficiente para suscribirse al fondo: " + fund.getName(), false);
            }
        } else {
            return new ServiceResponse("Fondo o usuario no encontrado", false);
        }
    }


    // Desuscribirse de un fondo
    public ServiceResponse unsubscribeFromFund(String fundId, String userId) {
        Optional<Fund> fundOptional = getFundById(fundId);
        Optional<User> userOptional = userService.findUserById(userId);

        if (fundOptional.isPresent() && userOptional.isPresent()) {
            Fund fund = fundOptional.get();
            User user = userOptional.get();
            BigDecimal fundMinimum = fund.getMinimumAmount();

            BigDecimal currentBalance = user.getTotalTransactions();

            Optional<Transaction> subscriptionTransaction = transactionRepository
                .findByFundIdAndUserIdAndType(fundId, userId, "INVEST");

            if (subscriptionTransaction.isPresent()) {
                BigDecimal newBalance = currentBalance.add(fundMinimum);
                if (newBalance.compareTo(new BigDecimal("500000")) > 0) {
                    return new ServiceResponse("El saldo no puede exceder COP500,000. No se puede desuscribir del fondo: " + fund.getName(), false);
                }

                user.setTotalTransactions(newBalance);
                userService.updateUser(user);

                Transaction withdrawalTransaction = new Transaction();
                withdrawalTransaction.setFundId(fund.getId());
                withdrawalTransaction.setAmount(fundMinimum);
                withdrawalTransaction.setUserId(user.getId());
                withdrawalTransaction.setType("WITHDRAW");
                withdrawalTransaction.setDate(LocalDate.now());

                transactionRepository.save(withdrawalTransaction);
                transactionRepository.delete(subscriptionTransaction.get());

                return new ServiceResponse("Desuscripción exitosa del fondo: " + fund.getName(), true);
            } else {
                return new ServiceResponse("No está suscrito al fondo " + fund.getName(), false);
            }
        } else {
            return new ServiceResponse("Fondo o usuario no encontrado.", false);
        }
    }

    // Obtener los fondos suscritos por un usuario
    public List<Fund> getSubscribedFundsByUser(String userId) {
        List<String> fundIds = transactionRepository.findByUserIdAndType(userId, "INVEST")
            .stream()
            .map(Transaction::getFundId)
            .collect(Collectors.toList());

        return fundRepository.findAllById(fundIds);
    }


    // Enviar notificación por correo electrónico
    private void sendNotificationWithHtml(String fundName, String emailUser, String userName, String methodSelect) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        String methodMessage = methodSelect.equals("email") ? "por correo electrónico" : "por SMS";

        String htmlContent = 
            "<!DOCTYPE html>" +
            "<html lang=\"es\">" +
            "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>Notificación de suscripción</title>" +
                "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #FFFFFF; color: #002855; margin: 0; padding: 0; }" +
                    ".container { width: 70%; padding: 20px; background-color: #FFFFFF; }" +
                    ".header { background-color: #fff; padding: 20px; text-align: center; }" +
                    ".header img { width: 150px; }" +
                    ".content { padding: 20px; text-align: center; }" +
                    ".content h1 { color: #002855; }" +
                    ".content p { color: #002855; }" +
                    ".button { display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #5AB1E4; color: #FFFFFF; text-decoration: none; border-radius: 5px; }" +
                    ".footer { background-color: #002855; color: #FFFFFF; padding: 10px; text-align: center; }" +
                "</style>" +
            "</head>" +
            "<body>" +
                "<div class=\"container\">" +
                    "<div class=\"header\">" +
                        "<img src=\"https://www.btgpactual.com.co/sites/default/files/btg%20pactual%20-%20logo.png\" alt=\"BTG Pactual Logo\">" +
                    "</div>" +
                    "<div class=\"content\">" +
                        "<h1>Hola, " + userName + "</h1>" +
                        "<p>Te has suscrito correctamente al fondo <strong>" + fundName + "</strong>.</p>" +
                        "<p>Has seleccionado recibir las notificaciones mediante <strong>" + methodMessage + "</strong> sobre tu suscripción.</p>" +
                        "<a href=\"https://www.btgpactual.com.co\" class=\"button\">Ir a nuestro sitio web</a>" +
                    "</div>" +
                    "<div class=\"footer\">" +
                        "<p>&copy; 2024 BTG Pactual. Todos los derechos reservados.</p>" +
                    "</div>" +
                "</div>" +
            "</body>" +
            "</html>";

        helper.setTo(emailUser);
        helper.setSubject("Notificación de suscripción");
        helper.setText(htmlContent, true); 

        mailSender.send(mimeMessage);
    }


    // Obtener el saldo actual del cliente
    public BigDecimal getClientBalance(String userId) {
        Optional<User> userOptional = userService.findUserById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getTotalTransactions(); // Devuelve el saldo real del usuario
        } else {
            throw new RuntimeException("Usuario no encontrado.");
        }
    }

    
    // Establecer el saldo del cliente
    public void setClientBalance(String userId, BigDecimal newBalance) {
        Optional<User> userOptional = userService.findUserById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setTotalTransactions(newBalance); // Establecer el nuevo saldo
            userService.updateUser(user); // Guardar los cambios en la base de datos
        } else {
            throw new RuntimeException("Usuario no encontrado.");
        }
    }
}
