package com.yaseen.latticeinnovationstest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherToday extends AppCompatActivity {

    EditText cityName;
    Button showResultBtn;
    TextView tempcenttv,tempfhtv,latitudetv,longitudetv;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_today);

        cityName=findViewById(R.id.citynameedittext);
        showResultBtn=findViewById(R.id.showresultbtn);
        tempcenttv=findViewById(R.id.tempcent);
        tempfhtv=findViewById(R.id.tempfh);
        latitudetv=findViewById(R.id.latitude);
        longitudetv=findViewById(R.id.longitude);

        showResultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String citynamestr=cityName.getText().toString();

                if (TextUtils.isEmpty(citynamestr)){
                    Toast.makeText(WeatherToday.this,"Enter Valid City",Toast.LENGTH_LONG).show();
                }else {
                    getDataFromCity(citynamestr);
                }


            }
        });

    }

    private void getDataFromCity(String citynamestr) {

        mRequestQueue.getCache().clear();

        String url = "https://api.weatherapi.com/v1/current.json?key=35c9f92ac5bf4df0811144140212307&q="+citynamestr+"&aqi=no";

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray postOfficeArray = response.getJSONArray("PostOffice");
                    if (response.getString("Status").equals("Error")) {
                        tempcenttv.setText("Pin code is not valid.");
                    } else {

                        JSONObject obj = postOfficeArray.getJSONObject(0);

                        String tempcenttext = obj.getString("temp_c");
                        String tempfhtext = obj.getString("temp_f");
                        String lattext = obj.getString("lat");
                        String longtext = obj.getString("lon");

                        tempcenttv.setText("Temperature in Centigrade:"+tempcenttext);
                        tempfhtv.setText("Temperature in Farenheit:"+tempfhtext);
                        latitudetv.setText("Latitude:"+lattext);
                        longitudetv.setText("Longitude:"+longtext);

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    tempcenttv.setText("Pin code is not valid");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(WeatherToday.this, "City is not valid", Toast.LENGTH_SHORT).show();
                tempcenttv.setText("Pin code is not valid");
            }
        });

        MySingleton.getInstance(WeatherToday.this).addToRequestQue(jsonObjectRequest);


    }
}