package com.epam.training.gen.ai.service;


import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptServiceTest {

    private PromptService promptService;
    private Kernel kernel;
    private ChatCompletionService chatCompletionService;

    @BeforeEach
    void setUp() throws ServiceNotFoundException {
        kernel = mock(Kernel.class);
        chatCompletionService = mock(ChatCompletionService.class);
        when(kernel.getService(ChatCompletionService.class)).thenReturn(chatCompletionService);
        promptService = new PromptService(kernel);
    }

    @Test
    void receivedPromptResponseReturnsValidResponse() throws ServiceNotFoundException {
        String prompt = "test prompt";
        String expectedResponse = "response";
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addMessage(AuthorRole.USER, prompt);
        List<ChatMessageContent<?>> responses = List.of(new ChatMessageContent(AuthorRole.ASSISTANT, expectedResponse));


        when(chatCompletionService.getChatMessageContentsAsync((ChatHistory) any(), any(), any()))
                .thenReturn(Mono.just(responses));

        String actualResponse = promptService.receivedPromptResponse(prompt);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void receivedPromptResponseReturnsNoResponseReceived() throws ServiceNotFoundException {
        String prompt = "test prompt";
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addMessage(AuthorRole.USER, prompt);
        List<ChatMessageContent<?>> responses = Collections.emptyList();

        when(chatCompletionService.getChatMessageContentsAsync((ChatHistory) any(), any(), any())).thenReturn(Mono.just(responses));

        String actualResponse = promptService.receivedPromptResponse(prompt);

        assertEquals("No response received.", actualResponse);
    }

    @Test
    void chatWithUserReturnsValidResponse() {
        String chatId = "chat1";
        String prompt = "test prompt";
        String expectedResponse = "response";
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addMessage(AuthorRole.USER, prompt);
        List<ChatMessageContent<?>> responses = List.of(new ChatMessageContent(AuthorRole.ASSISTANT, expectedResponse));

        when(chatCompletionService.getChatMessageContentsAsync((ChatHistory) any(), any(), any())).thenReturn(Mono.just(responses));

        String actualResponse = promptService.chatWithUser(chatId, prompt, false, 0.5);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void chatWithUserThrowsRuntimeExceptionWhenNoResponse() {
        String chatId = "chat1";
        String prompt = "test prompt";
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addMessage(AuthorRole.USER, prompt);
        List<ChatMessageContent<?>> responses = Collections.emptyList();

        when(chatCompletionService.getChatMessageContentsAsync((ChatHistory) any(), any(), any())).thenReturn(Mono.just(responses));

        assertThrows(RuntimeException.class, () -> promptService.chatWithUser(chatId, prompt, false, 0.5));
    }



}
