package com.filerepository.repositoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FolderRequest {

    @NotBlank(message = "Folder name is required")
    private String name;

    private String description;

    private boolean isMilestone;

    @NotNull(message = "Repository ID is required")
    private Long repositoryId;

    private Long parentFolderId;

    public FolderRequest() {}

    public FolderRequest(String name, String description, boolean isMilestone, Long repositoryId, Long parentFolderId) {
        this.name = name;
        this.description = description;
        this.isMilestone = isMilestone;
        this.repositoryId = repositoryId;
        this.parentFolderId = parentFolderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMilestone() {
        return isMilestone;
    }

    public void setMilestone(boolean milestone) {
        isMilestone = milestone;
    }

    public Long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Long parentFolderId) {
        this.parentFolderId = parentFolderId;
    }
}
