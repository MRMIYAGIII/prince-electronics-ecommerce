package com.ecommerce.beta.dto;

import java.util.UUID;

public class ImageDTO {
    private UUID uuid;
    private String fileName;

    // Getters and setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}