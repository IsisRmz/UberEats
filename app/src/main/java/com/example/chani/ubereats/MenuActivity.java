package com.example.chani.ubereats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import modelos.Tienda;

public class MenuActivity extends AppCompatActivity {
//Declaración de listview
ListView lvtiendas;
    ArrayList<Tienda> tiendas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        new fetchTiendas().execute();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.contextActualizar:
                Toast.makeText(this, "Actualizar", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(this, ActualizarTiendaActivity.class);
                intent.putExtra("Tiendita", tiendas.get(info.position));
                startActivityForResult(intent, 20);
                return true;
            case R.id.contextEliminar:
                    Toast.makeText(this, "Eliminar", Toast.LENGTH_SHORT).show();
                    new EliminarTienda().execute(tiendas.get(info.position));
                    return true;
            case R.id.contextNotification:
                int nNotificationId = 1;
                String channelID = "my_channel_01";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
                Intent intent1 = new Intent(getApplicationContext(), MenuActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(MenuActivity.this, 0, intent1, 0);
                NotificationManager notificationManager1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    CharSequence name = "Nombre";
                    String description = "Descripcion";
                    int importancia = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(channelID, name, importancia);
                    channel.setDescription(description);
                    channel.enableLights(true);
                    channel.enableVibration(true);
                    channel.setVibrationPattern(new long[]{100,200,300,400,500,600,400,300,200,100});
                    notificationManager1 = getSystemService(NotificationManager.class);
                    notificationManager1.createNotificationChannel(channel);
                    builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
                }
                    builder.setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Titulo")
                            .setContentText("Texto")
                            .setContentIntent(pendingIntent)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Much longer text that cannot fit one line..."))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    notificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager1.notify(nNotificationId, builder.build());




                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==20 && resultCode== Activity.RESULT_OK){
            tiendas.clear();
            new fetchTiendas().execute();
        }
    }

    //Tarea Async para la petición de datos en este caso las tiendas
    // El primer parametro, VOID es lo que recibe, segundo es avance (casi siempre int) y el ultimo es que regresa
    class fetchTiendas extends AsyncTask<Void, Integer, ArrayList<Tienda> >{


        @Override
        protected ArrayList<Tienda> doInBackground(Void... voids) {
            try {
                //Conexión para recibir datos y acomodarlos por objetos dentro de un array
                URL url = new URL("http://172.18.26.67/cursoAndroid/vista/tienda/obtenerTiendas.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
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
                    Tienda tienda = new Tienda();
                    tienda.setDescripcion(jsonObject.getString("descripcion"));
                    tienda.setDireccion(jsonObject.getString("direccion"));
                    tienda.setId(jsonObject.getInt("idtienda"));
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
        protected void onPostExecute(final ArrayList<Tienda> tiendas) {
            super.onPostExecute(tiendas);
            lvtiendas = findViewById(R.id.lvtiendas);
            //Adaptador de la listview
            AdapterTiendas adapter = new AdapterTiendas(MenuActivity.this,tiendas);
            lvtiendas.setAdapter(adapter);
            registerForContextMenu(lvtiendas);
            lvtiendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MenuActivity.this, TiendaProductosActivity.class);
                    intent.putExtra("tienda", tiendas.get(i));
                    startActivity(intent);
                }
            });
        }
    }
    class EliminarTienda extends AsyncTask<Tienda, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Tienda... tiendas) {
            String params= "idtienda="+tiendas[0].getId();

            try {
                URL url=new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/eliminarTienda.php");
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream outputStream =connection.getOutputStream();
                BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                writer.write(params);
                writer.flush();
                writer.close();
                outputStream.close();

                connection.connect();

                int responseCode= connection.getResponseCode();
                if(responseCode==HttpURLConnection.HTTP_OK){
                    return true;
                }else{
                    return false;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
//COMENTARIO CHINGÖN
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(MenuActivity.this, "Tienda eliminada con exito", Toast.LENGTH_SHORT).show();
                tiendas.clear();
                new fetchTiendas().execute();
            }else{
                Toast.makeText(MenuActivity.this, "Tienda no eliminada :)", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
