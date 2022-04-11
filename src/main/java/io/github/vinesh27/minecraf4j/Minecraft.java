package io.github.vinesh27.minecraf4j;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Minecraft {
    private HttpClient client;
    
    public Minecraft() {
        client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    }
    
    public Minecraft(HttpClient client) { this.client = client; }
    
    public HttpClient getClient() { return client; }
    public void setClient(HttpClient client) { this.client = client; }
    
    //https://wiki.vg/Mojang_API#Username_to_UUID
    /**
     * Gets the UUID of a player from their name
     * @param name - The name of the player
     * @return String - The UUID of the player
     */
    public String getUUID(String name)  {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + name))
            .GET()
            .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 204) return null;
            if (response.statusCode() == 200) {
                try {
                    return ((JSONObject) new JSONParser().parse(response.body())).get("id").toString();
                } catch (ParseException e) {
                    return "Parse ERROR [" + response.statusCode() + "]: " + response.body();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
    
}
