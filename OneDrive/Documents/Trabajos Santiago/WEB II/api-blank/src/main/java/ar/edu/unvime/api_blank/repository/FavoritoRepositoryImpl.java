package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.model.Favorito;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class  FavoritoRepositoryImpl implements FavoritoRepository{
    //ConcirrentHashMap para garantizar la seguridad en hilos
    private final Map<Long, Favorito> tablaFavoritos = new ConcurrentHashMap<>();
    //Generador de ids autoincrementales
    private final AtomicLong secuenciadorId = new AtomicLong(1);

    @Override
    public Favorito guardar(Favorito favorito){
        if(favorito.getId()==null){
            favorito.setId(secuenciadorId.getAndIncrement());
        }
        tablaFavoritos.put(favorito.getId(), favorito);
        return favorito;
    }
    @Override
    public List<Favorito> buscarTodos() {
        return new ArrayList<>(tablaFavoritos.values());
    }

    @Override
    public Optional<Favorito> buscarPorId(Long id) {
        return Optional.ofNullable(tablaFavoritos.get(id));
    }

    @Override
    public boolean existePorId(Long id) {
        return tablaFavoritos.containsKey(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        tablaFavoritos.remove(id);
    }
}