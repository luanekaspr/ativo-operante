package com.example.ativoeoperante.controllers;

import com.example.ativoeoperante.entities.Denuncia;
import com.example.ativoeoperante.entities.Erro;
import com.example.ativoeoperante.entities.Orgao;
import com.example.ativoeoperante.entities.Tipo;
import com.example.ativoeoperante.services.DenunciaService;
import com.example.ativoeoperante.services.OrgaoService;
import com.example.ativoeoperante.services.TipoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("apis/cidadao")
public class CidadaoRestController {

    @Autowired
    HttpServletRequest request;
    @Autowired
    DenunciaService denunciaService;
    @Autowired
    TipoService tipoService;
    @Autowired
    OrgaoService orgaoService;

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

    // ================================================= TIPOS ============================================================================

    //listar tipos
    @GetMapping("/tipos-all")
    public ResponseEntity<Object> buscarTipos() {
        List<Tipo> tipoList = tipoService.buscarTipos();
        return ResponseEntity.ok(tipoList);
    }

    // ================================================= ORGÃO ============================================================================

    @GetMapping("/orgao-all")
    public ResponseEntity<Object> buscarorgao() {
        List<Orgao> orgaoList = orgaoService.buscarOrgaos();
        return ResponseEntity.ok(orgaoList);
    }



}
