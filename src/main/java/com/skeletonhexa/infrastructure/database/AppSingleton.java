package com.skeletonhexa.infrastructure.database;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public enum AppSingleton {
    INSTANCE;

    private final Properties properties = new Properties();

    AppSingleton() {
        loadConfigurations("configmysql.properties");
    }

    private void loadConfigurations(String filePath) {
        try (InputStream imputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (imputStream == null) {
                throw new IOException("File not found: " + filePath);
            }
            properties.load(imputStream);
        } catch (IOException e) {
            System.err.println("❌ Error loading configuration: " + e.getMessage());
        }
    }

    public String get(String key) {
        return properties.getProperty(key, "Not found");
    }
}
