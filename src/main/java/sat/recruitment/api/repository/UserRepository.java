package sat.recruitment.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sat.recruitment.api.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
