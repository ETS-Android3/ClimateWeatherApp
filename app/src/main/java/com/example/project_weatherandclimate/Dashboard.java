package com.example.project_weatherandclimate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

public final class  Dashboard extends AppCompatActivity {

    EditText et;
    String city;
    private TextView cityN = null;
    private TextView inputDate = null;
    private ConstraintLayout background;
    private TextView inputTemp, inputHumidty,inputWeather, inputWind,inputDegres,inputDay1, inputAlertDescription;
    private Date date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        et = findViewById(R.id.et);
        cityN = findViewById(R.id.city);
        inputDate = findViewById(R.id.date);
        background = findViewById(R.id.backgroundDashboard);
        inputHumidty = findViewById(R.id.textHumidity);
        inputTemp = findViewById(R.id.textTemp);
        inputWeather = findViewById(R.id.textWeather);
        inputWind = findViewById(R.id.textWind);

        date = new Date();



    }

    public void get(View v) {
        city = et.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city.toLowerCase() + "&appid=4a349dc7b3e97c50fa940bf7b764f9a6";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                String output = "";
                try {

                    //City Name
                    String cityName = response.getString("name");
                    cityN.setText(cityName);

                    //Temperature & Humidity
                    String format = "0.00";
                    NumberFormat formatter = new DecimalFormat(format);
                    JSONObject temps= (JSONObject) response.get("main");
                    String humidity = temps.getString("humidity");
                    double temp = temps.getDouble("temp") - 273.15;
                    String tmp = formatter.format(temp);
                    inputTemp.setText(tmp+" °C");
                    inputHumidty.setText(humidity+" %");

                    //Weather
                    JSONArray weather= (JSONArray) response.get("weather");
                    JSONObject jsonObjectWeather = weather.getJSONObject(0);
                    String weatherMain = jsonObjectWeather.getString("main") ;
                    String weatherDescription  =  jsonObjectWeather.getString("description");
                    inputWeather.setText(weatherDescription);
                    setBackgroundImage(weatherMain,date);




                    //Wind
                    JSONObject wind = (JSONObject) response.get("wind");
                    double speed = wind.getDouble("speed");
                    String windSpeed = formatter.format(speed);
                    inputWind.setText(windSpeed +" m/s");


                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    private void setBackgroundImage(String weatherMain, Date date) {

        if (date.getTime()<18){

            background.setBackgroundResource(R.drawable.altinay);
        }

        if (date.getTime()>18){

            background.setBackgroundResource(R.drawable.clouds);
        }

        if(weatherMain.equals("Clouds")){
            //background.setBackground(R.drawable.clouds);
            background.setBackgroundResource(R.drawable.clouds);

            if (date.getTime()>12){

                background.setBackgroundResource(R.drawable.sky);
            }
            else{

                background.setBackgroundResource(R.drawable.clouds);

            }

        }
    }

}