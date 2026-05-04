package com.vivah.vivahconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vivah.vivahconnect.entity.Guest;

public interface GuestRepository extends JpaRepository<Guest, Integer> {

}