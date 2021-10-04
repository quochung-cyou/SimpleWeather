package com.quochung.minimalweather;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.location.Location;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    public static final int LOCATION_PERMISSION = 100;
    public RequestQueue requestQueue;

    TextView city, temp, state, date;
    ImageView weather, weatherblur;
    SharedPreferences weatherdata;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = VolleyS.getmInstance(this).getRequestQueue();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        city = findViewById(R.id.city);
        date = findViewById(R.id.date);
        temp = findViewById(R.id.temp);
        state = findViewById(R.id.state);
        weather = findViewById(R.id.weather);
        weatherblur = findViewById(R.id.weatherblur);

        //Set date
        Date currentdate = new Date();
        String stringDate = DateFormat.getDateInstance() .format(currentdate);
        date.setText(stringDate);
        weatherdata = getSharedPreferences("weatherdata", MODE_PRIVATE);

        state.setText(weatherdata.getString("statedata", "Clear"));
        String coloruse  = weatherdata.getString("coloruse", "#8EC1DD");


        //-------------------
        ////Set cache temp
        String temptext = weatherdata.getString("tempdata", "26") + "°";
        temp.setText(temptext);
        TextPaint paint = temp.getPaint();
        float width = paint.measureText(temptext);

        Shader textShader = new LinearGradient(10, 10, width, temp.getTextSize(),
                new int[]{
                        Color.parseColor(coloruse),
                        Color.parseColor("#8EC1DD"),
                }, null, Shader.TileMode.CLAMP);
        temp.getPaint().setShader(textShader);
        temp.setTextColor(Color.parseColor(coloruse));

        city.setText(weatherdata.getString("citydata", "Bac Ninh"));



        //Set image cache
        switch(weatherdata.getString("statedata", "clear").toLowerCase()) {
            case "clouds":
                weather.setImageResource(R.drawable.clouds);
                coloruse = "#8EC1DD";
                state.setTextColor(Color.parseColor(coloruse));
                break;
            case "atmosphere":
                weather.setImageResource(R.drawable.atmosphere);
                coloruse = "#27B1FF";
                state.setTextColor(Color.parseColor(coloruse));
                break;
            case "snow":
                weather.setImageResource(R.drawable.snow);
                coloruse = "#8EC1DD";
                state.setTextColor(Color.parseColor(coloruse));
                break;
            case "rain":
                weather.setImageResource(R.drawable.rain);
                coloruse = "#4E8DB1";
                state.setTextColor(Color.parseColor(coloruse));
                break;
            case "drizzle":
                weather.setImageResource(R.drawable.rain);
                coloruse = "#4E8DB1";
                state.setTextColor(Color.parseColor(coloruse));
                break;
            case "thunderstorm":
                weather.setImageResource(R.drawable.thunderstorm);
                coloruse = "#BF8EDD";
                state.setTextColor(Color.parseColor(coloruse));
                break;
            default:
                weather.setImageResource(R.drawable.clear);
                coloruse = "#FF8E27";
                state.setTextColor(Color.parseColor(coloruse));
                break;
        }







        if (checkPermission()) {
            getLocation();
        }
    }



    public void getcityname(Location mLastLocation) {
        //Get the city name for openweatherapi
        String url = "https://api.openweathermap.org/geo/1.0/reverse?lat=" + mLastLocation.getLatitude() + "&lon=" + mLastLocation.getLongitude() + "&limit=1&appid=f6531adea60f0d386c77dce30be52c5e";
        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                displayweather(response.getJSONObject(0).getString("name"));
                city.setText(response.getJSONObject(0).getString("name"));
                editor = weatherdata.edit();
                editor.putString("citydata", response.getJSONObject(0).getString("name"));
                editor.apply();
                //citydata
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(JsonArrayRequest);
    }


    public void displayweather(String cityname) {
        //Call with city name
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityname + "&units=metric&appid=f6531adea60f0d386c77dce30be52c5e";
        JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                //State
                //statedata
                state.setText(response.getJSONArray("weather").getJSONObject(0).getString("main"));
                editor = weatherdata.edit();
                editor.putString("statedata", response.getJSONArray("weather").getJSONObject(0).getString("main"));





                String checkstate = response.getJSONArray("weather").getJSONObject(0).getString("main");
                String coloruse;
                switch(checkstate.toLowerCase()) {
                    case "clouds":
                        weather.setImageResource(R.drawable.clouds);

                        coloruse = "#8EC1DD";
                        state.setTextColor(Color.parseColor(coloruse));
                        break;
                    case "atmosphere":
                        weather.setImageResource(R.drawable.atmosphere);
                        coloruse = "#27B1FF";
                        state.setTextColor(Color.parseColor(coloruse));
                        break;
                    case "snow":
                        weather.setImageResource(R.drawable.snow);
                        coloruse = "#8EC1DD";
                        state.setTextColor(Color.parseColor(coloruse));
                        break;
                    case "rain":
                        weather.setImageResource(R.drawable.rain);
                        coloruse = "#4E8DB1";
                        state.setTextColor(Color.parseColor(coloruse));
                        break;
                    case "drizzle":
                        weather.setImageResource(R.drawable.rain);
                        coloruse = "#4E8DB1";
                        state.setTextColor(Color.parseColor(coloruse));
                        break;
                    case "thunderstorm":
                        weather.setImageResource(R.drawable.thunderstorm);
                        coloruse = "#BF8EDD";
                        state.setTextColor(Color.parseColor(coloruse));
                        break;
                    default:
                        weather.setImageResource(R.drawable.clear);
                        coloruse = "#FF8E27";
                        state.setTextColor(Color.parseColor(coloruse));
                        break;
                }
                editor.putString("coloruse", coloruse);
                //coloruse
                String temptext = (Math.round(Double.parseDouble(response.getJSONObject("main").getString("temp")))) + "°";
                temp.setText(temptext);
                //tempdata
                editor.putString("tempdata", String.valueOf((Math.round(Double.parseDouble(response.getJSONObject("main").getString("temp"))))));
                editor.apply();
                TextPaint paint = temp.getPaint();
                float width = paint.measureText(temptext);

                Shader textShader = new LinearGradient(10, 10, width, temp.getTextSize(),
                        new int[]{
                                Color.parseColor(coloruse),
                                Color.parseColor("#ffffff"),
                        }, null, Shader.TileMode.CLAMP);
                temp.getPaint().setShader(textShader);
                temp.setTextColor(Color.parseColor(coloruse));
                //Temperature

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(JsonObjectRequest);
    }






    private void getLocation() {
        //Get location and get city name
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return; }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location != null) {
                getcityname(location);

            }
        });
    }





    //runtime location permission
    public boolean checkPermission() {
        int ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED && ACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }

}