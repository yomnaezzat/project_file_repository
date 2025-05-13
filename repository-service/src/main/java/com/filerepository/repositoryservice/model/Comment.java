package com.filerepository.repositoryservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "repository_id", nullable = false)
    private Long repositoryId;

    @Column(name = "username")
    private String username;

    @Column(name = "is_supervisor_comment")
    private boolean isSupervisorComment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Comment() {}

    public Comment(Long id, String content, Long fileId, Long userId, Long repositoryId, String username, boolean isSupervisorComment, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.fileId = fileId;
        this.userId = userId;
        this.repositoryId = repositoryId;
        this.username = username;
        this.isSupervisorComment = isSupervisorComment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Manual Builder
    public static class Builder {
        private String content;
        private Long fileId;
        private Long userId;
        private Long repositoryId;
        private String username;
        private boolean isSupervisorComment;

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder fileId(Long fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder repositoryId(Long repositoryId) {
            this.repositoryId = repositoryId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder isSupervisorComment(boolean isSupervisorComment) {
            this.isSupervisorComment = isSupervisorComment;
            return this;
        }

        public Comment build() {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setFileId(fileId);
            comment.setUserId(userId);
            comment.setRepositoryId(repositoryId);
            comment.setUsername(username);
            comment.setSupervisorComment(isSupervisorComment);
            return comment;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getFileId() { return fileId; }
    public void setFileId(Long fileId) { this.fileId = fileId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getRepositoryId() { return repositoryId; }
    public void setRepositoryId(Long repositoryId) { this.repositoryId = repositoryId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public boolean isSupervisorComment() { return isSupervisorComment; }
    public void setSupervisorComment(boolean supervisorComment) { isSupervisorComment = supervisorComment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
