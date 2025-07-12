package elpais.translator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpanishTranslator {

    private static final Logger logger = LoggerFactory.getLogger(SpanishTranslator.class);
    final String API_KEY = System.getenv("TRANSLATOR_API_KEY");

    public List<String> toEnglish(List<String> spanishStrings) {
        List<String> englishStrings = new ArrayList<>();

        String jsonBody = new Gson().toJson(Map.of(
                "from", "es",
                "to", "en",
                "q", spanishStrings
        ));

        Request request = new Request.Builder()
                .url("https://rapid-translate-multi-traduction.p.rapidapi.com/t")
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Content-Type", "application/json")
                .addHeader("X-RapidAPI-Key", API_KEY)
                .addHeader("X-RapidAPI-Host", "rapid-translate-multi-traduction.p.rapidapi.com")
                .build();

        try {
            Response response = new OkHttpClient().newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                JsonArray translated = JsonParser.parseString(response.body().string()).getAsJsonArray();
                for (JsonElement element : translated) {
                    englishStrings.add(element.getAsString());
                }
            } else {
                logger.error("API error: " + response.code());
            }
        } catch (IOException e) {
            logger.error("Request failed: " + e.getMessage(), e);
        }

        return englishStrings;
    }

}
