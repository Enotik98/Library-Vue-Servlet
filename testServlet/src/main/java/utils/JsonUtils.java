package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

public class JsonUtils {
    public static final Logger log = Logger.getLogger(JsonUtils.class);

    public static JSONObject getJson(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return new JSONObject(stringBuilder.toString());
    }


    public static String getJsonString(Object obj) {
        String json = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            json = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.info(e);
        }
        return json;
    }

    public static User parseUserJson(JSONObject json) {
        String username = json.getString("username");
        String password = null, hash = null, type_auth = null;
        if (json.has("password")) {
            password = json.getString("password");
            hash = UserService.hasPassword(password);
        }
        String email = json.getString("email");
        String surname = json.getString("surname");
        String address = json.getString("address");
        if (json.has("type_auth")) {
            type_auth = json.getString("type_auth");
        }
        return new User(username, password, email, hash, surname, address, type_auth);
    }


    public static Book parseBookJson(JSONObject json) {
        String name = json.getString("name");
        String author = json.getString("author");
        String genre = json.getString("genre");
        int quantity = json.getInt("quantity");
        int year = json.getInt("year");
        return new Book(name, author, genre, quantity, year);
    }


    public static Order parseOrderJsonForUpdate(JSONObject json) {
        int book_id = json.getInt("book_id");
        TicketType ticketType = TicketType.valueOf(json.getString("type").toUpperCase());
        return new Order(book_id, ticketType);
    }

    public static Order parseOrderJson(JSONObject json) {
        int user_id = json.getInt("user_id");
        int book_id = json.getInt("book_id");
        Date date_order = java.sql.Date.valueOf(json.getString("date_order"));
        StatusType statusType = StatusType.valueOf(json.getString("status").toUpperCase());
        TicketType ticketType = TicketType.valueOf(json.getString("type").toUpperCase());
        return new Order(user_id, book_id, date_order, statusType, ticketType);
    }


    public static boolean checkJsonForEmptyKeys(JSONObject json) {
        for (String key : json.keySet()) {
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                if (!checkJsonForEmptyKeys((JSONObject) value)) {
                    return false;
                }
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        if (!checkJsonForEmptyKeys(jsonArray.getJSONObject(i))) {
                            return false;
                        }
                    }
                }
            } else if (value == null || value.toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }


    public static boolean isIntValueKey(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.optInt(key, Integer.MIN_VALUE) != Integer.MIN_VALUE;
        }
        return false;
    }
}
