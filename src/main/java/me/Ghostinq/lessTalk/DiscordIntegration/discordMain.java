package me.Ghostinq.lessTalk.DiscordIntegration;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class discordMain {

    public static int hexToInt(String color) {
        return Integer.parseInt(color.replace("#", ""), 16);
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }

    public static void discordLog(String webHookUrl, String title, String description, String color) {
        try {
            URL url = new URL(webHookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            int colorFromHex = hexToInt(String.valueOf(color));

            String json = "{"
                    + "\"embeds\": [{"
                    + "\"title\": \"" + escape(title) + "\","
                    + "\"description\": \"" + escape(description) + "\","
                    + "\"color\": " + colorFromHex
                    + "}]"
                    + "}";

            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes());
            }
            connection.getInputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
