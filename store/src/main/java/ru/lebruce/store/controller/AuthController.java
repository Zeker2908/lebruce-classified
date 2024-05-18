package ru.lebruce.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lebruce.store.domain.dto.JwtAuthenticationResponse;
import ru.lebruce.store.domain.dto.SignInRequest;
import ru.lebruce.store.domain.dto.SignUpRequest;
import ru.lebruce.store.service.AuthenticationUserService;
import ru.lebruce.store.service.PendingUserService;
import ru.lebruce.store.service.UserService;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationUserService authenticationService;
    private final UserService userService;
    private final PendingUserService pendingUserService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) throws MessagingException {
        authenticationService.checkUserExistence(request.getUsername());
        authenticationService.signUp(request);
        return ResponseEntity.ok("На вашу почту " + request.getUsername() + " отправлено письмо");
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmEmail(@RequestParam String token) {
        authenticationService.confirmEmail(token);
        return ResponseEntity.ok("Email успешно подтвержден");
    }
}

