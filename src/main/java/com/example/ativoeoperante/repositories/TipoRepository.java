package com.example.ativoeoperante.repositories;
import com.example.ativoeoperante.entities.Denuncia;
import com.example.ativoeoperante.entities.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoRepository extends JpaRepository<Tipo, Long> {

    public Tipo findByNome(String nome);

    @Query(value = "SELECT * FROM tipo WHERE upper(tip_nome) LIKE %:keyword%",nativeQuery = true)
    public List<Tipo> findByKW(@Param("keyword") String keyword);

}
