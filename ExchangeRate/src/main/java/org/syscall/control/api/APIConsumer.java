package org.syscall.control.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIConsumer {

    public JsonObject fetch(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Error HTTP: " + connection.getResponseCode());
        }

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            return new Gson().fromJson(reader, JsonObject.class);
        } finally {
            connection.disconnect();
        }
    }
}