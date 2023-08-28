package cz.devforce.partnersbootcamp.repository;

import cz.devforce.partnersbootcamp.dto.entity.PersonDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonsRepository extends JpaRepository<PersonDao, Long> {
}
