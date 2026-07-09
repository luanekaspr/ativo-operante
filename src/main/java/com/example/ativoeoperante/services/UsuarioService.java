package com.example.ativoeoperante.services;
import com.example.ativoeoperante.entities.Erro;
import com.example.ativoeoperante.entities.Usuario;
import com.example.ativoeoperante.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> buscarTodosUsuarios(){
        List<Usuario> usuarioList = usuarioRepository.findAll();
        return usuarioList;
    }

    public boolean verificarEmail(String email){
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(email);
        return usuarioEncontrado == null;
    }

    public Usuario inserirUsuario(Usuario novoUsuario){
        if(verificarEmail(novoUsuario.getEmail())){
            novoUsuario.setNivel(2);
            novoUsuario = usuarioRepository.save(novoUsuario);
            return novoUsuario;
        }
        return null;
    }
}
