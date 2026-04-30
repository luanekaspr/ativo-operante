package com.example.ativoeoperante.controllers;

import com.example.ativoeoperante.entities.Denuncia;
import com.example.ativoeoperante.entities.Erro;
import com.example.ativoeoperante.entities.Tipo;
import com.example.ativoeoperante.services.DenunciaService;
import com.example.ativoeoperante.services.TipoService;
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

    @GetMapping("/denuncias-all")
    public ResponseEntity<Object> buscarTodasDenuncias() {
     //String token = request.getHeader("Authorization");
      //if (!JWTTokenProvider.verifyToken(token))
          //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Denuncia> denunciaList = denunciaService.buscarTodasDenuncias();
        return ResponseEntity.ok(denunciaList);
    }

    @GetMapping("/denuncias/id/{id}")
    public ResponseEntity<Object> buscarDenunciaId(@PathVariable Long id) {
        Denuncia denuncia = denunciaService.buscarPorId(id);
        if(denuncia != null)
            return ResponseEntity.ok(denuncia);
        else
            return ResponseEntity.badRequest().body(new Erro("Denúncia não encontrada!"));
    }

    @GetMapping("/denuncias/titulo/{titulo}")
    public ResponseEntity<Object> buscarDenunciaPorTitulo(@PathVariable String titulo) {
        Denuncia denuncia = denunciaService.buscarPorTitulo(titulo);
        if(denuncia != null)
            return ResponseEntity.ok(denuncia);
        else
            return ResponseEntity.badRequest().body(new Erro("Denúncia não encontrada!"));
    }

    @GetMapping("/denuncias/kw/{palavraChave}")
    public ResponseEntity<Object> buscarPorPalavraChave(@PathVariable String palavraChave){
        List<Denuncia> denunciaList = denunciaService.buscarPorKW(palavraChave);
        return ResponseEntity.ok(denunciaList);
    }

    @DeleteMapping("/denuncia/deletar/{id}") //verificar se ja tem feedback ou nao
    public ResponseEntity<Object> remover(@PathVariable Long id) {
        if(denunciaService.apagar(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ao excluir denúncia!"));
    }

    // ================================================= TIPO ============================================================================

    @Autowired
    TipoService tipoService;

    @PostMapping("/tipos")
    public ResponseEntity<Object> inserir(@PathVariable Tipo tipo) {
        Tipo tipoinserir = tipoService.inserir(tipo);

        if (tipoinserir != null) {
            return ResponseEntity.ok(tipoinserir);
        } else {
            return ResponseEntity.badRequest().body(new Erro("Erro ao inserir: Tipo já cadastrado ou dados inválidos!"));
        }
    }

    @GetMapping("/tipos-all")
    public ResponseEntity<Object> buscarTipos() {
        List<Tipo> tipoList = tipoService.buscarTipos();
        return ResponseEntity.ok(tipoList);
    }

    @GetMapping("/tipos/id/{id}")
    public ResponseEntity<Object> buscarTipoId(@PathVariable Long id) {
        Tipo tipo = tipoService.buscarPorId(id);
        if(tipo != null)
            return ResponseEntity.ok(tipo);
        else
            return ResponseEntity.badRequest().body(new Erro("Tipo de problema não encontrado ou inexistente!"));
    }

    @GetMapping("/tipos/nome/{nome}")
    public ResponseEntity<Object> buscarTipoPorNome(@PathVariable String nome) {
        Tipo tipo = tipoService.buscarPorNome(nome);
        if(tipo != null)
            return ResponseEntity.ok(tipo);
        else
            return ResponseEntity.badRequest().body(new Erro("Tipo de problema não encontrado ou inexistente!"));
    }

    @GetMapping("/tipos/{palavraChave}")
    public ResponseEntity<Object> buscarTipoPorPalavraChave(@PathVariable String palavraChave){
        List<Tipo> tipoList = tipoService.buscarPorKW(palavraChave);
        return ResponseEntity.ok(tipoList);
    }


    @DeleteMapping("/tipos/deletar/{id}") //verificar se ja tem denuncia com ele ou não
    public ResponseEntity<Object> removertipo(@PathVariable Long id) {
        if(tipoService.apagar(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ao excluir tipo, já existem denúncias vinculadas a ele!"));
    }

}
