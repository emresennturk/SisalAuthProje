package sisal.user_service.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sisal.user_service.entities.Role;
import sisal.user_service.entities.RoleEnum;


@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{ 

    Optional<Role> findByName(RoleEnum name);
    
}
