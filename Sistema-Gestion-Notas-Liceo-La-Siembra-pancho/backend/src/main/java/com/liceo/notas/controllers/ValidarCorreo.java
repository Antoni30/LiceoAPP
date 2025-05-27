package com.liceo.notas.controllers;

import com.liceo.notas.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/auth")
public class ValidarCorreo {
    @Autowired
    private AuthService authService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, Model model) {
        boolean exito = authService.verifyEmailToken(token);

        model.addAttribute("success", exito);
        model.addAttribute("message", exito ? "Email verificado exitosamente ✅" : "Token inválido o expirado ❎");

        return "verify-email-result";
    }
}
