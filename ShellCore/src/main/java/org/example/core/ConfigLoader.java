package org.example.core;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public class ConfigLoader
{
    private final Path currentHome;
    private final String FILE_NAME = ".toriumshellrc";

    public ConfigLoader() {
        this.currentHome = Path.of(System.getProperty("user.home"));
        Optional<Config> config = load();

        if (config.isEmpty()) {
            System.out.println("Config not found or invalid, creating default...");
            save(new Config());
        }
    }

    public void save(Config config)
    {
        Path path = currentHome.resolve(FILE_NAME);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(path.toFile(), config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save config", e);
        }
    }

    public Optional<Config> load()
    {
        Path path = currentHome.resolve(FILE_NAME);
        File file = path.toFile();

        if (!file.exists()) {
            return Optional.empty();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Config state = objectMapper.readValue(file, Config.class);
            return Optional.of(state);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
