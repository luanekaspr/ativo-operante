package com.example.ativoeoperante.services;

import com.example.ativoeoperante.entities.Denuncia;
import com.example.ativoeoperante.entities.Feedback;
import com.example.ativoeoperante.repositories.DenunciaRepository;
import com.example.ativoeoperante.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenunciaService {

    @Autowired
    DenunciaRepository denunciaRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    public List<Denuncia> buscarTodasDenuncias() {
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        return denunciaList;
    }

    public Denuncia buscarPorId(Long id){
        Denuncia denuncia = denunciaRepository.findById(id).orElse(null);
        return denuncia;
    }

    public Denuncia buscarPorTitulo(String titulo){
        Denuncia denuncia = denunciaRepository.findByTitulo(titulo);
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

    public List<Denuncia> buscarDenunciaPorUsuarioId(Long usuarioId) {
        return denunciaRepository.findByUsuarioId(usuarioId);
    }

    //-------- FEEDBACK ---------

    public Feedback darFeedback(String texto, Long denunciaId) {
        Denuncia denuncia = denunciaRepository.findById(denunciaId).orElseThrow(() -> new RuntimeException("Denúncia não encontrada"));

        //cria o feedback
        Feedback feedback = new Feedback();
        feedback.setTexto(texto);
        feedback.setDenuncia(denuncia);

        return feedbackRepository.save(feedback);
    }


    public Denuncia apagarFeedback(Long denunciaId) {

        Denuncia denuncia = denunciaRepository.findById(denunciaId).orElseThrow();
        denuncia.setFeedback(null);

        return denunciaRepository.save(denuncia);

    }

    public List<Feedback> buscarFeedbacks() {
        List<Feedback> feedbackList = feedbackRepository.findAll();
        return feedbackList;
    }
}
