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
    public String getUUID(String name) throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + name))
            .GET()
            .build();
        HttpResponse<String> response;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 204) return null;
        if (response.statusCode() == 200) {
            return ((JSONObject) new JSONParser().parse(response.body())).get("id").toString();
        }
        return "ERROR";
    }
    
    //https://wiki.vg/Mojang_API#UUID_to_Name_History
    /**
     * Gets the name history of a player
     * @param uuid - The UUID of the player
     * @return ArrayList<String> - of names. First element is the current name. Other elements have a | in between.
     */
    public ArrayList<String> getNameHistory(String uuid) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.mojang.com/user/profiles/" + uuid + "/names"))
            .GET()
            .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 204) return null;
            if (response.statusCode() == 200) {
                try {
                    ArrayList<String> names = new ArrayList<>();
                    JSONObject root = (JSONObject) new JSONParser().parse(response.body());
                    for (int i = 0; i < root.size(); i++) {
                        JSONObject entry = (JSONObject) root.get(i);
                        if (entry.containsKey("changedToAt"))
                            names.add(entry.get("name").toString() + "|" + entry.get("changedToAt").toString());
                        else
                            names.add(entry.get("name").toString());
                    }
                    return names;
                } catch (ParseException e) {
                    ArrayList<String> error = new ArrayList<>();
                    error.add("Parse ERROR [" + response.statusCode() + "]: " + response.body().replace("\n", ""));
                    return error;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ArrayList<String> getUUIDs(String... names) {
        return null;
    }
}
