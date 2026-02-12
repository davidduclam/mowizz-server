package com.github.davidduclam.movietracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class MovieTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieTrackerApplication.class, args);
    }

    @Bean
    public CommandLineRunner testConnection(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                String catalog = connection.getCatalog();
                System.out.println("✅ SUCCESS: Connected to Supabase!");
                System.out.println("✅ Database Name: " + catalog);
            } catch (Exception e) {
                System.err.println("❌ FAILURE: Could not connect to Supabase.");
                System.err.println("Error details: " + e.getMessage());
            }
        };
    }

}
