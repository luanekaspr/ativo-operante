package com.example.ativoeoperante.repositories;

import com.example.ativoeoperante.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    public Usuario findByEmailAndSenha(String email, int senha);
    public Usuario findByEmail(String email);
}

