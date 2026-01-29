package io.github.dbc2201.cineconnectgraphqldemo.graphql;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;

/**
 * GraphQL controller for health check queries.
 */
@Controller
public class HealthQueryController {

    @QueryMapping
    public HealthStatus health() {
        return new HealthStatus(
            "UP",
            "0.0.1-SNAPSHOT",
            Instant.now().toString()
        );
    }

    public record HealthStatus(
        String status,
        String version,
        String timestamp
    ) {}
}
