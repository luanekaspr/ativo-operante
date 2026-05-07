package com.example.ativoeoperante.controllers;


import com.example.ativoeoperante.entities.Erro;
import com.example.ativoeoperante.entities.Usuario;
import com.example.ativoeoperante.repositories.UsuarioRepository;
import com.example.ativoeoperante.security.JWTTokenProvider;
import com.example.ativoeoperante.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("apis/acesso")
public class AcessoRestController {
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/autenticar")
    public ResponseEntity<Object> autenticarUser(String login, int senha)
    {
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(login);
        String token="";

        if(usuarioEncontrado != null) {
            if (usuarioEncontrado.getSenha() == senha) {
                token = JWTTokenProvider.getToken(login, ""+usuarioEncontrado.getNivel());
                return new ResponseEntity<>(token, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("ACESSO NAO PERMITIDO", HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/cadastrar-cidadao")
    public ResponseEntity<Object> cadastrarNovoCidadao(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.inserirUsuario(usuario);
        if (novoUsuario != null) {
            return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new Erro("Este e-mail já está cadastrado no sistema."), HttpStatus.BAD_REQUEST);
        }
    }
}