package api.controller.response;

import org.json.JSONArray;

public class ApiResponse {

    public static boolean isRunning;
    public static boolean isWorkOnce;
    public static JSONArray jsonArray;

    public static JSONArray getJsonArray() {
        return jsonArray;
    }

    public static void setJsonArray(JSONArray jsonArray) {
        ApiResponse.jsonArray = jsonArray;
    }
}
