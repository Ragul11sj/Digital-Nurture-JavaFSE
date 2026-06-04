package com.ragulsj.eventmanagement.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Instant;

public class NetworkService {

    private static final int TIMEOUT_MS = 3000;

    public String fetchFromUrl(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URI uri = URI.create(urlString);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            conn.setRequestProperty("User-Agent", "CivicPulse-EventManager/1.0");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        result.append(line).append(System.lineSeparator());
                    }
                }
            } else {
                result.append("HTTP Error: ").append(responseCode);
            }
            conn.disconnect();
        } catch (Exception e) {
            result.append("Network error: ").append(e.getMessage());
        }
        return result.toString().trim();
    }

    public boolean checkConnectivity() {
        try {
            URI uri = URI.create("https://www.google.com");
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            conn.connect();
            int code = conn.getResponseCode();
            conn.disconnect();
            return code == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public String getServerTime() {
        return "Server Time (Local): " + Instant.now().toString();
    }
}
