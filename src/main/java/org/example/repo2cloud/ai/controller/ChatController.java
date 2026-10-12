package org.example.repo2cloud.ai.controller;

import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import org.example.repo2cloud.ai.ai_services.Repo2CloudAiAssitant;
import org.example.repo2cloud.dto.requestDTO.ChatRequest;
import org.example.repo2cloud.wrapper.ApiRoute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoute.AI)
@RequiredArgsConstructor
public class ChatController {
    private final ChatModel chatModel;
    private final Repo2CloudAiAssitant repo2CloudAiAssitant;

    @GetMapping("/")
    public String chat(
            @RequestBody ChatRequest request
            ){
        return chatModel.chat(
                request.getMsg()
        );
    }

    @GetMapping("/assistant")
    public ResponseEntity<String> chatAssistant(
            @RequestBody ChatRequest request
    ){
        return ResponseEntity.ok(
                repo2CloudAiAssitant.chat(
                        request.getMsg()
                )
        );
    }
}
