package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(API_V_1_CATEGORIES)
    Mono<Void> create(@RequestBody Publisher<Category> categoryPublisher) {
        return categoryRepository.saveAll(categoryPublisher).then();
    }

    @PutMapping(API_V_1_CATEGORIES + "/{id}")
    Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping(API_V_1_CATEGORIES + "/{id}")
    Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {
        Category foundCategory = categoryRepository.findById(id).toProcessor().block();

        assert foundCategory != null;
        if (!foundCategory.getDescription().equals(category.getDescription())) {
            foundCategory.setDescription(category.getDescription());
            return categoryRepository.save(foundCategory);
        }
        return Mono.just(foundCategory);
    }
}
