package com.summerizers.hackathon;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SummerisingText {

    public Map<String, String> testToSummary() throws URISyntaxException, InterruptedException {
        String url = "https://languagehk2023.cognitiveservices.azure.com/language/analyze-conversations/jobs?api-version=2022-10-01-preview";
        String requestJson = """
                {
                       	"displayName": "Conversation Task Example",
                       	"analysisInput": {
                       		"conversations": [{
                       			"conversationItems": [{
                       					"text": "Hello, you’re chatting with Rene. How may I help you?",
                       					"id": "1",
                       					"role": "Agent",
                       					"participantId": "Agent_1"
                       				},
                       				{
                       					"text": "Hi, I tried to set up wifi connection for Smart Brew 300 espresso machine, but it didn’t work.",
                       					"id": "2",
                       					"role": "Customer",
                       					"participantId": "Customer_1"
                       				},
                       				{
                       					"text": "I’m sorry to hear that. Let’s see what we can do to fix this issue. Could you please try the following steps for me? First, could you push the wifi connection button, hold for 3 seconds, then let me know if the power light is slowly blinking on and off every second?",
                       					"id": "3",
                       					"role": "Agent",
                       					"participantId": "Agent_1"
                       				},
                       				{
                       					"text": "Yes, I pushed the wifi connection button, and now the power light is slowly blinking.",
                       					"id": "4",
                       					"role": "Customer",
                       					"participantId": "Customer_1"
                       				},
                       				{
                       					"text": "Great. Thank you! Now, please check in your Contoso Coffee app. Does it prompt to ask you to connect with the machine? ",
                       					"id": "5",
                       					"role": "Agent",
                       					"participantId": "Agent_1"
                       				},
                       
                       				{
                       					"text": "No. Nothing happened.",
                       					"id": "6",
                       					"role": "Customer",
                       					"participantId": "Customer_1"
                       				},
                       				{
                       					"text": "Please restart your wi-fi modem to solve the issue with Contoso Coffee app",
                       					"id": "7",
                       					"role": "Agent",
                       					"participantId": "Agent_1"
                       				}
                       			],
                       			"modality": "text",
                       			"id": "conversation1",
                       			"language": "en"
                       		}]
                       	},
                       	"tasks": [{
                       		"taskName": "Conversation Task 1",
                       		"kind": "ConversationalSummarizationTask",
                       		"parameters": {
                       			"summaryAspects": [
                       				"chapterTitle", "narrative", "issue", "resolution"
                       			]
                       		}
                       	}]
                       }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Ocp-Apim-Subscription-Key","47dba23c31b444d3a477dce302c0bac1");
        org.springframework.http.HttpEntity<String> requestEntity = new HttpEntity<>(requestJson,headers);
        RestTemplate restTemplate = new RestTemplate();
        //POST Call
        ResponseEntity response  = restTemplate.exchange(
                new URI(url), HttpMethod.POST, requestEntity,
                String.class);
        String responseHeader = response.getHeaders().get("operation-location").get(0);
        System.out.println("post response : " +responseHeader);
        Thread.sleep(5000);

        //GET call
        org.springframework.http.HttpEntity<String> getRequestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> getResponse  = restTemplate.exchange(
                new URI(responseHeader), HttpMethod.GET, getRequestEntity,
                String.class);
        System.out.println(getResponse.getBody());

        Map<String,String> summaryMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject(getResponse.getBody());
        JSONArray items = jsonObject.getJSONObject("tasks").getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            JSONArray conversations = item.getJSONObject("results").getJSONArray("conversations");
            for (int j = 0; j < conversations.length(); j++) {
                JSONObject conversation = conversations.getJSONObject(j);
                JSONArray summaries = conversation.getJSONArray("summaries");
                for (int k = 0; k < summaries.length(); k++) {
                    JSONObject summary = summaries.getJSONObject(k);
                    String aspect = summary.getString("aspect");
                    String text = summary.getString("text");
                    summaryMap.put(aspect,text);
                }
            }
        }
        return summaryMap;

    }

}
