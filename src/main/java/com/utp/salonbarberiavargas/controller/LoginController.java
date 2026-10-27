package com.utp.salonbarberiavargas.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.utp.salonbarberiavargas.service.UsuarioService;

@Controller
public class LoginController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("email") String email,
                                @RequestParam("password") String password,
                                Model model) {
        boolean valido = usuarioService.validarLogin(email, password);
        if (valido) {
            return "redirect:/"; // Redirige al dashboard/index si las credenciales coinciden con MySQL
        } else {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "login/login";
        }
    }
}