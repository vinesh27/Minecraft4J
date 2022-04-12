package io.github.vinesh27.minecraf4j;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class Example {
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        Minecraft minecraft = new Minecraft();
        String uuid = minecraft.getUUID("name");
        ArrayList<String> nameHistory = minecraft.getNameHistory("uuid");
    }
}
