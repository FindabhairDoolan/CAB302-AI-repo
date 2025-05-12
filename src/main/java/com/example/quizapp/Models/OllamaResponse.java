package com.example.quizapp.Models;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * OllamaResponse class allows questions to be sent to LLMs in a synchronous way.
 * This API corresponds to the completion API.
 * Need to choose the LLM that is available locally
 */
public class OllamaResponse {

    static final String LLAMA = "llama3.2";
    private static final int HTTP_OK = 200;
    static final String host = "http://localhost:11434/";
    private String prompt;
    private OllamaAPI ollamaAPI;

    public OllamaResponse(String prompt) {
        this.prompt = prompt;
        ollamaAPI = new OllamaAPI(host);
    }

    /**
     * Uses the Ollama4j API to generate a response to the prompt sent
     * @return String representing the response from LLM as long as HTTP response is 200 ok and response not null
     * @throws OllamaBaseException
     * @throws IOException
     * @throws InterruptedException
     */
    public String ollamaReturnResponse() throws OllamaBaseException, IOException, InterruptedException {
        OllamaAPI ollamaAPI = new OllamaAPI(host);
        ollamaAPI.setRequestTimeoutSeconds(120);


        OllamaResult result =
                ollamaAPI.generate(LLAMA, prompt, false, new OptionsBuilder().build());
        if (result.getHttpStatusCode()==HTTP_OK) {
            if (result != null) {
                return result.getResponse();
            }
        }
        return null;
    }


}


