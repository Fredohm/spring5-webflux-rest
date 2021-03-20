package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    @Override
    public void run(String... args) throws Exception {

        if (categoryRepository.count().toProcessor().block() == 0) {
            System.out.println("Loading categories");
            loadCategories();
        }

        if (vendorRepository.count().toProcessor().block() == 0) {
            System.out.println("loading vendors");
            loadVendors();
        }
    }

    private void loadCategories() {

        categoryRepository.save(Category.builder().description("Fruits").build()).toProcessor().block();
        categoryRepository.save(Category.builder().description("Nuts").build()).toProcessor().block();
        categoryRepository.save(Category.builder().description("Breads").build()).toProcessor().block();
        categoryRepository.save(Category.builder().description("Meats").build()).toProcessor().block();
        categoryRepository.save(Category.builder().description("Eggs").build()).toProcessor().block();

        System.out.println("Loaded categories : " + categoryRepository.count().toProcessor().block());
    }

    private void loadVendors() {

        vendorRepository.save(Vendor.builder().firstName("Joe").lastName("Buck").build()).toProcessor().block();
        vendorRepository.save(Vendor.builder().firstName("Michael").lastName("Weston").build()).toProcessor().block();
        vendorRepository.save(Vendor.builder().firstName("Jessie").lastName("Waters").build()).toProcessor().block();
        vendorRepository.save(Vendor.builder().firstName("Bill").lastName("Nershi").build()).toProcessor().block();
        vendorRepository.save(Vendor.builder().firstName("Jimmy").lastName("Buffet").build()).toProcessor().block();

        System.out.println("Loaded vendors : " + vendorRepository.count().toProcessor().block());
    }
}
