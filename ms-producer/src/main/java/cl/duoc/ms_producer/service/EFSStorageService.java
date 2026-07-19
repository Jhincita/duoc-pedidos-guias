package cl.duoc.ms_producer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class EFSStorageService {

    @Value("${efs.temp-dir}")
    private String tempDir;

    public String saveTempFile(byte[] content, String extension) throws IOException {
        String fileName = UUID.randomUUID() + extension;
        Path path = Paths.get(tempDir, fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, content);
        return path.toString();
    }

    public byte[] readTempFile(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    public void deleteTempFile(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
    }
}