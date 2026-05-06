package com.example.ativoeoperante.repositories;
import com.example.ativoeoperante.entities.Orgao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgaoRepository extends JpaRepository<Orgao, Long> {

    public Orgao findByNome(String nome);

    @Query(value = "SELECT * FROM orgaos WHERE upper(den_titulo) LIKE %:keyword%",nativeQuery = true)
    public List<Orgao> findByKW(@Param("keyword") String keyword);

}