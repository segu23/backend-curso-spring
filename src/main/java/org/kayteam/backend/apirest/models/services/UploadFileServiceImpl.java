package org.kayteam.backend.apirest.models.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

    @Override
    public Resource loadFile(String fileName) throws MalformedURLException {
        Resource photo;
        if(fileName != null && fileName.length() > 0){
            photo = new UrlResource(getPath(fileName).toUri());
            if (!photo.exists() && !photo.isReadable()) {
                throw new MalformedURLException();
            }
        }else{
            throw new MalformedURLException();
        }

        return photo;
    }

    @Override
    public void saveFile(MultipartFile file, String fileName) throws IOException {
        Path rutaArchivo = getPath(fileName).toAbsolutePath();

        if (!rutaArchivo.getParent().toFile().exists()) rutaArchivo.getParent().toFile().mkdirs();

        Files.copy(file.getInputStream(), rutaArchivo);
    }

    @Override
    public boolean deleteFile(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            File file = getPath(fileName).toFile();
            if (file.exists() && file.canRead()) {
                return file.delete();
            }
        }
        return false;
    }

    @Override
    public Path getPath(String path) {
        return Paths.get("uploads").resolve(path);
    }
}
