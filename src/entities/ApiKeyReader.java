package entities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class ApiKeyReader {
    public static String keyReader(String fileName) throws FileNotFoundException {
        Reader reader = new FileReader(fileName);
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        String apiKey = jsonObject.get("api_key").getAsString();
        return apiKey;
    }
}
