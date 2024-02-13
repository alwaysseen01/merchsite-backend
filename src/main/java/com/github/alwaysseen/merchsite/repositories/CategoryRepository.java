package com.github.alwaysseen.merchsite.repositories;

import com.github.alwaysseen.merchsite.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
