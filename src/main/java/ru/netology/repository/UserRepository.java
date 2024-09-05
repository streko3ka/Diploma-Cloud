package ru.netology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {
    //поиск по имени юзера
    User findByUsername(String username);
}