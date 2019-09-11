package br.com.emmmanuelneri.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KafkaProducerVerticle extends AbstractVerticle {

    private static final String TOPIC = "Topic";

    @Override
    public void start() {
        final Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        final KafkaProducer<String, String> producer = KafkaProducer.create(vertx, config);

        this.vertx.setPeriodic(10_000, handler -> {
            final KafkaProducerRecord<String, String> kafkaProducerRecord = KafkaProducerRecord.
                    create(TOPIC, UUID.randomUUID().toString(), "Kafka message");

            producer.send(kafkaProducerRecord);
        });
    }
}
