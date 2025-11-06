package com.example.backend.kafka.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.common.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Configuration
public class KafkaStartupVerifier {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStartupVerifier.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ApplicationRunner kafkaStartupCheck() {
        return args -> {
            Properties props = new Properties();
            props.put("bootstrap.servers", bootstrapServers);

            logger.info("Attempting to connect to Kafka bootstrap servers: {}", bootstrapServers);

            try (AdminClient admin = AdminClient.create(props)) {
                DescribeClusterResult result = admin.describeCluster();

                String clusterId = result.clusterId().get(5, TimeUnit.SECONDS);
                Node controller = result.controller().get(5, TimeUnit.SECONDS);
                Collection<Node> nodes = result.nodes().get(5, TimeUnit.SECONDS);

                logger.info("Successfully connected to Kafka cluster.");
                logger.info("Cluster ID: {}", clusterId);
                logger.info("Controller: {} ({})", controller.host(), controller.idString());
                logger.info("Broker nodes:");
                for (Node node : nodes) {
                    logger.info(" - Broker ID={} Host={} Port={}", node.id(), node.host(), node.port());
                }

            } catch (Exception ex) {
                logger.error("Unable to connect to Kafka at '{}'", bootstrapServers, ex);
            }
        };
    }
}
