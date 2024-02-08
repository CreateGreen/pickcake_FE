package co.pickcake.reservedomain.searchcake.repository;

import co.pickcake.reservedomain.entity.item.Cake;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CakeAdminRepository {

    private final EntityManager em;

    public void save(Cake item) {
        if (item.getId() == null ) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public Cake findById(Long id) {
        return em.find(Cake.class, id);
    }


}
