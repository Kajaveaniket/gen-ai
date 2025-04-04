package com.epam.training.gen.ai.service;


import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptServiceTest {

    @InjectMocks
    private PromptService promptService;

    @Mock
    private Kernel kernel;

    @Mock
    private ChatCompletionService chatCompletionService;

    @Test
    void receivedPromptResponseReturnsValidResponse() throws ServiceNotFoundException {
        String prompt = "test prompt";
        String expectedResponse = "response";
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addMessage(AuthorRole.USER, prompt);
        List<ChatMessageContent<?>> responses = List.of(new ChatMessageContent(AuthorRole.USER,expectedResponse));

        when(kernel.getService(ChatCompletionService.class)).thenReturn(chatCompletionService);
        when(chatCompletionService.getChatMessageContentsAsync((ChatHistory) any(), any(), any())).thenReturn(Mono.just(responses));

        String actualResponse = promptService.receivedPromptResponse(prompt);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void receivedPromptResponseReturnsNoResponseReceived() throws ServiceNotFoundException {
        String prompt = "test prompt";
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addMessage(AuthorRole.USER, prompt);
        List<ChatMessageContent<?>> responses = Collections.emptyList();

        when(kernel.getService(ChatCompletionService.class)).thenReturn(chatCompletionService);
        when(chatCompletionService.getChatMessageContentsAsync((ChatHistory) any(), any(), any())).thenReturn(Mono.just(responses));

        String actualResponse = promptService.receivedPromptResponse(prompt);

        assertEquals("No response received.", actualResponse);
    }

    @Test
    void receivedPromptResponseThrowsServiceNotFoundException() throws ServiceNotFoundException {
        String prompt = "test prompt";

        when(kernel.getService(ChatCompletionService.class)).thenThrow(new ServiceNotFoundException("Service not found"));

        assertThrows(ServiceNotFoundException.class, () -> promptService.receivedPromptResponse(prompt));
    }



}
