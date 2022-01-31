package com.example.project_weatherandclimate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public final class Home extends AppCompatActivity {
    private TextView city = null;
    private TextView inputDate = null;
    private TextView inputTemp, inputHumidty,inputWeather, inputWind,inputDegres,inputDay1, inputAlertDescription;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;
    private String userUID;
    private Date date;
    private String cityNameUser;
    private ConstraintLayout background;
    private Button buttonLogOut ;
    private Button buttonSearch;
    private double latitude = 0;
    private double longitude = 0;
    private String url ="https://api.openweathermap.org/data/2.5/onecall?appid=4a349dc7b3e97c50fa940bf7b764f9a6&lat=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Firebase Initialisation
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userUID = mUser.getUid();

        //Item References
        city = findViewById(R.id.city);
        inputDate = findViewById(R.id.date);
        background = findViewById(R.id.backgoundHome);
        inputHumidty = findViewById(R.id.textHumidity);
        inputTemp = findViewById(R.id.textTemp);
        inputWeather = findViewById(R.id.textWeather);
        inputWind = findViewById(R.id.textWind);
        inputDay1 = findViewById(R.id.inputDay1);
        inputDay1.setText("Mistt");
        inputAlertDescription = findViewById(R.id.AlertDescription);
        buttonLogOut = findViewById(R.id.buttonLogout);
        buttonSearch = findViewById(R.id.buttonSearch);


        //Time
        date = new Date();

        //User Profile
        reference.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    cityNameUser = userProfile.city.toUpperCase();
                    city.setText(cityNameUser);
                    inputDate.setText(date.toString());

                    //API1
                       RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                       connectToAPIandSendData();


                    //API2
                    RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
                    connectToAPI2andSendData();




                }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this, "Something Wrong was happened ", Toast.LENGTH_LONG).show();
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, Login.class));
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Dashboard.class));
            }
        });



    }

    private void connectToAPI2andSendData() {
        RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
        String url1 = url+latitude+"lon="+longitude;
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                String output = "";
                try {


                    JSONArray nextDays = (JSONArray) response.get("daily");
                    JSONObject jsonObjectWeatherDescription = nextDays.getJSONObject(0);
                    JSONArray weatherMainWeatherDescription = jsonObjectWeatherDescription.getJSONArray("weather");
                    JSONObject jsonObjectWeatherDescriptionDay =  weatherMainWeatherDescription.getJSONObject(0);
                    String weatherDescriptionDay  =  jsonObjectWeatherDescriptionDay.getString("Description");
                    inputDay1.setText("Mist");




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
        queue1.add(request1);

    }

    private void connectToAPIandSendData() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityNameUser + "&appid=3f3409947deca9d4fca4972463ac25df";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                String output = "";
                try {

                    //City Name
                    String cityName = response.getString("name");

                    //latitude et longitude
                    JSONObject crdtCity= (JSONObject) response.get("coord");
                    latitude = crdtCity.getDouble("lat");
                    longitude = crdtCity.getDouble("lon");




                    //Temperature & Humidity
                    String format = "0.00";
                    NumberFormat formatter = new DecimalFormat(format);
                    JSONObject temps= (JSONObject) response.get("main");
                    String humidity = temps.getString("humidity");
                    double temp = temps.getDouble("temp") - 273.15;
                    String tmp = formatter.format(temp);
                    inputTemp.setText(tmp+" °C");
                    inputHumidty.setText(humidity +" %");


                    //Weather
                    JSONArray weather= (JSONArray) response.get("weather");
                    JSONObject jsonObjectWeather = weather.getJSONObject(0);
                    String weatherMain = jsonObjectWeather.getString("main") ;
                    String weatherDescription  =  jsonObjectWeather.getString("description");
                    inputWeather.setText(weatherMain);
                    inputAlertDescription.setText(weatherDescription);
                    setBackgroundImage(weatherMain,date);



                    //Wind
                    JSONObject wind = (JSONObject) response.get("wind");
                    double speed = wind.getDouble("speed");
                    String windSpeed = formatter.format(speed);
                    inputWind.setText(windSpeed +" m/s");



                    //Api for Lat and lon
                     /*
                    api5 = 'https://api.openweathermap.org/data/2.5/weather?q=paris&appid=4a349dc7b3e97c50fa940bf7b764f9a6'

                    json_data2 = requests.get(api5).json()
                    Alert2 = json_data2
                    print(Alert2)

                    long = json_data2['coord']['lon']
                    lat = json_data2['coord']['lat']

                    api4 = f'https://api.openweathermap.org/data/2.5/onecall?appid=4a349dc7b3e97c50fa940bf7b764f9a6&lat={lat}&lon={long}'
                    json_data = requests.get(api4).json()
                    #alert = json_data['alerts']['description']
                    Alert = json_data['daily'][0]['weather'][0]['description']
                    Alert1 = json_data
                    print(api4)
                    print(json_data)
                    print(Alert)
                    */

                     /*next Days
                                    JSONArray nextDays = (JSONArray) response.get("daily");
                                    JSONObject jsonObjectWeatherDescription = weather.getJSONObject(0);
                                    String weatherMainWeatherDescription = jsonObjectWeatherDescription.getString("weather");
                                    JSONObject jsonObjectWeatherDescriptionDay = weather.getJSONObject(0);
                                    String weatherDescriptionDay  =  jsonObjectWeather.getString("Description");
                                    inputDay.setText(weatherDescriptionDay);

                                     */


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


    private void setBackgroundImage(String weatherMain,Date date) {

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