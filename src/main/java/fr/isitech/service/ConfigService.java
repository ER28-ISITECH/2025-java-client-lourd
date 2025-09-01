package fr.isitech.service;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * ConfigManager class for handling application configuration files
 * Provides methods to read, update, and save configuration properties
 */
public class ConfigService {
    private static final Logger LOGGER = Logger.getLogger(ConfigService.class.getName());

    private final Properties properties;
    private final String configFilePath;
    private final File configFile;

    /**
     * Constructor with default config file name
     */
    public ConfigService() {
        this("config.properties");
    }

    /**
     * Constructor with custom config file path
     * @param configFilePath path to the configuration file
     */
    public ConfigService(String configFilePath) {
        this.configFilePath = configFilePath;
        this.configFile = new File(configFilePath);
        this.properties = new Properties();
        loadConfig();
    }

    /**
     * Load configuration from file
     * Creates the file if it doesn't exist
     */
    private void loadConfig() {
        try {
            if (!configFile.exists()) {
                LOGGER.info("Config file not found. Creating new file: " + configFilePath);
                configFile.createNewFile();
            }

            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
                LOGGER.info("Configuration loaded successfully from: " + configFilePath);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading configuration file: " + configFilePath, e);
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    /**
     * Get property value by key
     * @param key property key
     * @return property value or null if not found
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value by key with default value
     * @param key property key
     * @param defaultValue default value if key not found
     * @return property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Set property value
     * @param key property key
     * @param value property value
     */
    public void setProperty(String key, String value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Property key cannot be null or empty");
        }
        properties.setProperty(key, value != null ? value : "");
        LOGGER.info("Property updated: " + key + " = " + value);
    }

    /**
     * Remove property by key
     * @param key property key to remove
     * @return the previous value associated with key, or null if there was no mapping
     */
    public String removeProperty(String key) {
        Object removed = properties.remove(key);
        if (removed != null) {
            LOGGER.info("Property removed: " + key);
        }
        return (String) removed;
    }

    /**
     * Check if property exists
     * @param key property key
     * @return true if property exists, false otherwise
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    /**
     * Save current properties to file
     * @throws IOException if unable to save the file
     */
    public void saveConfig() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            properties.store(fos, "Application Configuration - Last updated: " +
                    new java.util.Date().toString());
            LOGGER.info("Configuration saved successfully to: " + configFilePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving configuration file: " + configFilePath, e);
            throw e;
        }
    }

    /**
     * Update multiple properties and save to file
     * @param updates Properties object containing key-value pairs to update
     * @throws IOException if unable to save the file
     */
    public void updateAndSave(Properties updates) throws IOException {
        if (updates != null) {
            for (String key : updates.stringPropertyNames()) {
                setProperty(key, updates.getProperty(key));
            }
            saveConfig();
        }
    }

    /**
     * Update a single property and save to file
     * @param key property key
     * @param value property value
     * @throws IOException if unable to save the file
     */
    public void updateAndSave(String key, String value) throws IOException {
        setProperty(key, value);
        saveConfig();
    }

    /**
     * Get all properties as a Properties object
     * @return copy of all properties
     */
    public Properties getAllProperties() {
        Properties copy = new Properties();
        copy.putAll(properties);
        return copy;
    }

    /**
     * Clear all properties
     */
    public void clearAll() {
        properties.clear();
        LOGGER.info("All properties cleared");
    }

    /**
     * Reload configuration from file
     * Discards any unsaved changes
     */
    public void reloadConfig() {
        properties.clear();
        loadConfig();
        LOGGER.info("Configuration reloaded from file");
    }

    /**
     * Get the configuration file path
     * @return configuration file path
     */
    public String getConfigFilePath() {
        return configFilePath;
    }

    /**
     * Example usage and testing
     */
    public static void main(String[] args) {
        try {
            // Create config manager
            ConfigService config = new ConfigService("app.properties");

            // Set some properties
            config.setProperty("app.name", "MyApplication");
            config.setProperty("app.version", "1.0.0");
            config.setProperty("database.host", "localhost");
            config.setProperty("database.port", "3306");

            // Save to file
            config.saveConfig();

            // Read properties
            System.out.println("App Name: " + config.getProperty("app.name"));
            System.out.println("Database URL: " +
                    config.getProperty("database.host") + ":" +
                    config.getProperty("database.port"));

            // Update and save in one operation
            config.updateAndSave("last.updated", java.time.LocalDateTime.now().toString());

            System.out.println("Configuration updated successfully!");

        } catch (IOException e) {
            System.err.println("Error working with configuration: " + e.getMessage());
        }
    }
}