package com.summerizers.hackathon;

import com.microsoft.applicationinsights.core.dependencies.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import java.util.concurrent.ExecutionException;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
public class Controller {

    @CrossOrigin
    @GetMapping(value = "/speechtotext")
    public String getSpeechToTextUsingInputFile() throws FileNotFoundException, ExecutionException, InterruptedException {
        SpeechToText speechToText = new SpeechToText();
        Gson gson = new Gson();
        return gson.toJson(speechToText.generateTestFromInput());
    }

@CrossOrigin
@GetMapping(value = "/texttosummary")
    public String getTextToSummary() throws URISyntaxException, InterruptedException {
    SummerisingText summerisingText = new SummerisingText();
    Gson gson = new Gson();
    return gson.toJson(summerisingText.testToSummary());
}

}
