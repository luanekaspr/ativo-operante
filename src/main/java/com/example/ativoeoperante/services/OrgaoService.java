package com.example.ativoeoperante.services;
import com.example.ativoeoperante.entities.Orgao;
import com.example.ativoeoperante.repositories.DenunciaRepository;
import com.example.ativoeoperante.repositories.OrgaoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrgaoService {
        @Autowired
        OrgaoRepository orgaoRepository;

        @Autowired
        DenunciaRepository denunciaRepository;

        public List<Orgao> buscarOrgaos(){
            List<Orgao> orgaoList = orgaoRepository.findAll();
            return orgaoList;
        }

        public Orgao buscarPorId(Long id){
            Orgao orgao = orgaoRepository.findById(id).orElse(null);
            return orgao;
        }

        public Orgao buscarPorNome(String nome){
            Orgao orgao = orgaoRepository.findByNome(nome);
            return orgao;
        }

        public List<Orgao> buscarPorKW(String keyword){
            List <Orgao> orgaoList = orgaoRepository.findByKW(keyword.toUpperCase());
            return orgaoList;
        }

        public Orgao inserir(Orgao orgao) {
            // verificar se já tem
            Orgao existe = orgaoRepository.findByNome(orgao.getNome());
            if (existe != null) {
                return null;
            }
            try {
                return orgaoRepository.save(orgao); //tenta salvar se não tiver
            } catch (Exception e) {
                return null;
            }
        }

        //verifica se tem denúncia vinculada
        public boolean apagar(Long id) {
            Orgao tipo = buscarPorId(id);
            if (tipo == null) {
                return false;
            }

            boolean possuiDenuncias = denunciaRepository.existsByOrgaoId(id);

            if (possuiDenuncias) {
                return false; //não vai apagar porque tem denúncia vinculada
            }

            orgaoRepository.deleteById(id);
            return true;
        }
}
