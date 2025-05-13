package com.filerepository.fileservice.dto;

import java.time.LocalDateTime;

public class FileUploadResponse {
    private Long id;
    private String filename;
    private String originalFilename;
    private String contentType;
    private String description;
    private Long size;
    private Long repositoryId;
    private Long folderId;
    private Long uploaderId;
    private String uploaderName;
    private LocalDateTime uploadedAt;

    public FileUploadResponse() {}

    public FileUploadResponse(Long id, String filename, String originalFilename, String contentType,
                              String description, Long size, Long repositoryId, Long folderId,
                              Long uploaderId, String uploaderName, LocalDateTime uploadedAt) {
        this.id = id;
        this.filename = filename;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.description = description;
        this.size = size;
        this.repositoryId = repositoryId;
        this.folderId = folderId;
        this.uploaderId = uploaderId;
        this.uploaderName = uploaderName;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
