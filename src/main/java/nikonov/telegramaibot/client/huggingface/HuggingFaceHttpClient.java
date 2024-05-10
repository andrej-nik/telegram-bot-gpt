package nikonov.telegramaibot.client.huggingface;

import nikonov.telegramaibot.client.huggingface.configuration.HuggingFaceHttpClientConfiguration;
import nikonov.telegramaibot.client.huggingface.dto.HuggingFaceRequest;
import nikonov.telegramaibot.client.huggingface.dto.HuggingFaceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Http клиент к api HuggingFace
 */
@FeignClient(
        name = "hugging-face-client",
        url = "${application.hugging-face.url}",
        configuration = HuggingFaceHttpClientConfiguration.class)
public interface HuggingFaceHttpClient {

    @PostMapping 
    ResponseEntity<ByteArrayResource> getAIImage(@RequestBody HuggingFaceRequest request);
    
    @PostMapping
    ResponseEntity<List<HuggingFaceResponse>> getAIText(@RequestBody HuggingFaceRequest request);
}
