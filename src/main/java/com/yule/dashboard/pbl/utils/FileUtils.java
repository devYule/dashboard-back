package com.yule.dashboard.pbl.utils;

import com.yule.dashboard.pbl.exception.ServerException;
import com.yule.dashboard.pbl.utils.enums.FileCategory;
import com.yule.dashboard.pbl.utils.enums.FileKind;
import com.yule.dashboard.pbl.utils.enums.FileType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Slf4j
public class FileUtils {

    @Value("${files.base-path}")
    private String basePath;

    private String filePath;



    public FileUtils(FileCategory category, FileType type, Long id) {
        this.filePath = category.getValue() + "/" + type.getValue() + "/" + id;
    }
    /* --- inner record --- */
    public record FileSaveResult(String savedPath, String movedPath) {

    }

    /* --- logic --- */
    public FileSaveResult save(MultipartFile file, FileKind kind) {
        if (file == null || file.getOriginalFilename() == null) return null;

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        Path savePath = genPath(kind.getValue() + extension);
        Path movedPath = null;
        try {
            if (Files.exists(savePath)) {
                movedPath = Files.move(savePath, genPath(LocalDateTime.now() + extension));
            }

            file.transferTo(savePath);

        } catch (IOException e) {
            log.error("error", e);
            throw new ServerException();
        }
        return new FileSaveResult(savePath.toString(), movedPath == null ? null : movedPath.toString());
    }

    /* --- inner method --- */
    private Path genPath(String suffix) {
        return Paths.get(basePath, filePath, suffix);
    }

}
