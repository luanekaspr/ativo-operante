package com.example.ativoeoperante.repositories;
import com.example.ativoeoperante.entities.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    public Denuncia findByTitulo(String titulo);

    @Query(value = "SELECT * FROM denuncia WHERE upper(den_titulo) LIKE %:keyword%",nativeQuery = true)
    public List<Denuncia> findByKW(@Param("keyword") String keyword);

    //vem lá do tipo pra persistir se já existe antes de cadastrar
    boolean existsByTipoId(Long tipoId);
}
