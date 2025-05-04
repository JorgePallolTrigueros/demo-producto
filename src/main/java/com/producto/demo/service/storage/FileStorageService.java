package com.producto.demo.service.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    // Carpeta externa en la raíz del proyecto o donde arranque la app
    private final Path root = Paths.get("uploads");

    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize uploads folder!", e);
        }
    }

    /**
     * Guarda el MultipartFile en uploads/{directory}/{directory}-{uuid}.{ext}
     * y devuelve la URL pública: http://{host}:{port}/uploads/{directory}/{filename}
     */
    public String save(String directory, MultipartFile file) {
        try {
            // 1. Crea uploads/{directory}
            Path dirPath = root.resolve(directory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // 2. Genera nombre único
            String extension = getExtension(file.getOriginalFilename());
            String filename = directory + "-" + UUID.randomUUID() + extension;

            // 3. Copia el archivo
            Path destinationFile = dirPath.resolve(filename);
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            // 4. Construye URL pública
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(directory + "/")
                    .path(filename)
                    .toUriString();

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage(), e);
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(root, 1)
                    .filter(path -> !path.equals(root))
                    .map(root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!", e);
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return (dot >= 0) ? filename.substring(dot) : "";
    }
}
