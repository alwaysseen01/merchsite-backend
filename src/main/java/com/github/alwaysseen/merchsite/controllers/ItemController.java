package com.github.alwaysseen.merchsite.controllers;

import com.github.alwaysseen.merchsite.entities.Category;
import com.github.alwaysseen.merchsite.entities.Item;
import com.github.alwaysseen.merchsite.repositories.CategoryRepository;
import com.github.alwaysseen.merchsite.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("api/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    record NewItemRequest(
            String name,
            String description,
            Double price,
            Integer quantity,
            Integer category
    ){}

    @GetMapping
    public ResponseEntity<List<Item>> getAll(){
        return new ResponseEntity<>(itemRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/id/{item_id}")
    public ResponseEntity<Item> getById(@PathVariable("item_id") Integer id){
        Item item = itemRepository.findById(id).get();
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/category/{category_id}")
    public ResponseEntity<List<Item>> getByCategory(@PathVariable("category_id") Integer id){
        return new ResponseEntity<>(itemRepository.findByCategory(categoryRepository.findById(id).get()), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Item> create(@RequestBody NewItemRequest request){
        Item item = new Item();
        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setQuantity(request.quantity());
        item.setCategory(categoryRepository.findById(request.category()).get());
        itemRepository.save(item);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{item_id}")
    public ResponseEntity<Item> update(@RequestBody NewItemRequest request,
                                       @PathVariable("item_id") Integer id){
        Item item = itemRepository.findById(id).get();
        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setQuantity(request.quantity());
        item.setCategory(categoryRepository.findById(request.category()).get());
        itemRepository.save(item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{item_id}")
    public ResponseEntity<Item> update(@PathVariable("item_id") Integer id){
        itemRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
