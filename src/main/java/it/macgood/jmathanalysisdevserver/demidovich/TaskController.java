package it.macgood.jmathanalysisdevserver.demidovich;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("api/v1/demidovich")
public class TaskController {
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskRepository taskRepository, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }


    @GetMapping
    public List<Task> findAll(
            @RequestParam(defaultValue = "1") Long startId,
            @RequestParam(defaultValue = "3200") Long endId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size
    ) {

        Page<Task> items = taskService.getItemsInRange(startId, endId, page, size);
        return items.getContent();
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable String id) {
        return taskRepository.findById(Long.parseLong(id)).orElseThrow();
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Resource resource = loadFileAsResource(fileName);

        String contentType = determineContentType(resource, request);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            Path filePath = Paths.get("src/main/resources/static/" + fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
    }

    private String determineContentType(Resource resource, HttpServletRequest request) throws IOException {
        return request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    }

}
