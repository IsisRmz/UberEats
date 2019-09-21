package com.example.chani.ubereats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import modelos.Tienda;

public class ActualizarTiendaActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextIdT, editTextNombreT, editTextDireccion, editTextLatitud, editTextLongitud, editTextDescripcion;
    Button btnActualizarTienda, btnCancelarTienda;
    Intent intent;
    Tienda tiendita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_tienda);

        editTextIdT = findViewById(R.id.editTextIdT);
        editTextNombreT = findViewById(R.id.editTextNombreT);
        editTextDireccion = findViewById(R.id.editTextDireccion);
        editTextLatitud = findViewById(R.id.editTextLatitud);
        editTextLongitud = findViewById(R.id.editTextLongitud);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);

        btnActualizarTienda =findViewById(R.id.btnActualizarTienda);
        btnCancelarTienda =findViewById(R.id.btnCancelarTienda);
        btnActualizarTienda.setOnClickListener(this);
        btnCancelarTienda.setOnClickListener(this);
        intent = getIntent();
        tiendita = (Tienda) intent.getSerializableExtra("Tiendita");
        editTextIdT.setText(String.valueOf(tiendita.getId()));
        editTextDescripcion.setText(tiendita.getDescripcion());
        editTextLongitud.setText(tiendita.getLongitud());
        editTextLatitud.setText(tiendita.getLatitud());
        editTextDireccion.setText(tiendita.getDireccion());
        editTextNombreT.setText(tiendita.getNombre());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnActualizarTienda:
                actualizar();
                break;
            case R.id.btnCancelarTienda:
                break;
        }
    }

    private void actualizar() {
        String nombre=editTextNombreT.getText().toString();
        String direccion=editTextDireccion.getText().toString();
        String latitud=editTextLatitud.getText().toString();
        String longitud=editTextLongitud.getText().toString();
        String descripcion=editTextDescripcion.getText().toString();
        tiendita.setNombre(nombre);
        tiendita.setDireccion(direccion);
        tiendita.setLatitud(latitud);
        tiendita.setLongitud(longitud);
        tiendita.setDescripcion(descripcion);
        new ActualizarTienda().execute(tiendita);
    }

    class ActualizarTienda extends AsyncTask<Tienda, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Tienda... tiendas) {
            String params="nombre="+tiendas[0].getNombre()+ "&"+
                           "direccion="+tiendas[0].getDireccion()+ "&"+
                           "latitud="+tiendas[0].getLatitud()+ "&"+
                            "longitud="+tiendas[0].getLongitud()+ "&"+
                            "descripcion="+tiendas[0].getDescripcion()+ "&"+
                             "idtienda="+tiendas[0].getId();

            try {
                URL url=new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/modificarTienda.php");
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

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(ActualizarTiendaActivity.this, "Tienda actualizada con exito", Toast.LENGTH_SHORT).show();
                String result = "ok";
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(ActualizarTiendaActivity.RESULT_OK,returnIntent);
                finish();
            }else{
                Toast.makeText(ActualizarTiendaActivity.this, "Tienda no actualizada :)", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
