package com.example.ativoeoperante.services;
import com.example.ativoeoperante.entities.Tipo;
import com.example.ativoeoperante.repositories.DenunciaRepository;
import com.example.ativoeoperante.repositories.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoService {

    @Autowired
    DenunciaRepository denunciaRepository;

    @Autowired
    TipoRepository tipoRepository;

    public List<Tipo>buscarTipos(){
        List<Tipo> tipoList = tipoRepository.findAll();
        return tipoList;
    }

    public Tipo buscarPorId(Long id){
        Tipo tipo = tipoRepository.findById(id).orElse(null);
        return tipo;
    }

    public Tipo buscarPorNome(String nome){
        Tipo tipo = tipoRepository.findByNome(nome);
        return tipo;
    }

    public List<Tipo> buscarPorKW(String keyword){
        List <Tipo> tipoList = tipoRepository.findByKW(keyword.toUpperCase());
        return tipoList;
    }

    public Tipo inserir(Tipo tipo) {
        // verificar se já tem
        Tipo existe = tipoRepository.findByNome(tipo.getNome());
        if (existe != null) {
            return null;
        }
        try {
            return tipoRepository.save(tipo); //tenta salvar se não tiver
        } catch (Exception e) {
            return null;
        }
    }

    //verifica se tem denúncia vinculada
    public boolean apagar(Long id) {
        Tipo tipo = buscarPorId(id);
        if (tipo == null) {
            return false;
        }

        boolean possuiDenuncias = denunciaRepository.existsByTipoId(id);

        if (possuiDenuncias) {
            return false; //não vai apagar porque tem denúncia vinculada
        }

        tipoRepository.deleteById(id);
        return true;
    }
}
