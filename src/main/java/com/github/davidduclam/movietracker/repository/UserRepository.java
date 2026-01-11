package com.github.davidduclam.movietracker.repository;

import com.github.davidduclam.movietracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
