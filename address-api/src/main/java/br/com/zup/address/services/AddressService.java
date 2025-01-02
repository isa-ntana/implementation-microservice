package br.com.zup.address.services;

import br.com.zup.address.models.Address;
import br.com.zup.address.repositories.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    private static final Logger log = LoggerFactory.getLogger(AddressService.class);

    // Create
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    // Read (Get All)
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    // Read (Get by ID)
    public Optional<Address> getAddressById(String id) {
        return addressRepository.findById(id);
    }

    // Update
    public Address updateAddress(String id, Address updatedAddress) {
        log.info("Updating address with id {}", id + " to updatedAddress");
        return addressRepository.findById(id).map(address -> {
            address.setStreet(updatedAddress.getStreet());
            address.setCity(updatedAddress.getCity());
            address.setState(updatedAddress.getState());
            address.setZipCode(updatedAddress.getZipCode());
            return addressRepository.save(address);
        }).orElseThrow(() -> {
            log.error("Address not found with id: " + id + " to update");
            return new RuntimeException("Address not found with id: " + id);
        });
    }

    // Delete
    public void deleteAddress(String id) {
        log.info("Start delete address service flow");
        if (addressRepository.existsById(id)) {
            log.info("Finish delete address service flow");
            addressRepository.deleteById(id);
        } else {
            log.error("Delete address blocked, because id not found: {}" ,id);
            throw new RuntimeException("Address not found with id " + id);
        }
    }
}