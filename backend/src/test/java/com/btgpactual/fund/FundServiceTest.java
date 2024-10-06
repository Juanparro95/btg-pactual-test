package com.btgpactual.fund;

import com.btgpactual.fund.model.Fund;
import com.btgpactual.fund.service.FundService;
import com.btgpactual.fund.model.User;
import com.btgpactual.fund.repository.IFundRepository;
import com.btgpactual.fund.repository.ITransactionRepository;
import com.btgpactual.fund.repository.IUserRepository;
import com.btgpactual.fund.service.UserService;
import com.btgpactual.fund.util.ServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FundServiceTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private IFundRepository fundRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;
    
    @Mock
    private UserService userService;

    @InjectMocks
    private FundService fundService;

    @Mock
    private MimeMessage mimeMessage;

    private BigDecimal initialClientBalance;
    private String userId;
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simular el usuario y su balance en base de datos
        mockUser = new User();
        mockUser.setId("1");
        mockUser.setName("John Doe");
        mockUser.setEmail("example@user.com");
        mockUser.setTotalTransactions(new BigDecimal("500000")); // Saldo inicial del usuario

        userId = mockUser.getId();

        // Simular los métodos del mock de userService
        when(userService.findUserById(userId)).thenReturn(Optional.of(mockUser));

        // Simular la creación del mensaje Mime
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }


    // Test: Suscripción exitosa
    @Test
    void testSubscribeToFundSuccess() throws MessagingException {
        Fund mockFund = new Fund("Fondo de Inversión A", new BigDecimal("100000"), "FPV");
        mockFund.setId("1");

        // Simular la consulta del fondo y el balance inicial del usuario
        when(fundRepository.findById("1")).thenReturn(Optional.of(mockFund));
        when(userService.findUserById(userId)).thenReturn(Optional.of(mockUser));

        ServiceResponse result = fundService.subscribeToFund("1", userId, "sms");

        assertEquals("Suscripción exitosa al fondo: Fondo de Inversión A", result.getMessage());
        assertTrue(result.isSuccess());

        // Verificar que el balance del usuario se haya actualizado correctamente
        assertEquals(new BigDecimal("400000"), mockUser.getTotalTransactions());
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    // Test: Saldo insuficiente
    @Test
    void testSubscribeToFundInsufficientBalance() {
        Fund mockFund = new Fund("Fondo de Inversión B", new BigDecimal("600000"), "FPV");
        mockFund.setId("2");

        when(fundRepository.findById("2")).thenReturn(Optional.of(mockFund));
        when(userService.findUserById(userId)).thenReturn(Optional.of(mockUser));  // Asegurarse de que el mockUser sea retornado

        ServiceResponse result = fundService.subscribeToFund("2", userId, "sms");

        assertEquals("Saldo insuficiente para suscribirse al fondo: Fondo de Inversión B", result.getMessage());
        assertEquals(false, result.isSuccess());
        assertEquals(mockUser.getTotalTransactions(), fundService.getClientBalance(userId));  // Asegurarse de usar el balance correcto del usuario
        verify(mailSender, times(0)).send(any(MimeMessage.class));
    }

    // Test: Fondo no encontrado
    @Test
    void testSubscribeToFundNotFound() {
        when(fundRepository.findById(anyString())).thenReturn(Optional.empty());

        ServiceResponse result = fundService.subscribeToFund("999", userId, "sms");

        assertEquals("Fondo o usuario no encontrado", result.getMessage());
        assertEquals(false, result.isSuccess());
        verify(mailSender, times(0)).send(any(MimeMessage.class));
    }

    // Test: Límite con monto extremadamente grande
    @Test
    void testSubscribeToFundWithLargeAmount() {
        Fund mockFund = new Fund("Fondo de Inversión C", new BigDecimal("1000000"), "FPV");
        mockFund.setId("3");

        when(fundRepository.findById("3")).thenReturn(Optional.of(mockFund));
        when(userService.findUserById(userId)).thenReturn(Optional.of(mockUser));  // Asegurarse de que el mockUser sea retornado

        ServiceResponse result = fundService.subscribeToFund("3", userId, "sms");

        assertEquals("Saldo insuficiente para suscribirse al fondo: Fondo de Inversión C", result.getMessage());
        assertEquals(false, result.isSuccess());
        assertEquals(mockUser.getTotalTransactions(), fundService.getClientBalance(userId));  // Asegurarse de usar el balance correcto del usuario
        verify(mailSender, times(0)).send(any(MimeMessage.class));
    }

    // Test: Límite justo en el balance del cliente
    @Test
    void testSubscribeToFundWithExactBalance() throws MessagingException {
        Fund mockFund = new Fund("Fondo de Inversión D", new BigDecimal("500000"), "FPV");
        mockFund.setId("4");

        when(fundRepository.findById("4")).thenReturn(Optional.of(mockFund));
        mockUser.setTotalTransactions(new BigDecimal("500000"));  // Simular que el balance del usuario es el monto exacto
        when(userService.findUserById(userId)).thenReturn(Optional.of(mockUser));

        ServiceResponse result = fundService.subscribeToFund("4", userId, "sms");

        assertEquals("Suscripción exitosa al fondo: Fondo de Inversión D", result.getMessage());
        assertEquals(true, result.isSuccess());
        assertEquals(BigDecimal.ZERO, mockUser.getTotalTransactions());  // Verificar que el balance ahora sea cero
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
