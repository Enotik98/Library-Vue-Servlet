package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.*;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {
    public static JSONObject getJson(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line ;
        while ((line = reader.readLine()) != null){
            stringBuilder.append(line);
        }
        return new JSONObject(stringBuilder.toString());
    }


    public static String getJsonString(Object obj) {
        String json  = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            json = objectMapper.writeValueAsString(obj);
        }catch (JsonProcessingException e ){
            e.printStackTrace();
        }
        return json;
    }

    public static User parsUserJsonNotId(JSONObject json){
        String username = json.getString("username");
        String password = json.getString("password");
        String email = json.getString("email");
        UserRole role = UserRole.valueOf(json.getString("role").toUpperCase());
        String hash = UserService.hasPassword(password);
        return new User(username, password, email, role, hash);
    }


    public static Book parseBookJsonNotId(JSONObject json){
        String name = json.getString("name");
        String author = json.getString("author");
        String genre = json.getString("genre");
        int quantity = json.getInt("quantity");
        return new Book(name, author, genre, quantity);
    }


    public static Order parseOrderJsonNotId(JSONObject json){
//        int user_id = json.getInt("user_id");
        int book_id = json.getInt("book_id");
//        Date date_order = java.sql.Date.valueOf(json.getString("date_order"));
        TicketType ticketType = TicketType.valueOf(json.getString("type").toUpperCase());
        return new Order(book_id, ticketType);
    }
    public static Order parseOrderJsonNotIdWithStatus(JSONObject json){
        int user_id = json.getInt("user_id");
        int book_id = json.getInt("book_id");
        Date date_order = java.sql.Date.valueOf(json.getString("date_order"));
        StatusType statusType = StatusType.valueOf(json.getString("status").toUpperCase());
        TicketType ticketType = TicketType.valueOf(json.getString("type").toUpperCase());
        return new Order(user_id, book_id, date_order, statusType, ticketType);
    }

    public static boolean checkJsonNotEmpty(JSONObject json) {
        for (String key : json.keySet()) {
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                if (!checkJsonNotEmpty((JSONObject) value)) {
                    return false;
                }
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        if (!checkJsonNotEmpty(jsonArray.getJSONObject(i))) {
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

    public static boolean checkIntValueKey(JSONObject jsonObject, String key){
        if (jsonObject.has(key)){
            return jsonObject.optInt(key, Integer.MIN_VALUE) != Integer.MIN_VALUE;
        }
        return false;
    }
}
