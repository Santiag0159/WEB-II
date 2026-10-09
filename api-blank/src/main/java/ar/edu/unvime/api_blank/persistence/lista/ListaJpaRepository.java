package ar.edu.unvime.api_blank.persistence.lista;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaJpaRepository extends JpaRepository<ListaEntity, Long> {
}