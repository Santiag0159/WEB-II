package ar.edu.unvime.api_blank.persistence.favorito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritoJpaRepository extends JpaRepository<FavoritoEntity, Long> {

    List<FavoritoEntity> findByListaId(Long listaId);

    @Modifying
    @Query("UPDATE FavoritoEntity f SET f.lista.id = :destinoId WHERE f.lista.id = :origenId")
    int reasignarFavoritosDeLista(@Param("origenId") Long origenId, @Param("destinoId") Long destinoId);
}