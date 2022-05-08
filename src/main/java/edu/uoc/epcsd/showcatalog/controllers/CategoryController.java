package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import edu.uoc.epcsd.showcatalog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/category")
public class CategoryController {


    @Autowired //hacemos inyección de dependencias
    private final CategoryService categoryService;

    @Autowired
    private final CategoryRepository categoryRepository;

    /*@GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        log.trace("getAllCategories");

        return categoryRepository.findAll();
    }*/

    // add the code for the missing system operations here


    //metodo GET para obtener todas las categorias
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        log.trace("Obtain all the categories");
        return categoryService.findAllCategories();
    }

    //metodo GET para obtener una categoria mediante su ID como parametro de búsqueda
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id) {
        log.trace("Get a category using" + id);
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }

    //metodo POST para crear una nueva categoria
    @PostMapping()
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        log.trace("Creating a new category: " + category);
        return new ResponseEntity<>(categoryService.saveCategory(category), HttpStatus.CREATED);
    }

    //metodo DELETE para eliminar una categoria mediante su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable("id") Long id) {
        log.trace("Delete a specific category using" + id);
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/consultarCategoria/{nameCategory}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Category>> getCategoryByName(@PathVariable String nameCategory) {
        log.trace("Getting Category by name: " + nameCategory);
        return new ResponseEntity<List<Category>>(categoryRepository.findShowByName(nameCategory), HttpStatus.OK);
    }


}
