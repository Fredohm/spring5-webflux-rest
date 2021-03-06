package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);

        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("One").lastName("Name").build(),
                        Vendor.builder().firstName("Second").lastName("Name").build()));

                webTestClient.get().uri("/api/v1/vendors/")
                        .exchange()
                        .expectBodyList(Vendor.class)
                        .hasSize(2);
    }

    @Test
    void getById() {
        given(vendorRepository.findById("someId"))
                .willReturn(Mono.just(Vendor.builder().firstName("One").lastName("Name").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/someId")
                .exchange()
                .expectBodyList(Vendor.class);
    }

    @Test
    public void create() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorSaveMono = Mono.just(Vendor.builder().firstName("John").lastName("Doe").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void update() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("John").lastName("Doe").build());

        webTestClient.put()
                .uri("/api/v1/vendors/someId")
                .body(vendorToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

//    @Test
//    void patchWithChanges() {
//        given(vendorRepository.findById(anyString()))
//                .willReturn(Mono.just(Vendor.builder().build()));
//
//        given(vendorRepository.save(any(Vendor.class)))
//                .willReturn(Mono.just(Vendor.builder().build()));
//
//        Mono<Vendor> vendorToPatchMono = Mono.just(Vendor.builder().firstName("John").lastName("Doe").build());
//
//        webTestClient.patch()
//                .uri("/api/v1/vendors/someId")
//                .body(vendorToPatchMono, Vendor.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        verify(vendorRepository).save(any());
//    }
//
//    @Test
//    void patchNoChanges() {
//        given(vendorRepository.findById(anyString()))
//                .willReturn(Mono.just(Vendor.builder().build()));
//
//        given(vendorRepository.save(any(Vendor.class)))
//                .willReturn(Mono.just(Vendor.builder().build()));
//
//        Mono<Vendor> vendorToPatchMono = Mono.just(Vendor.builder().build());
//
//        webTestClient.patch()
//                .uri("/api/v1/vendors/someId")
//                .body(vendorToPatchMono, Vendor.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        verify(vendorRepository, never()).save(any());
//    }
@Test
public void patchVendorWithChanges() {

    given(vendorRepository.findById(anyString()))
            .willReturn(Mono.just(Vendor.builder().firstName("John").lastName("Doe").build()));

    given(vendorRepository.save(any(Vendor.class)))
            .willReturn(Mono.just(Vendor.builder().build()));

    Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().firstName("Jim").lastName("Doe").build());

    webTestClient.patch()
            .uri("/api/v1/vendors/someid")
            .body(vendorMonoToUpdate, Vendor.class)
            .exchange()
            .expectStatus()
            .isOk();

    verify(vendorRepository).save(any());
}

    @Test
    public void patchVendorNoChanges() {

        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").lastName("Doe").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().firstName("Jimmy").lastName("Doe").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/someid")
                .body(vendorMonoToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, never()).save(any());
    }
}