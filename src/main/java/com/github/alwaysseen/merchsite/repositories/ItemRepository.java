package com.github.alwaysseen.merchsite.repositories;

import com.github.alwaysseen.merchsite.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
}
