package kpu.cybersecurity.training.repository;

import kpu.cybersecurity.training.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    boolean existsByUserIdAndEmail(String userId, String email);

    boolean existsByEmail(String email);

    Optional<User> getUserByUserId(String userId);

    Optional<User> findByRefreshTokenAndUserId(String token, String userId);
}
