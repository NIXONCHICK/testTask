package testtask.testtask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import testtask.testtask.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}