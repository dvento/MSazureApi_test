package com.searchbar.msazureapi_test;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

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

public class MainActivity extends AppCompatActivity {

    private Button transBtn;
    private TextView transResult;
    private EditText sourceText;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private Map languages;
    private String selectedLang;

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceText = findViewById(R.id.sourceText);
        transResult = findViewById(R.id.translationResult);
        transBtn = findViewById(R.id.translateBtn);
        spinner = findViewById(R.id.spinner);

        // set up spinner
        loadJSON();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // get selected language code from languages Map
                selectedLang = languages.get(parent.getSelectedItem().toString()).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        transBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get text to translate
                String textToTrans = sourceText.getText().toString().trim();

                // call the API
                try {
                    new TransAsynTask().execute(textToTrans, selectedLang);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

    }

    private class TransAsynTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            MStranslate mStranslate = new MStranslate();
            String translationResult = mStranslate.performTrans(strings[0], strings[1]);
            return translationResult;
        }

        @Override
        protected void onPostExecute(String s) {
            transResult.setText("Translation: " + s);
            super.onPostExecute(s);
        }

    }

    private void loadJSON() {

        // get json resource file
        InputStream is = getResources().openRawResource(R.raw.languages_list);

        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();

        while(scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }

        parseJson(builder.toString());

    }

    private void parseJson(String s) {

        try {
            //JSONObject jsonObject = new JSONObject(s);

            Gson gson = new Gson();
            languages = gson.fromJson(s,Map.class);

            // initialize spinner arraylist
            ArrayList<String> spinnerArray = new ArrayList<>();

            for (Object key : languages.keySet()) {

                // add each language to spinner's arraylist
                spinnerArray.add(key.toString());

            }

            // set up spinner
            spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
            spinner.setAdapter(spinnerAdapter);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }


    }

}

