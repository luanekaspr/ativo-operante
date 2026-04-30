package com.example.ativoeoperante.controllers;


import com.example.ativoeoperante.security.JWTTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("acesso")
public class AcessoRestController {
    @PostMapping("/logar")
    public ResponseEntity<Object> autenticar(String email, String senha,String nivel){
        String token= "";
        if (email.equals("admin@pm.br") && senha.equals("123321")){
            token = JWTTokenProvider.createToken(email,senha);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("ACESSO NAO PERMITIDO",HttpStatus.NOT_ACCEPTABLE);
    }

    //@postmapping ("criar")
}
