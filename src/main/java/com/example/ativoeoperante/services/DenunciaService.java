package com.example.ativoeoperante.services;

import com.example.ativoeoperante.entities.Denuncia;
import com.example.ativoeoperante.repositories.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenunciaService {

    @Autowired
    DenunciaRepository denunciaRepository;

    public List<Denuncia> buscarTodasDenuncias() {
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        return denunciaList;
    }

    public Denuncia buscarPorId(Long id){
        Denuncia denuncia = denunciaRepository.findById(id).orElse(null);
        return denuncia;
    }

    public Denuncia buscarPorTitulo(String titulo){
        Denuncia denuncia = denunciaRepository.findByNome(titulo);
        return denuncia;
    }

    public List<Denuncia> buscarPorKW(String keyword){
        List <Denuncia> denunciaList = denunciaRepository.findByKW(keyword.toUpperCase());
        return denunciaList;
    }

    public Denuncia inserir(Denuncia denuncia){
        try {
            denuncia = denunciaRepository.save(denuncia);
            return denuncia;
        } catch (Exception e) {
            return null; }
    }

    public boolean apagar(Long id){
        if(buscarPorId(id)!=null) {
            denunciaRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
