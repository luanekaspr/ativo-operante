package com.example.ativoeoperante.controllers;

import com.example.ativoeoperante.entities.Denuncia;
import com.example.ativoeoperante.entities.Erro;
import com.example.ativoeoperante.services.DenunciaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("apis/cidadao")
public class CidadaoRestController {

    @Autowired
    HttpServletRequest request;
    @Autowired
    DenunciaService denunciaService;

    // ================================================= DENÚNCIAS ============================================================================

    // Adicionar denúncia
    @PostMapping
    public ResponseEntity<Object> adicionarDenuncia(@RequestBody Denuncia denuncia) {
        denuncia = denunciaService.inserir(denuncia);
        if(denuncia != null)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ao cadastrar a denúncia!"));
    }

}
