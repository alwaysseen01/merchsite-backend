package com.github.alwaysseen.merchsite.repositories;

import com.github.alwaysseen.merchsite.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}
