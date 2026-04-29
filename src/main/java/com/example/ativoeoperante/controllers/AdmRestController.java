package com.example.ativoeoperante.controllers;

import com.example.ativoeoperante.entities.Denuncia;
import com.example.ativoeoperante.entities.Erro;
import com.example.ativoeoperante.services.DenunciaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("apis/adm")
public class AdmRestController {

    @Autowired
    HttpServletRequest request;
    @Autowired
    DenunciaService denunciaService;

    // ================================================= DENÚNCIAS ============================================================================

    @GetMapping("/todas_denuncias")
    public ResponseEntity<Object> buscarTodasDenuncias() {
     //String token = request.getHeader("Authorization");
      //if (!JWTTokenProvider.verifyToken(token))
          //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Denuncia> denunciaList = denunciaService.buscarTodasDenuncias();
        return ResponseEntity.ok(denunciaList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarDenunciaId(@PathVariable Long id) {
        Denuncia denuncia = denunciaService.buscarPorId(id);
        if(denuncia != null)
            return ResponseEntity.ok(denuncia);
        else
            return ResponseEntity.badRequest().body(new Erro("Denúncia não encontrada!"));
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<Object> buscarDenunciaPorTitulo(@PathVariable String titulo) {
        Denuncia denuncia = denunciaService.buscarPorTitulo(titulo);
        if(denuncia != null)
            return ResponseEntity.ok(denuncia);
        else
            return ResponseEntity.badRequest().body(new Erro("Denúncia não encontrada!"));
    }

    @GetMapping("/kw/{palavraChave}")
    public ResponseEntity<Object> buscarPorPalavraChave(@PathVariable String palavraChave){
        List<Denuncia> denunciaList = denunciaService.buscarPorKW(palavraChave);
        return ResponseEntity.ok(denunciaList);
    }

    @DeleteMapping("{id}") //verificar se ja tem feedback ou nao
    public ResponseEntity<Object> remover(@PathVariable Long id) {
        if(denunciaService.apagar(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ao excluir denúncia!"));
    }
}
