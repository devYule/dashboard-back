package com.yule.dashboard.pbl.utils;

import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.exception.ServerException;
import com.yule.dashboard.pbl.utils.enums.FileCategory;
import com.yule.dashboard.pbl.utils.enums.FileKind;
import com.yule.dashboard.pbl.utils.enums.FileType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Slf4j
@Component
public class FileUtils {

    @Value("${files.base-path}")
    private String basePath;


    /* --- inner record --- */
    public record FileSaveResult(String savedPath, String movedPath) {

    }

    /* --- logic --- */

    public FileSaveResult save(MultipartFile file, FileKind kind, FileCategory category, FileType type, Long identity) {
        if (file == null || file.getOriginalFilename() == null) return null;


        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String suffix = category.getValue() + "/" + type.getValue() + "/" + identity + "/";
        String savePathSuffix = suffix + kind.getValue() + extension;
        Path savePath = genPath(savePathSuffix);
        Path movedPath = null;
        try {
            if (Files.exists(savePath)) {
                movedPath = remove(savePath, suffix, extension);

            }
            if (Files.notExists(savePath.getParent())) {
                Files.createDirectories(savePath.getParent());
            }
            file.transferTo(savePath);

        } catch (IOException e) {
            log.error("error", e);
            throw new ServerException();
        }
        return new FileSaveResult(savePathSuffix, movedPath == null ? null : movedPath.toString());
    }

    public FileSaveResult remove(String userPicPath) {
        if(userPicPath == null) throw new ClientException(ExceptionCause.FILE_IS_NOT_EXISTS);
        String suffix = userPicPath.substring(0, userPicPath.lastIndexOf(FileKind.PROFILE_PIC.getValue()));
        String extension = userPicPath.substring(userPicPath.lastIndexOf("."));
        Path targetPath = genPath(userPicPath);
        return new FileSaveResult(null, remove(targetPath, suffix, extension).toString());
    }

    private Path remove(Path targetPath, String suffix, String extension) {
        String movePathSuffix = suffix + LocalDateTime.now() + extension;
        try {
            return Files.move(targetPath, genPath(movePathSuffix));
        } catch (IOException e) {
            throw new ServerException(e);
        }

    }


    /* --- inner method --- */
    private Path genPath(String suffix) {
        return Path.of(basePath, suffix);
//        return Paths.get(basePath, suffix);
    }

}
