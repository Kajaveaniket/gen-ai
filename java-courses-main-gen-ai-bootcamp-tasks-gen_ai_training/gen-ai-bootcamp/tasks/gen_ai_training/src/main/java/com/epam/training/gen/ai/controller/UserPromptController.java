package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.PromptService;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-prompt")
public class UserPromptController {

    @Autowired
    PromptService promptService;

    @GetMapping
    public String getPrompt(@RequestParam String input) throws ServiceNotFoundException {
        return promptService.receivedPromptResponse(input);
    }
}
