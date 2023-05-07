package org.kayteam.backend.apirest.models.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public interface IUploadFileService {

    Resource loadFile(String fileName) throws MalformedURLException;

    void saveFile(MultipartFile file, String fileName) throws IOException;

    boolean deleteFile(String fileName);

    Path getPath(String path);
}
