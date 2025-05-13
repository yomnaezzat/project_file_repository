package com.filerepository.repositoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class RepositoryRequest {

    @NotBlank(message = "Repository name is required")
    private String name;

    private String description;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    private Set<Long> supervisorIds;

    public RepositoryRequest() {}

    public RepositoryRequest(String name, String description, Long ownerId, Set<Long> supervisorIds) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.supervisorIds = supervisorIds;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Set<Long> getSupervisorIds() {
        return supervisorIds;
    }

    public void setSupervisorIds(Set<Long> supervisorIds) {
        this.supervisorIds = supervisorIds;
    }
}
