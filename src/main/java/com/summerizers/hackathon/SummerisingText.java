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
                 	"displayName": "Request to Block Debit Card with ABN AMRO BANK",
                 	"analysisInput": {
                 		"conversations": [{
                 			"conversationItems": [{
                 					"text": "Good afternoon, Thanks for calling ABN AMRO Customer Care. You are speaking to Steven. How can I help you?",
                 					"id": "1",
                 					"role": "Agent",
                 					"participantId": "Steven"
                 				},
                 				{
                 					"text": "Hi, Good afternoon. My name is Anja. I am calling because I think I have lost my debit card.",
                 					"id": "2",
                 					"role": "Customer",
                 					"participantId": "Anja"
                 				},
                 				{
                 					"text": "Ohh, that's unfortunate to hear. Have you already blocked the card in your app?",
                 					"id": "3",
                 					"role": "Agent",
                 					"participantId": "Steven"
                 				},
                 				{
                 					"text": "I haven't.",
                 					"id": "4",
                 					"role": "Customer",
                 					"participantId": "Anja"
                 				},
                 				{
                 					"text": "Hey do you use the mobile banking app?",
                 					"id": "5",
                 					"role": "Agent",
                 					"participantId": "Steven"
                 				},
                 				{
                 					"text": "Yes I use it if you can help me where I can find that.",
                 					"id": "6",
                 					"role": "Customer",
                 					"participantId": "Anja"
                 				},
                 				{
                 					"text": "I will. If you can put me on speaker and get to the app then I'll explain to you how to do this.",
                 					"id": "7",
                 					"role": "Agent",
                 					"participantId": "Steven"
                 				},
                 				{
                 					"text": "Great, thank you.",
                 					"id": "8",
                 					"role": "Customer",
                 					"participantId": "Anja"
                 				},
                 				{
                 					"text": "So if you go to the app, at the bottom of the screen there's a self-service button.",
                 					"id": "9",
                 					"role": "Agent",
                 					"participantId": "Steven"
                 				},
                 				{
                 					"text": "Yes.",
                 					"id": "10",
                 					"role": "Customer",
                 					"participantId": "Anja"
                 				},
                 				{
                 					"text": "In the self-service menu you can select Debit cards.The screen will show you the debit cards that you own. You can select the debit card that is lost and click the block button.After blocking, it will ask you if you will also want to replace the debit card. You can select yes, check the address and replace the card right there.",
                 					"id": "11",
                 					"role": "Agent",
                 					"participantId": "Steven"
                 				},
                 				{
                 					"text": "Oh, That's great. Is that all?",
                 					"id": "12",
                 					"role": "Customer",
                 					"participantId": "Anja"
                 				},
                 				{
                 					"text": "That would be all, unless you have any further questions for me.",
                 					"id": "13",
                 					"role": "Agent",
                 					"participantId": "Steven"
                 				},
                 				{
                 					"text": " No, This was exactly what I was looking for. Thank you so much. ",
                 					"id": "14",
                 					"role": "Customer",
                 					"participantId": "Anja"
                 				},
                 				{
                 					"text": "All right. In that case, have a very nice day.",
                 					"id": "15",
                 					"role": "Agent",
                 					"participantId": "Steven"
                 				},
                 				{
                 					"text": " Thank you, Steven. Bye, bye. All right. Bye.",
                 					"id": "16",
                 					"role": "Customer",
                 					"participantId": "Anja"
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
