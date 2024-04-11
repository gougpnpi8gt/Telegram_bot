package com.projectsvadim.vadimbot.repository;

import com.projectsvadim.vadimbot.enity.User.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DetailsRepo extends JpaRepository<UserDetails, UUID> {
}
