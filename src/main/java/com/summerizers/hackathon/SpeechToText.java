package com.summerizers.hackathon;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.*;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;


@SuppressWarnings("resource")
public class SpeechToText {

    private static String languageKey = "460fc5562d05422bbcb3035acb980123";
    private static String languageEndpoint = "https://summeriserlanguage.cognitiveservices.azure.com/";

//    public static void main(String[] args) throws FileNotFoundException, ExecutionException, InterruptedException {
//        List<String> recognisedSpeech  = recognitionWithAudioStreamAsync();
//    }

    public List<String> generateTestFromInput() throws InterruptedException, ExecutionException, FileNotFoundException
    {
        Semaphore stopRecognitionSemaphore = new Semaphore(0);
        List<String> recognisedSpeech = new ArrayList<>();
        // Creates an instance of a speech config with specified
        // subscription key and service region. Replace with your own subscription key
        // and service region (e.g., "westus").
        SpeechConfig config = SpeechConfig.fromSubscription("aed5010f80664d8195c49707ddcd6f8c", "northeurope");

        // Create an object that parses the WAV file and implements PullAudioInputStreamCallback to read audio data from the file.
        // Replace with your own audio file name.
        WavStream wavStream = new WavStream(new FileInputStream("src/main/resources/test_call.wav"));
        //WavStream wavStream = new WavStream(new FileInputStream("src/main/resources/test_call2.mp4"));
        // Create a pull audio input stream from the WAV file
        PullAudioInputStream inputStream = PullAudioInputStream.createPullStream(wavStream, wavStream.getFormat());

        // Create a configuration object for the recognizer, to read from the pull audio input stream
        AudioConfig audioInput = AudioConfig.fromStreamInput(inputStream);

        // Creates a speech recognizer using audio stream input.
        SpeechRecognizer recognizer = new SpeechRecognizer(config, audioInput);
        {
            // Subscribes to events.
//            recognizer.recognizing.addEventListener((s, e) -> {
//                System.out.println("RECOGNIZING: Text=" + e.getResult().getText());
 //           });

            recognizer.recognized.addEventListener((s, e) -> {
                if (e.getResult().getReason() == ResultReason.RecognizedSpeech) {
                    System.out.println("RECOGNIZED: Text=" + e.getResult().getText());
                    recognisedSpeech.add(e.getResult().getText());
                }
                else if (e.getResult().getReason() == ResultReason.NoMatch) {
                    System.out.println("NOMATCH: Speech could not be recognized.");
                }
            });

            recognizer.canceled.addEventListener((s, e) -> {
                System.out.println("CANCELED: Reason=" + e.getReason());

                if (e.getReason() == CancellationReason.Error) {
                    System.out.println("CANCELED: ErrorCode=" + e.getErrorCode());
                    System.out.println("CANCELED: ErrorDetails=" + e.getErrorDetails());
                    System.out.println("CANCELED: Did you update the subscription info?");
                }

                stopRecognitionSemaphore.release();
            });

            recognizer.sessionStarted.addEventListener((s, e) -> {
                System.out.println("\nSession started event.");
            });

            recognizer.sessionStopped.addEventListener((s, e) -> {
                System.out.println("\nSession stopped event.");

                // Stops recognition when session stop is detected.
                System.out.println("\nStop recognition.");
                stopRecognitionSemaphore.release();
            });

            // Starts continuous recognition. Uses stopContinuousRecognitionAsync() to stop recognition.
            recognizer.startContinuousRecognitionAsync().get();

            // Waits for completion.
            stopRecognitionSemaphore.acquire();

            // Stops recognition.
            recognizer.stopContinuousRecognitionAsync().get();
        }

        config.close();
        audioInput.close();
        recognizer.close();
        return recognisedSpeech;
    }


}

