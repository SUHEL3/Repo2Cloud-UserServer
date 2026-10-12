package org.example.repo2cloud.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.observability.api.event.AiServiceCompletedEvent;
import dev.langchain4j.service.AiServices;
import org.example.repo2cloud.ai.ai_services.Repo2CloudAiAssitant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ChatModelConfig {

    @Bean
    public ChatModel chatModel(){
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("qwen2.5-coder:1.5b")
                .maxRetries(2)
                .timeout(Duration.ofMinutes(5))
                .build();
    }

    @Bean
    public Repo2CloudAiAssitant repo2CloudAiAssitant(ChatModel chatModel){
        return AiServices.create(Repo2CloudAiAssitant.class,chatModel);
    }

}
