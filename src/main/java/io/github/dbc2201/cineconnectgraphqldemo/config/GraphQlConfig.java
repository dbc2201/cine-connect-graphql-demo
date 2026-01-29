package io.github.dbc2201.cineconnectgraphqldemo.config;

import graphql.language.StringValue;
import graphql.scalars.ExtendedScalars;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * GraphQL configuration for custom scalars and runtime wiring.
 */
@Configuration
public class GraphQlConfig {

    /**
     * Custom DateTime scalar that works with java.time.Instant.
     * Serializes to ISO-8601 format and parses from the same.
     */
    private static final GraphQLScalarType INSTANT_SCALAR = GraphQLScalarType.newScalar()
        .name("DateTime")
        .description("ISO-8601 DateTime scalar supporting java.time.Instant")
        .coercing(new Coercing<Instant, String>() {
            @Override
            public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                if (dataFetcherResult instanceof Instant instant) {
                    return DateTimeFormatter.ISO_INSTANT.format(instant);
                }
                throw new CoercingSerializeException(
                    "Expected Instant but was " + dataFetcherResult.getClass().getName());
            }

            @Override
            public Instant parseValue(Object input) throws CoercingParseValueException {
                if (input instanceof String str) {
                    return Instant.parse(str);
                }
                throw new CoercingParseValueException(
                    "Expected String but was " + input.getClass().getName());
            }

            @Override
            public Instant parseLiteral(Object input) throws CoercingParseLiteralException {
                if (input instanceof StringValue stringValue) {
                    return Instant.parse(stringValue.getValue());
                }
                throw new CoercingParseLiteralException(
                    "Expected StringValue but was " + input.getClass().getName());
            }
        })
        .build();

    /**
     * Register custom GraphQL scalar types.
     * DateTime scalar is used for timestamps throughout the schema.
     */
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
            .scalar(INSTANT_SCALAR);
    }
}
