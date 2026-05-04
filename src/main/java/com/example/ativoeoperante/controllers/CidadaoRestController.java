package com.example.ativoeoperante.controllers;

import com.example.ativoeoperante.entities.Denuncia;
import com.example.ativoeoperante.entities.Erro;
import com.example.ativoeoperante.entities.Orgao;
import com.example.ativoeoperante.entities.Tipo;
import com.example.ativoeoperante.security.JWTTokenProvider;
import com.example.ativoeoperante.services.DenunciaService;
import com.example.ativoeoperante.services.OrgaoService;
import com.example.ativoeoperante.services.TipoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/denuncias")
    public ResponseEntity<Object> adicionarDenuncia(@RequestBody Denuncia denuncia) {
        denuncia = denunciaService.inserir(denuncia);
        if(denuncia != null)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ao cadastrar a denúncia!"));
    }

    // para consguir testar por enquanto passa o id do usuario direto como param
    @GetMapping("/denuncias/usuario/{usuarioId}")
    public ResponseEntity<Object> buscarDenunciasDoUsuario(@PathVariable Long usuarioId) {
        List<Denuncia> denunciasList = denunciaService.buscarDenunciaPorUsuarioId(usuarioId);

        if (denunciasList != null)
            return ResponseEntity.ok(denunciasList);
        else
            return ResponseEntity.badRequest().body(new Erro("Denúncias não encontradas!"));
    }

    // ainda como teste, aqui pega o id do usuario pelo token da autorização
    @GetMapping("/denuncias/usuario/minhas")
    public ResponseEntity<Object> buscarDenunciasDoUsuario(@RequestHeader("Authorization") String token) {
        String tokenLimpo = token.replace("Bearer ", "");
        if (!JWTTokenProvider.verifyToken(tokenLimpo))
            return new ResponseEntity<>("Token inválido ou expirado", HttpStatus.UNAUTHORIZED);

        String usuarioInfo = JWTTokenProvider.getAllClaimsFromToken(tokenLimpo).getSubject();
        try {
            Long usuarioId = Long.parseLong(usuarioInfo);
            List<Denuncia> minhasDenuncias = denunciaService.buscarDenunciaPorUsuarioId(usuarioId);
            return ResponseEntity.ok(minhasDenuncias);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new Erro("ID do usuário no token é inválido."));
        }
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
