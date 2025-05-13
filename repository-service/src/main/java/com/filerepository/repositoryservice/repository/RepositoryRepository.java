package com.filerepository.repositoryservice.repository;

import com.filerepository.repositoryservice.model.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryRepository extends JpaRepository<RepositoryEntity, Long> {

    List<RepositoryEntity> findByOwnerId(Long ownerId);

    @Query("SELECT r FROM RepositoryEntity r JOIN r.supervisorIds s WHERE s = :supervisorId")
    List<RepositoryEntity> findBySupervisorId(@Param("supervisorId") Long supervisorId);

    @Query("SELECT r FROM RepositoryEntity r WHERE r.ownerId = :userId OR :userId MEMBER OF r.supervisorIds")
    List<RepositoryEntity> findByUserIdAsOwnerOrSupervisor(@Param("userId") Long userId);

    boolean existsById(Long id);

    List<RepositoryEntity> findByNameContainingIgnoreCase(String keyword);
}

