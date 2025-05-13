package com.filerepository.repositoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentRequest {

    @NotBlank(message = "Comment content is required")
    private String content;

    @NotNull(message = "File ID is required")
    private Long fileId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private boolean isSupervisorComment;

    public CommentRequest() {
    }

    public CommentRequest(String content, Long fileId, Long userId, boolean isSupervisorComment) {
        this.content = content;
        this.fileId = fileId;
        this.userId = userId;
        this.isSupervisorComment = isSupervisorComment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isSupervisorComment() {
        return isSupervisorComment;
    }

    public void setSupervisorComment(boolean supervisorComment) {
        isSupervisorComment = supervisorComment;
    }
}
