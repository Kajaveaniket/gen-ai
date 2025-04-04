package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PromptService {

    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;
    private final Map<String, ChatHistory> chatHistories = new ConcurrentHashMap<>();

    public PromptService(Kernel kernel) throws ServiceNotFoundException {
        this.kernel = kernel;
        this.chatCompletionService = kernel.getService(ChatCompletionService.class);
    }


    public String receivedPromptResponse(String prompt) throws ServiceNotFoundException {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addMessage(AuthorRole.USER, prompt);

        var responses = chatCompletionService.getChatMessageContentsAsync(chatHistory, kernel, null).block();

        if (responses != null && !responses.isEmpty()) {
            return responses.get(0).getContent();
        }
        return "No response received.";
    }

    public String chatWithUser(String chatId, String prompt, boolean random, Double inputTemperature) {
        ChatHistory history = chatHistories.computeIfAbsent(chatId, id -> new ChatHistory());
        history.addMessage(new ChatMessageContent(AuthorRole.USER, prompt));

        System.out.println("Azure OpenAI version: " +
                com.azure.ai.openai.models.ChatRequestAssistantMessage.class.getPackage().getImplementationVersion());
        double temperature = random ? 0.9 : (inputTemperature != null ? inputTemperature : 0.5);
        PromptExecutionSettings settings = PromptExecutionSettings.builder()
                .withTemperature(temperature)
                .build();

        InvocationContext context = InvocationContext.builder()
                .withPromptExecutionSettings(settings)
                .build();

        List<ChatMessageContent<?>> result = chatCompletionService.getChatMessageContentsAsync(
                history,
                kernel,
                context
        ).block();

        if (result == null || result.isEmpty()) {
            throw new RuntimeException("No response from OpenAI");
        }

        String reply = result.get(0).getContent();
        history.addMessage(new ChatMessageContent(AuthorRole.ASSISTANT, reply));
        return reply;
    }
}
