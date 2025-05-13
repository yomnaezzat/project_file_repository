package com.filerepository.fileservice.service;

import com.filerepository.common.annotation.Audited;
import com.filerepository.common.annotation.LogExecutionTime;
import com.filerepository.common.dto.UserDTO;
import com.filerepository.fileservice.client.RepositoryServiceClient;
import com.filerepository.fileservice.client.UserServiceClient;
import com.filerepository.fileservice.dto.FileUploadResponse;
import com.filerepository.fileservice.exception.FileNotFoundException;
import com.filerepository.fileservice.model.FileEntity;
import com.filerepository.fileservice.model.FileHistory;
import com.filerepository.fileservice.repository.FileHistoryRepository;
import com.filerepository.fileservice.repository.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;
    private final FileHistoryRepository fileHistoryRepository;
    private final FileStorageService fileStorageService;
    private final UserServiceClient userServiceClient;
    private final RepositoryServiceClient repositoryServiceClient;

    public FileService(
            FileRepository fileRepository,
            FileHistoryRepository fileHistoryRepository,
            FileStorageService fileStorageService,
            UserServiceClient userServiceClient,
            RepositoryServiceClient repositoryServiceClient) {
        this.fileRepository = fileRepository;
        this.fileHistoryRepository = fileHistoryRepository;
        this.fileStorageService = fileStorageService;
        this.userServiceClient = userServiceClient;
        this.repositoryServiceClient = repositoryServiceClient;
    }

    @Audited(action = "UPLOAD_FILE", resource = "FILE")
    @LogExecutionTime
    @Transactional
    public FileUploadResponse uploadFile(MultipartFile file, Long repositoryId, Long folderId, Long uploaderId, String description) {
        String storedFilename = fileStorageService.storeFile(file);
        UserDTO uploader = userServiceClient.getUserById(uploaderId);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(storedFilename);
        fileEntity.setOriginalFilename(file.getOriginalFilename());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setDescription(description);
        fileEntity.setSize(file.getSize());
        fileEntity.setStoragePath(storedFilename);
        fileEntity.setRepositoryId(repositoryId);
        fileEntity.setFolderId(folderId);
        fileEntity.setUploaderId(uploaderId);
        fileEntity.setUploaderName(uploader.getFullName());

        FileEntity savedFile = fileRepository.save(fileEntity);

        FileHistory fileHistory = new FileHistory();
        fileHistory.setFileId(savedFile.getId());
        fileHistory.setActionType("UPLOAD");
        fileHistory.setActionDescription("File uploaded");
        fileHistory.setUserId(uploaderId);
        fileHistory.setUsername(uploader.getUsername());

        fileHistoryRepository.save(fileHistory);

        FileUploadResponse response = new FileUploadResponse();
        response.setId(savedFile.getId());
        response.setFilename(savedFile.getFilename());
        response.setOriginalFilename(savedFile.getOriginalFilename());
        response.setContentType(savedFile.getContentType());
        response.setDescription(savedFile.getDescription());
        response.setSize(savedFile.getSize());
        response.setRepositoryId(savedFile.getRepositoryId());
        response.setFolderId(savedFile.getFolderId());
        response.setUploaderId(savedFile.getUploaderId());
        response.setUploaderName(savedFile.getUploaderName());
        response.setUploadedAt(savedFile.getUploadedAt());
        return response;
    }

    @Audited(action = "DOWNLOAD_FILE", resource = "FILE")
    @LogExecutionTime
    public Resource downloadFile(Long fileId, Long userId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));

        UserDTO user = userServiceClient.getUserById(userId);

        FileHistory fileHistory = new FileHistory();
        fileHistory.setFileId(fileId);
        fileHistory.setActionType("DOWNLOAD");
        fileHistory.setActionDescription("File downloaded");
        fileHistory.setUserId(userId);
        fileHistory.setUsername(user.getUsername());

        fileHistoryRepository.save(fileHistory);

        return fileStorageService.loadFileAsResource(fileEntity.getStoragePath());
    }

    @Audited(action = "UPDATE_FILE", resource = "FILE")
    @LogExecutionTime
    @Transactional
    public FileUploadResponse updateFile(Long fileId, MultipartFile file, String description, Long userId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));

        if (file != null && !file.isEmpty()) {
            fileStorageService.deleteFile(fileEntity.getStoragePath());
            String storedFilename = fileStorageService.storeFile(file);
            fileEntity.setFilename(storedFilename);
            fileEntity.setOriginalFilename(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setSize(file.getSize());
            fileEntity.setStoragePath(storedFilename);
        }

        if (description != null) {
            fileEntity.setDescription(description);
        }

        UserDTO user = userServiceClient.getUserById(userId);

        FileHistory fileHistory = new FileHistory();
        fileHistory.setFileId(fileId);
        fileHistory.setActionType("UPDATE");
        fileHistory.setActionDescription("File updated");
        fileHistory.setUserId(userId);
        fileHistory.setUsername(user.getUsername());

        fileHistoryRepository.save(fileHistory);

        FileEntity updatedFile = fileRepository.save(fileEntity);

        FileUploadResponse response = new FileUploadResponse();
        response.setId(updatedFile.getId());
        response.setFilename(updatedFile.getFilename());
        response.setOriginalFilename(updatedFile.getOriginalFilename());
        response.setContentType(updatedFile.getContentType());
        response.setDescription(updatedFile.getDescription());
        response.setSize(updatedFile.getSize());
        response.setRepositoryId(updatedFile.getRepositoryId());
        response.setFolderId(updatedFile.getFolderId());
        response.setUploaderId(updatedFile.getUploaderId());
        response.setUploaderName(updatedFile.getUploaderName());
        response.setUploadedAt(updatedFile.getUploadedAt());
        return response;
    }

    @Audited(action = "DELETE_FILE", resource = "FILE")
    @LogExecutionTime
    @Transactional
    public void deleteFile(Long fileId, Long userId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));

        fileStorageService.deleteFile(fileEntity.getStoragePath());

        UserDTO user = userServiceClient.getUserById(userId);

        FileHistory fileHistory = new FileHistory();
        fileHistory.setFileId(fileId);
        fileHistory.setActionType("DELETE");
        fileHistory.setActionDescription("File deleted");
        fileHistory.setUserId(userId);
        fileHistory.setUsername(user.getUsername());

        fileHistoryRepository.save(fileHistory);

        fileRepository.delete(fileEntity);
    }

    @Audited(action = "GET_FILE", resource = "FILE")
    public FileEntity getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
    }

    @Audited(action = "GET_FILES_BY_REPOSITORY", resource = "FILE")
    public List<FileEntity> getFilesByRepositoryId(Long repositoryId) {
        return fileRepository.findByRepositoryId(repositoryId);
    }

    @Audited(action = "GET_FILES_BY_FOLDER", resource = "FILE")
    public List<FileEntity> getFilesByFolderId(Long folderId) {
        return fileRepository.findByFolderId(folderId);
    }

    @Audited(action = "GET_FILES_BY_REPOSITORY_AND_FOLDER", resource = "FILE")
    public List<FileEntity> getFilesByRepositoryIdAndFolderId(Long repositoryId, Long folderId) {
        return fileRepository.findByRepositoryIdAndFolderId(repositoryId, folderId);
    }

    @Audited(action = "GET_FILES_BY_UPLOADER", resource = "FILE")
    public List<FileEntity> getFilesByUploaderId(Long uploaderId) {
        return fileRepository.findByUploaderId(uploaderId);
    }

    @Audited(action = "SEARCH_FILES", resource = "FILE")
    public List<FileEntity> searchFiles(String keyword) {
        return fileRepository.findByFilenameContainingIgnoreCase(keyword);
    }

    @Audited(action = "GET_FILE_HISTORY", resource = "FILE")
    public List<FileHistory> getFileHistory(Long fileId) {
        return fileHistoryRepository.findByFileIdOrderByTimestampDesc(fileId);
    }
}
