package com.searchbar.msazureapi_test;

import android.util.Log;
import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/*
 *
 * Daniel Vento, 2019. MIT license.
 *
 * Thanks to https://developer.android.com/ and https://docs.microsoft.com/en-us/azure/cognitive-services/translator/quickstart-java-translate
 *
 * https://economagically.blogspot.com/2019/05/use-microsoft-cognitive-services.html
 *
 * https://www.linkedin.com/in/ventod/
 *
 */

public class MStranslate {

    private final static String API_KEY = "YOUR_API_KEY_HERE";
    private final static String TRANS_URL = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&to=";
    private static final String TAG = MStranslate.class.getName();


    // Instantiates the OkHttpClient.
    private OkHttpClient client = new OkHttpClient();

    // This function performs a POST request.
    private String Post (String sourceTranslation, String targetLang) throws IOException {

        // request to MS api
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\n\t\"Text\": \"" + sourceTranslation + "\"\n}]");
        Request request = new Request.Builder()
                .url(TRANS_URL + targetLang).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", API_KEY)
                .addHeader("Content-type", "application/json").build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    private static String getTrans(String json_text) throws JSONException {

        JSONObject jsonObject = new JSONArray(json_text).getJSONObject(0).getJSONArray("translations")
                .getJSONObject(0);
        return jsonObject.getString("text");

    }

    public String performTrans(String sourceTranslation, String targetLang) {

        String response = "no translation";

        try {

            response = getTrans(Post(sourceTranslation, targetLang));

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return response;
    }


}
