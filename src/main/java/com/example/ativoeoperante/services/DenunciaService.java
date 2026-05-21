package com.example.ativoeoperante.services;

import com.example.ativoeoperante.entities.Denuncia;
import com.example.ativoeoperante.entities.Erro;
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


        // Registrar feedback
        public Feedback darFeedback(String texto, Long denunciaId) {
            Denuncia denuncia = denunciaRepository.findById(denunciaId)
                    .orElseThrow(() -> new RuntimeException("Denúncia não encontrada"));

            if (denuncia.getFeedback() != null) {
                throw new RuntimeException("Já existe feedback na denúncia");
            }

            Feedback feedback = new Feedback();
            feedback.setTexto(texto);
            feedback.setDenuncia(denuncia);

            return feedbackRepository.save(feedback);
        }

        // Apagar feedback
        public boolean apagarFeedback(Long denunciaId) {
            Denuncia denuncia = denunciaRepository.findById(denunciaId)
                    .orElseThrow(() -> new RuntimeException("Denúncia não encontrada"));

            Feedback feedback = denuncia.getFeedback();
            if (feedback != null) {

                denuncia.setFeedback(null);
                denunciaRepository.save(denuncia);
                return true;
            } else {
                return false;
            }
        }

        public List<Feedback> buscarFeedbacks() {
            List<Feedback> feedbackList = feedbackRepository.findAll();
            return feedbackList;
        }
}

