package com.utp.salonbarberiavargas.service;

import com.utp.salonbarberiavargas.model.Usuario;
import com.utp.salonbarberiavargas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para validar las credenciales del login
    public boolean validarLogin(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        // Si el usuario existe, verificamos que la contraseña coincida
        if (usuarioOpt.isPresent()) {
            return usuarioOpt.get().getPassword().equals(password);
        }
        return false; // Si no existe o la clave es incorrecta, devuelve false
    }
}
