package com.summerizers.hackathon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonFormatter {
    public static void main(String[] args) {
        String jsonString = """ 
                {"jobId":"944e2d89-f21a-4a33-8b37-5a22f534f189","lastUpdatedDateTime":"2023-06-21T13:36:56Z","createdDateTime":"2023-06-21T13:36:54Z","expirationDateTime":"2023-06-22T13:36:54Z","status":"succeeded","errors":[],"displayName":"Conversation Task Example","tasks":{"completed":1,"failed":0,"inProgress":0,"total":1,"items":[{"kind":"conversationalSummarizationResults","taskName":"Conversation Task 1","lastUpdateDateTime":"2023-06-21T13:36:56.1771136Z","status":"succeeded","results":{"conversations":[{"summaries":[{"aspect":"chapterTitle","text":"Troubleshooting SmartBrew 300 Espresso Machine WiFi Connection","contexts":[{"conversationItemId":"1","offset":0,"length":53},{"conversationItemId":"2","offset":0,"length":94},{"conversationItemId":"3","offset":0,"length":266},{"conversationItemId":"4","offset":0,"length":85},{"conversationItemId":"5","offset":0,"length":119},{"conversationItemId":"6","offset":0,"length":21},{"conversationItemId":"7","offset":0,"length":74}]},{"aspect":"narrative","text":"Customer_1 tried to set up wifi connection for Smart Brew 300 espresso machine, but it didn't work. Agent_1 suggested that the customer push the wifi connection button, hold for 3 seconds, and let him know if the power light is slowly blinking. The customer then checked in their Contoso Coffee app and it did not prompt to ask them to connect with the machine.","contexts":[{"conversationItemId":"1","offset":0,"length":53},{"conversationItemId":"2","offset":0,"length":94},{"conversationItemId":"3","offset":0,"length":266},{"conversationItemId":"4","offset":0,"length":85},{"conversationItemId":"5","offset":0,"length":119},{"conversationItemId":"6","offset":0,"length":21},{"conversationItemId":"7","offset":0,"length":74}]},{"aspect":"issue","text":"Customer unable to set up wifi connection for Smart Brew 300 espresso machine."},{"aspect":"resolution","text":"The agent instructed the customer to check the power light on the wifi connection button and the Contoso Coffee app, but no prompts were provided. The agent then advised the customer to restart their Wi-Fi modem to solve the issue."}],"id":"conversation1","warnings":[]}],"errors":[],"modelVersion":"latest"}}]}}
                """;
        Map<String,String> summaryMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject(jsonString);
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
    }
}
