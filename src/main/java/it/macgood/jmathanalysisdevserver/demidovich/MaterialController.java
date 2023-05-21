package it.macgood.jmathanalysisdevserver.demidovich;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/materials")
public class MaterialController {

    @GetMapping
    public List<Material> findAll() {
        Path materialsPath = Paths.get("src/main/resources/static/");
        File root = materialsPath.toFile();
        File[] materials = root.listFiles();
        List<Material> response = new ArrayList<>();
        Arrays.stream(materials).forEach(file -> {
            response.add(Material.builder()
                            .name(file.getName())
                            .path(file.getAbsolutePath())
                    .build());
        });

        return response;
    }

}
