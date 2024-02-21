package com.github.alwaysseen.merchsite.controllers;

import com.github.alwaysseen.merchsite.entities.Photo;
import com.github.alwaysseen.merchsite.repositories.ItemRepository;
import com.github.alwaysseen.merchsite.repositories.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/photo")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoRepository photoRepository;
    private final ItemRepository itemRepository;

    record NewPhotoRequest(
            String url,
            Integer item
    ){}

    @GetMapping
    public ResponseEntity<List<Photo>> getAll(){
        return new ResponseEntity<>(photoRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/id/{photo_id}")
    public ResponseEntity<Photo> getById(@PathVariable("photo_id") Integer id){
        Photo photo = photoRepository.findById(id).get();
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Photo> create(@RequestBody NewPhotoRequest request){
        Photo photo = new Photo();
        photo.setUrl(request.url());
        photo.setItem(itemRepository.findById(request.item()).get());
        photoRepository.save(photo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/update/{photo_id}")
    public ResponseEntity<Photo> update(@RequestBody NewPhotoRequest request,
                                        @PathVariable("photo_id") Integer id){
        Photo photo = photoRepository.findById(id).get();
        photo.setUrl(request.url());
        photo.setItem(itemRepository.findById(request.item()).get());
        photoRepository.save(photo);
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{photo_id}")
    public ResponseEntity<Photo> delete(@PathVariable("photo_id") Integer id){
        photoRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
