package com.epam.training.gen.ai.controller;


import com.epam.training.gen.ai.service.PromptService;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPromptControllerTest {

    @InjectMocks
    private UserPromptController userPromptController;

    @Mock
    private PromptService promptService;


    @Test
    void getPromptReturnsResponse() throws ServiceNotFoundException {
        String prompt = "test prompt";
        String expectedResponse = "response";
        when(promptService.receivedPromptResponse(prompt)).thenReturn(expectedResponse);

        String actualResponse = userPromptController.getPrompt(prompt);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getPromptThrowsServiceNotFoundException() throws ServiceNotFoundException {
        String prompt = "test prompt";
        when(promptService.receivedPromptResponse(prompt)).thenThrow(new ServiceNotFoundException("Service not found"));

        assertThrows(ServiceNotFoundException.class, () -> userPromptController.getPrompt(prompt));
    }



}
