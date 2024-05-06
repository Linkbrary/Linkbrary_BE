package com.linkbrary.domain.directory.service;


import com.linkbrary.common.response.code.ErrorCode;
import com.linkbrary.common.response.exception.handler.UserDirectoryHandler;
import com.linkbrary.domain.directory.dto.CreateUserDirectoryRequestDTO;
import com.linkbrary.domain.directory.dto.UpdateDirectoryNameRequestDTO;
import com.linkbrary.domain.directory.dto.UpdateUserDirectoryLocationRequestDTO;
import com.linkbrary.domain.directory.dto.UserDirectoryResponseDTO;
import com.linkbrary.domain.directory.entity.UserDirectory;
import com.linkbrary.domain.directory.repository.UserDirectoryRepository;
import com.linkbrary.domain.link.repository.UserLinkRepository;
import com.linkbrary.domain.user.entity.Member;
import com.linkbrary.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDirectoryService {

    private final UserDirectoryRepository userDirectoryRepository;
    private final UserService userService;
    private final UserLinkRepository userLinkRepository;

    @Transactional(readOnly = true)
    public String getAllDirectoryNames() {
        Member member = userService.getMemberFromToken();
        List<UserDirectory> rootDirectories = userDirectoryRepository.findByMember(member).stream()
                .filter(directory -> directory.getParentFolder() == null)
                .toList();

        StringBuilder directoryNames = new StringBuilder();
        rootDirectories.forEach(directory -> appendDirectoryNames(directory, directoryNames, ""));
        return directoryNames.toString();
    }

    private void appendDirectoryNames(UserDirectory directory, StringBuilder directoryNames, String indent) {
        directoryNames.append(indent)
                .append("|-- ")
                .append(directory.getDirectoryName())
                .append(" (ID: ")
                .append(directory.getId())
                .append(")\n");

        directory.getChildFolders().forEach(child -> appendDirectoryNames(child, directoryNames, indent + "    "));
    }


    @Transactional
    public UserDirectoryResponseDTO createDirectory(CreateUserDirectoryRequestDTO createUserDirectoryRequestDTO) {
        Member member = userService.getMemberFromToken();
        Long parentFolderId = createUserDirectoryRequestDTO.getParentFolderId();
        UserDirectory userDirectory;
        if (parentFolderId != null) {
            UserDirectory parentDirectory = userDirectoryRepository.findById(parentFolderId)
                    .orElseThrow(() -> new UserDirectoryHandler(ErrorCode.DIRECTORY_NOT_FOUND));

            userDirectory = UserDirectory.builder()
                    .directoryName(createUserDirectoryRequestDTO.getDirectoryName())
                    .parentFolder(parentDirectory)
                    .member(member)
                    .build();
        } else {
            userDirectory = UserDirectory.builder()
                    .directoryName(createUserDirectoryRequestDTO.getDirectoryName())
                    .member(member)
                    .build();
        }
        userDirectoryRepository.save(userDirectory);
        return UserDirectoryResponseDTO.from(userDirectory);
    }

    @Transactional(readOnly = true)
    public UserDirectoryResponseDTO getDirectory(Long id) {
        UserDirectory userDirectory = userDirectoryRepository.findById(id).orElseThrow(() -> new UserDirectoryHandler(ErrorCode.DIRECTORY_NOT_FOUND));
        return UserDirectoryResponseDTO.from(userDirectory);
    }

    @Transactional
    public UserDirectoryResponseDTO updateDirectoryName(UpdateDirectoryNameRequestDTO updateDirectoryNameRequestDTO) {
        Member member = userService.getMemberFromToken();
        UserDirectory directory = userDirectoryRepository.findById(updateDirectoryNameRequestDTO.getId())
                .orElseThrow(() -> new UserDirectoryHandler(ErrorCode.DIRECTORY_NOT_FOUND));
        directory.updateDirectoryName(updateDirectoryNameRequestDTO.getName());
        directory = userDirectoryRepository.save(directory);
        return UserDirectoryResponseDTO.from(directory);
    }

    @Transactional
    public UserDirectoryResponseDTO updateDirectoryLocation(UpdateUserDirectoryLocationRequestDTO updateUserDirectoryLocationRequestDTO) {
        UserDirectory currentDirectory = userDirectoryRepository.findById(updateUserDirectoryLocationRequestDTO.getDirectoryId()).orElseThrow(() -> new UserDirectoryHandler(ErrorCode.DIRECTORY_NOT_FOUND));
        if (updateUserDirectoryLocationRequestDTO.getRequestParentDirectoryId() != null) {
            UserDirectory parentFolder = userDirectoryRepository.findById(updateUserDirectoryLocationRequestDTO.getRequestParentDirectoryId())
                    .orElseThrow(() -> new UserDirectoryHandler(ErrorCode.DIRECTORY_NOT_FOUND));
            currentDirectory.updateParentFolder(parentFolder);
        } else {
            currentDirectory.updateParentFolder(null);
        }
        userDirectoryRepository.save(currentDirectory);
        return UserDirectoryResponseDTO.from(currentDirectory);
    }

    @Transactional
    public void deleteDirectory(Long id) {
        UserDirectory directoryToDelete = userDirectoryRepository.findById(id)
                .orElseThrow(() -> new UserDirectoryHandler(ErrorCode.DIRECTORY_NOT_FOUND));
        UserDirectory parentFolder = directoryToDelete.getParentFolder();
        // 자식 폴더들을 상위 폴더로 이동
        directoryToDelete.getChildFolders().forEach(child -> child.updateParentFolder(parentFolder));
        // 링크들을 상위 폴더로 이동
        directoryToDelete.getUserLinks().forEach(link -> link.updateUserDirectory(parentFolder));
        // 변경 사항 저장
        userDirectoryRepository.saveAll(directoryToDelete.getChildFolders());
        userLinkRepository.saveAll(directoryToDelete.getUserLinks());
        // 디렉토리 삭제
        userDirectoryRepository.delete(directoryToDelete);
    }
}
