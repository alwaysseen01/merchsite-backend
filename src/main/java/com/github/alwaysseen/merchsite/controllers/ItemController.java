package com.github.alwaysseen.merchsite.controllers;

import com.github.alwaysseen.merchsite.entities.Item;
import com.github.alwaysseen.merchsite.repositories.CategoryRepository;
import com.github.alwaysseen.merchsite.repositories.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/item")
public class ItemController {
    private ItemRepository itemRepository;
    private CategoryRepository categoryRepository;

    record NewItemRequest(
            String name,
            String desc,
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

    @PostMapping("/create")
    public ResponseEntity<Item> create(@RequestBody NewItemRequest request){
        Item item = new Item();
        item.setName(request.name());
        item.setDesc(request.desc());
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
        item.setDesc(request.desc());
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
