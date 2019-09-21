package com.example.chani.ubereats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import modelos.Producto;
import modelos.Tienda;

public class TiendaProductosActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    Tienda tienda = null;
    TextView lblNombreTienda, lblDescripcionTienda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda_productos);
        Intent intent = getIntent();
        tienda = (Tienda) intent.getSerializableExtra("tienda");
        Log.i("tienda", tienda.getNombre());
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        lblDescripcionTienda = findViewById(R.id.lblDescripcionTienda);
        lblNombreTienda = findViewById(R.id.lblNombreTienda);
        lblNombreTienda.setText(tienda.getNombre());
        lblDescripcionTienda.setText(tienda.getDescripcion());

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap gmap) {
        googleMap = gmap;
        googleMap.setMinZoomPreference(17);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng ny = new LatLng(Float.parseFloat(tienda.getLatitud()), Float.parseFloat(tienda.getLongitud()));
        googleMap.addMarker(new MarkerOptions().position(ny).title(tienda.getNombre()).visible(true));
        //googleMap.getUiSettings().setScrollGesturesEnabled(false);
        //googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

    class fetchProductos extends AsyncTask<Integer, Integer, ArrayList<Producto>>{

        ArrayList<Producto> productos;
        @Override
        protected ArrayList<Producto> doInBackground(Integer... integers) {
            try {
                //Conexión para recibir datos y acomodarlos por objetos dentro de un array
                URL url = new URL("http://172.18.26.67/cursoAndroid/vista/producto/obtenerProductos.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                String params = "idtienda="+integers[0];
                OutputStream outputStream = urlConnection.getOutputStream();

                BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(params);
                writer.flush();
                writer.close();
                outputStream.close();

                urlConnection.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String output;
                while ((output = bufferedReader.readLine())!= null){
                    stringBuilder.append(output);
                }
                //Hacemos un JSONArray con el texto obtenido
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                for (int n = 0; n <jsonArray.length(); n++){
                    //Separamos por objetos y guardamos cada objeto en el ListArray
                    JSONObject jsonObject = jsonArray.getJSONObject(n);
                    Producto producto = new Producto();
                    producto.setDescripcion(jsonObject.getString("descripcion"));
                    producto.setIdproducto(jsonObject.getInt("idproducto"));
                    producto.setIdtienda(jsonObject.getInt("idtienda"));
                    producto.setNombre(jsonObject.getString("nombre"));
                    producto.setPrecio((float) jsonObject.getDouble("precio"));
                    producto.setStock(jsonObject.getInt("stock"));
                    productos.add(producto);
                }
                return productos;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
