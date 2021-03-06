package main.bot;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;


import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by etryfly on 22.06.17.
 */


public class Solution {

    public static void main(String[] args) throws Exception {
        ChatBot chatBot = new ChatBot();

        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/config.properties"));
        Properties properties = new Properties();
        properties.load(fileInputStream);

        UserActor actor = new UserActor(Integer.parseInt(properties.getProperty("userId")), properties.getProperty("accessToken"));

        while (true) {
            Thread.sleep(1000);
            List<Message> messageList = vk.messages().get(actor).execute().getItems();
            for (Message message : messageList) {
                if (!message.isReadState()) {
                    int userId = message.getUserId();
                    System.out.println(message.getBody());
                    System.out.println(vk.messages().send(actor).
                            userId(userId).message(chatBot.sayInReturn(message.getBody())).execute());
                }
            }
        }
    }
}
