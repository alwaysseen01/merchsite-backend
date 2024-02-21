package com.github.alwaysseen.merchsite.repositories;

import com.github.alwaysseen.merchsite.entities.Category;
import com.github.alwaysseen.merchsite.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByCategory(Category category);
}
