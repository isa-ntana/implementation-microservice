package br.com.zup.consumer.services;

import br.com.zup.consumer.repositories.ConsumerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.zup.consumer.models.Consumer;

import java.util.List;
import java.util.Optional;

@Service
public class ConsumerService {
    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    private ConsumerRepository consumerRepository;

    // Create
    public Consumer createConsumer(Consumer consumer) {
        return consumerRepository.save(consumer);
    }

    // Read (Get all consumers)
    public List<Consumer> getAllConsumers() {
        return consumerRepository.findAll();
    }

    // Read (Get consumer by ID)
    public Optional<Consumer> getConsumerById(String id) {
        return consumerRepository.findById(id);
    }

    // Update
    public Consumer updateConsumer(String id, Consumer updatedConsumer) {
        log.info("Start update consumer with id {}", id + " to updatedConsumer flow");
        return consumerRepository.findById(id).map(consumer -> {
            consumer.setName(updatedConsumer.getName());
            consumer.setAge(updatedConsumer.getAge());
            consumer.setEmail(updatedConsumer.getEmail());
            log.info("Finish update consumer service flow");
            return consumerRepository.save(consumer);
        }).orElseThrow(() -> {
            log.error("Consumer not found with id: " + id + " to update");
            return new RuntimeException("Consumer not found with id: " + id);
        });
    }

    // Delete
    public void deleteConsumer(String id) {
        log.info("Start delete consumer service flow");
        if (consumerRepository.existsById(id)) {
            log.info("Finish delete consumer service flow");
            consumerRepository.deleteById(id);
        } else {
            log.error("Delete consumer blocked, because id not found: {}" ,id);
            throw new RuntimeException("Consumer not found with id: " + id);
        }
    }
}