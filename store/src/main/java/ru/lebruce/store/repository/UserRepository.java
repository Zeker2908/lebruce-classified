package ru.lebruce.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lebruce.store.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    void deleteByUserId(Long userId);

    void deleteByEmail(String email);

    User findByEmail(String email);

    User findByUsername(String username);
}
