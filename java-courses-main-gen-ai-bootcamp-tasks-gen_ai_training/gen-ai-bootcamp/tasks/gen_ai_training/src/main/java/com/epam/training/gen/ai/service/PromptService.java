package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    @Autowired
    private Kernel kernel;

    public String receivedPromptResponse(String prompt) throws ServiceNotFoundException {
        ChatCompletionService chatCompletionService = kernel.getService(ChatCompletionService.class);
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addMessage(AuthorRole.USER, prompt);

        var responses = chatCompletionService.getChatMessageContentsAsync(chatHistory, kernel, null).block();

        if (responses != null && !responses.isEmpty()) {
            return responses.get(0).getContent();
        }
        return "No response received.";
    }
}
