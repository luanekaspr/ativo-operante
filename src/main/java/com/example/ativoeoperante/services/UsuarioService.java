package com.example.ativoeoperante.services;
import com.example.ativoeoperante.entities.Erro;
import com.example.ativoeoperante.entities.Usuario;
import com.example.ativoeoperante.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
        @Autowired
        UsuarioRepository usuarioRepository;

        // Cadastro do cidadão
        public Usuario cadastrar(Usuario usuario) {
            usuario.setSenha(usuario.getSenha().hashCode());
            usuario.setNivel(1);
            return usuarioRepository.save(usuario);
        }

        //busca se usuário e senha batem
        public Usuario autenticar(String email, int senha) {
            return usuarioRepository.findByEmailSenha(email, senha).orElse(null);
        }
}
