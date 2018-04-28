package com.udacity.sandwichclub.utils;

import android.text.TextUtils;
import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    /**
     * Empty Constructor to prevent creating an instance of this class
     */
    private JsonUtils() {
    }

    public static Sandwich parseSandwichJson(String json) {
        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        // Create a Sandwhich object and initialize it to null
        Sandwich sandwich = null;

        try {
            JSONObject baseJson = new JSONObject(json);
            JSONObject name = baseJson.getJSONObject("name");

            String mainName = name.getString("mainName");

            List<String> alsoKnownAsList = getJsonArrayAsList(
                    name.getJSONArray("alsoKnownAs"));

            String placeOfOrigin = baseJson.getString("placeOfOrigin");
            String description = baseJson.getString("description");
            String image = baseJson.getString("image");

            List<String> ingredientsList = getJsonArrayAsList(
                    baseJson.getJSONArray("ingredients"));

            sandwich = new Sandwich(mainName,
                    alsoKnownAsList,
                    placeOfOrigin,
                    description,
                    image,
                    ingredientsList);


        } catch (JSONException e) {
            Log.e("JsonUtils", "Problem parsing the sandwhich JSON results", e);
        }

        return sandwich;
    }

    /**
     * Helper method to extract a String or Strings into an ArrayList
     *
     * @param jsonArray used to extract
     * @return an ArrayList
     */
    public static ArrayList<String> getJsonArrayAsList(JSONArray jsonArray) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
