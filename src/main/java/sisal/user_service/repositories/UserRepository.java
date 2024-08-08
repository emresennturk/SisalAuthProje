package sisal.user_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sisal.user_service.entities.User;


@Repository
public interface UserRepository extends CrudRepository<User, UUID>{

    Optional<User>findByEmail(String email);
}
