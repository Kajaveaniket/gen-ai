package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.KeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KernelConfig {

    @Value("${client-openai-key}")
    private String apiKey;

    @Value("${client-openai-endpoint}")
    private String endpoint;

    @Value("${client-openai-deployment-name}")
    private String deploymentName;

    @Bean
    public Kernel semanticKernel() {
        OpenAIAsyncClient client = new OpenAIClientBuilder()
                .credential(new KeyCredential(apiKey))
                .endpoint(endpoint)
                .buildAsyncClient();

        ChatCompletionService chatCompletionService = OpenAIChatCompletion.builder().withModelId(deploymentName)
                .withModelId(deploymentName)
                .withOpenAIAsyncClient(client)
                .withDeploymentName(deploymentName).build();

        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

}
