package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class VendorController {

    public static final String API_V_1_VENDORS = "/api/v1/vendors";
    private final VendorRepository vendorRepository;

    @GetMapping(API_V_1_VENDORS)
    Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping(API_V_1_VENDORS + "/{id}")
    Mono<Vendor> getById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(API_V_1_VENDORS)
    Mono<Void> create(@RequestBody Publisher<Vendor> vendorPublisher) {
        return vendorRepository.saveAll(vendorPublisher).then();
    }

    @PutMapping(API_V_1_VENDORS + "/{id}")
    Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping(API_V_1_VENDORS + "/{id}")
    Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {

        Vendor foundVendor = vendorRepository.findById(id).toProcessor().block();

        boolean changedVendor = false;

        assert foundVendor != null;
        if (!foundVendor.getFirstName().equals(vendor.getFirstName())) {
            foundVendor.setFirstName(vendor.getFirstName());
            changedVendor = true;
        }
        if (!foundVendor.getLastName().equals(vendor.getLastName())) {
            foundVendor.setLastName(vendor.getLastName());
            changedVendor = true;
        }

        return (changedVendor) ? vendorRepository.save(foundVendor) : Mono.just(foundVendor);
    }
}
