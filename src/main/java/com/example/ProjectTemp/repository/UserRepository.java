package com.example.ProjectTemp.repository;

import com.example.ProjectTemp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);

    @Query("SELECT u.followers FROM User u WHERE u.id = ?1")
    Set<User> findFollowersByUserId(Long id);

    @Query("SELECT u.following FROM User u WHERE u.id = ?1")
    Set<User> findFollowingByUserId(Long id);


}