//package org.example.repo2cloud.config;
//
//import org.example.repo2cloud.mcpTools.DeploymentTools;
//import org.springframework.ai.tool.ToolCallbackProvider;
//import org.springframework.ai.tool.method.MethodToolCallbackProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ToolConfig {
//    @Bean
//    ToolCallbackProvider tools(
//            DeploymentTools deploymentTools
//    ){
//        return MethodToolCallbackProvider.builder()
//                .toolObjects(deploymentTools)
//                .build();
//    }
//}
