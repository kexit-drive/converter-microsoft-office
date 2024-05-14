package kpi.zaranik.kexitdrive.convertermicrosoftoffice.converter;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RestController
public class ConvertToPdfController {

    @Autowired
    WebClient webClient;

    @PostMapping("convert")
    public ResponseEntity<Resource> getFileConvertedToPdf(@RequestPart MultipartFile file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        boolean singlePageSheets = Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        log.info("singlePageSheets = {}", singlePageSheets);

        builder
            .part("singlePageSheets", singlePageSheets);

        builder
            .part("file", file.getResource());


        MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();

        Resource converted = webClient.post()
            .uri("forms/libreoffice/convert")
            .bodyValue(multipartBody)
            .retrieve()
            .bodyToMono(Resource.class)
            .block();

        ContentDisposition contentDisposition = ContentDisposition.inline().build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .headers(headers)
            .body(converted);
    }

}
