package io.kmruiz.fa;

import net.manub.embeddedkafka.EmbeddedKafka$;
import net.manub.embeddedkafka.EmbeddedKafkaConfigImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import scala.collection.Map$;

@SpringBootApplication
public class FunctionalArchitectureApplication {

    public static void main(String[] args) {
        EmbeddedKafkaConfigImpl cfg = new EmbeddedKafkaConfigImpl(9090, 7072, Map$.MODULE$.empty(), Map$.MODULE$.empty(), Map$.MODULE$.empty());
        EmbeddedKafka$.MODULE$.start(cfg);
        EmbeddedKafka$.MODULE$.createCustomTopic("listings", Map$.MODULE$.empty(), 1, 1, cfg);

        SpringApplication.run(FunctionalArchitectureApplication.class, args);
    }
}
