package com.filerepository.repositoryservice.service;

import com.filerepository.common.annotation.Audited;
import com.filerepository.common.annotation.LogExecutionTime;
import com.filerepository.common.dto.UserDTO;
import com.filerepository.repositoryservice.client.UserServiceClient;
import com.filerepository.repositoryservice.dto.RepositoryRequest;
import com.filerepository.repositoryservice.dto.RepositoryResponse;
import com.filerepository.repositoryservice.dto.SupervisorDTO;
import com.filerepository.repositoryservice.exception.ResourceNotFoundException;
import com.filerepository.repositoryservice.model.RepositoryEntity;
import com.filerepository.repositoryservice.repository.RepositoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    private static final Logger log = LoggerFactory.getLogger(RepositoryService.class);

    private final RepositoryRepository repositoryRepository;
    private final UserServiceClient userServiceClient;

    public RepositoryService(RepositoryRepository repositoryRepository, UserServiceClient userServiceClient) {
        this.repositoryRepository = repositoryRepository;
        this.userServiceClient = userServiceClient;
    }

    @Audited(action = "CREATE_REPOSITORY", resource = "REPOSITORY")
    @LogExecutionTime
    @Transactional
    public RepositoryResponse createRepository(RepositoryRequest request) {
        RepositoryEntity repositoryEntity = RepositoryEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .ownerId(request.getOwnerId())
                .supervisorIds(request.getSupervisorIds() != null ? new HashSet<>(request.getSupervisorIds()) : new HashSet<>())
                .build();

        RepositoryEntity saved = repositoryRepository.save(repositoryEntity);
        return convertToResponse(saved);
    }

    @Audited(action = "GET_REPOSITORY", resource = "REPOSITORY")
    public RepositoryResponse getRepositoryById(Long repositoryId) {
        RepositoryEntity repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Repository not found with id: " + repositoryId));
        return convertToResponse(repository);
    }

    @Audited(action = "UPDATE_REPOSITORY", resource = "REPOSITORY")
    @Transactional
    public RepositoryResponse updateRepository(Long repositoryId, RepositoryRequest request) {
        RepositoryEntity repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Repository not found with id: " + repositoryId));

        repository.setName(request.getName());
        repository.setDescription(request.getDescription());
        if (request.getSupervisorIds() != null) {
            repository.setSupervisorIds(new HashSet<>(request.getSupervisorIds()));
        }

        RepositoryEntity updated = repositoryRepository.save(repository);
        return convertToResponse(updated);
    }

    @Audited(action = "DELETE_REPOSITORY", resource = "REPOSITORY")
    @Transactional
    public void deleteRepository(Long repositoryId) {
        if (!repositoryRepository.existsById(repositoryId)) {
            throw new ResourceNotFoundException("Repository not found with id: " + repositoryId);
        }
        repositoryRepository.deleteById(repositoryId);
    }

    @Audited(action = "GET_USER_REPOSITORIES", resource = "REPOSITORY")
    public List<RepositoryResponse> getRepositoriesByOwnerId(Long ownerId) {
        return repositoryRepository.findByOwnerId(ownerId)
                .stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Audited(action = "GET_SUPERVISOR_REPOSITORIES", resource = "REPOSITORY")
    public List<RepositoryResponse> getRepositoriesBySupervisorId(Long supervisorId) {
        return repositoryRepository.findBySupervisorId(supervisorId)
                .stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Audited(action = "GET_USER_ALL_REPOSITORIES", resource = "REPOSITORY")
    public List<RepositoryResponse> getRepositoriesByUserIdAsOwnerOrSupervisor(Long userId) {
        return repositoryRepository.findByUserIdAsOwnerOrSupervisor(userId)
                .stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Audited(action = "SEARCH_REPOSITORIES", resource = "REPOSITORY")
    public List<RepositoryResponse> searchRepositories(String keyword) {
        return repositoryRepository.findByNameContainingIgnoreCase(keyword)
                .stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Audited(action = "CHECK_REPOSITORY_EXISTS", resource = "REPOSITORY")
    public boolean checkRepositoryExists(Long repositoryId) {
        return repositoryRepository.existsById(repositoryId);
    }

    private RepositoryResponse convertToResponse(RepositoryEntity entity) {
        UserDTO owner = userServiceClient.getUserById(entity.getOwnerId());

        Set<SupervisorDTO> supervisors = new HashSet<>();
        if (entity.getSupervisorIds() != null) {
            for (Long supervisorId : entity.getSupervisorIds()) {
                try {
                    UserDTO user = userServiceClient.getUserById(supervisorId);
                    SupervisorDTO dto = SupervisorDTO.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .fullName(user.getFullName())
                            .build();
                    supervisors.add(dto);
                } catch (Exception e) {
                    log.error("Could not retrieve supervisor details for id: {}", supervisorId, e);
                }
            }
        }

        return RepositoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .ownerId(entity.getOwnerId())
                .ownerName(owner.getFullName())
                .supervisors(supervisors)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
