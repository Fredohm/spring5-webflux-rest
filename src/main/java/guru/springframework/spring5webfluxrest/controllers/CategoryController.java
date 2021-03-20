package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    public static final String API_V_1_CATEGORIES = "/api/v1/categories";
    private final CategoryRepository categoryRepository;

    @GetMapping(API_V_1_CATEGORIES)
    Flux<Category> list() {
        return categoryRepository.findAll();
    }

    @GetMapping( API_V_1_CATEGORIES + "/{id}")
    Mono<Category> getById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }
}
