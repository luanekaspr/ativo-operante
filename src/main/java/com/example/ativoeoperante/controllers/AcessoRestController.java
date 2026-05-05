package com.example.ativoeoperante.controllers;


import com.example.ativoeoperante.entities.Erro;
import com.example.ativoeoperante.entities.Usuario;
import com.example.ativoeoperante.security.JWTTokenProvider;
import com.example.ativoeoperante.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("acesso")
public class AcessoRestController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/logar")
    public ResponseEntity<Object> autenticar(@RequestParam String email, @RequestParam String senha) {
        Usuario usuario = usuarioService.autenticar(email, senha.hashCode());

        if (usuario != null) {
            String nivel = usuario.getNivel() == 1 ? "adm" : "cidadao";
            String token = JWTTokenProvider.createToken(email, nivel);
            return ResponseEntity.ok(token);
        }
        return new ResponseEntity<>("Acesso não permitido", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/criar")
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.cadastrar(usuario);
        if(novoUsuario != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    }

}

