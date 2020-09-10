package com.formation.spring_07_sep.repositories;

import com.formation.spring_07_sep.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
