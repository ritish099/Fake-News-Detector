package com.example.fnd;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cc.cloudist.acplibrary.ACProgressPie;

public class MainActivity extends AppCompatActivity {

    String URL_POST="https://cosinex.pythonanywhere.com/?text=";
    TextInputEditText news_text;
    MaterialButton check;
    String fake,real;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news_text=findViewById(R.id.news_text);
        check=findViewById(R.id.check);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL_POST=URL_POST+news_text.getText().toString().trim();
                send_data s1 = new send_data();
                s1.execute();
            }
        });
    }

    private class send_data extends AsyncTask<Void, Void, Void>
    {
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute()
        {
            dialog = new ACProgressFlower.Builder(MainActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("Authenticating")
                    .fadeColor(Color.DKGRAY).build();
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POST, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(String response)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        fake=jsonObject.getString("Fake");
                        real=jsonObject.getString("Real");
                        Log.d("TAG", "onResponse: " +fake);
                        Log.d("TAG", "onResponse: " +real);
                        Log.d("TAG", "onResponse: " +response);
                        onPostExecute(fake,real);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                        Toast.makeText(getApplicationContext(), "No Connection/Communication Error!", Toast.LENGTH_SHORT).show();

                    } else if (volleyError instanceof AuthFailureError) {
                        Toast.makeText(getApplicationContext(), "Authentication/ Auth Error!", Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ServerError) {
                        Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof NetworkError) {
                        Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ParseError) {
                        Toast.makeText(getApplicationContext(), "Parse Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("page_number", "1");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
            return null;
        }

        private void onPostExecute(String fake, String real)
        {
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
            intent.putExtra("fake", fake);
            intent.putExtra("real", real);
            startActivity(intent);
        }

    }
}