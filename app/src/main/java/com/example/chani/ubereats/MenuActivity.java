package com.example.chani.ubereats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import modelos.Tienda;

public class MenuActivity extends AppCompatActivity {

ListView lvtiendas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        new fetchTiendas().execute();

    }

    class fetchTiendas extends AsyncTask<Void, Integer, List<Tienda> >{

        List<Tienda> tiendas;
        @Override
        protected List<Tienda> doInBackground(Void... voids) {
            try {
                URL url = new URL("http://172.18.26.67/cursoAndroid/vista/tienda/obtenerTiendas.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String output;
                while ((output = bufferedReader.readLine())!= null){
                    stringBuilder.append(output);
                }
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                for (int n = 0; n <jsonArray.length(); n++){
                    JSONObject jsonObject = jsonArray.getJSONObject(n);
                    Tienda tienda = new Tienda();
                    tienda.setDescripcion(jsonObject.getString("descripcion"));
                    tienda.setDireccion(jsonObject.getString("direccion"));
                    tienda.setId(jsonObject.getInt("id"));
                    tienda.setLatitud(jsonObject.getString("latitud"));
                    tienda.setLongitud(jsonObject.getString("longitud"));
                    tienda.setNombre(jsonObject.getString("nombre"));
                    tiendas.add(tienda);
                }
                return tiendas;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Tienda> tiendas) {
            super.onPostExecute(tiendas);
            lvtiendas = findViewById(R.id.lvtiendas);
            AdapterTiendas adapter = new AdapterTiendas(MenuActivity.this,tiendas);
            lvtiendas.setAdapter(adapter);
        }
    }
}
