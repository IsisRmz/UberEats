package com.example.chani.ubereats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import modelos.Usuario;

public class RegistroActivity extends AppCompatActivity {

    EditText editTextName, editTextAdress, editTextUser, editTexPass, editTextEmail;
    Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        editTextName = findViewById(R.id.editTextName);
        editTextAdress = findViewById(R.id.editTextAdress);
        editTextUser = findViewById(R.id.editTexUser);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTexPass = findViewById(R.id.editTextPass);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
    }

    public void registrarse(View view) {

        crearUser();
    }

    private void crearUser() {
        String correo, pass, nombre, direccion, usuario;
        nombre = editTextName.getText().toString();
        direccion = editTextAdress.getText().toString();
        usuario = editTextUser.getText().toString();
        correo = editTextEmail.getText().toString();
        pass = editTexPass.getText().toString();

        if (nombre.isEmpty()) {
            editTextName.setError("Falta nombre");
            //si no es el metodo void seria return null
            return;
        }
        if (direccion.isEmpty()) {
            editTextAdress.setError("Falta direccion");
            return;
        }
        if (usuario.isEmpty()) {
            editTextUser.setError("Falta usuario");
        }
        if (correo.isEmpty()) {
            editTextEmail.setError("Falta correo electronico");
            return;
        }
        if (pass.isEmpty()) {
            editTexPass.setError("Falta contraseña");
            return;
        }
        Usuario datos=new Usuario(usuario, nombre, direccion, correo, pass);
        new registrarse().execute(datos);

    }



    //Permite generar una clase que realice una tarea fuera del hilo principal
    class registrarse extends AsyncTask <Usuario, Integer, Boolean>
    {
        @Override
        protected Boolean doInBackground(Usuario... usuarios) {
            //preparar los datos de insersión
            String params="";
            params="user="+usuarios[0].getNickname()+ "&"+
                    "nombre="+usuarios[0].getNombre()+ "&"+
                     "direccion="+usuarios[0].getDireccion()+ "&"+
                     "correo="+usuarios[0].getCorreo()+ "&"+
                      "password="+usuarios[0].getPassword();
            //Imprimir los parametros
            Log.d("registro", params);
            //preparar la conexión
            try {
                URL url=new URL("http://172.18.26.67/cursoAndroid/vista/Usuario/crearUsuario.php");
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();

                //para informar la ide de informacion
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();

                BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(params);
                writer.flush();
                writer.close();
                outputStream.close();

                connection.connect();

                connection.getResponseCode();

                int responseCode= connection.getResponseCode();
                if(responseCode==HttpURLConnection.HTTP_OK){
                    Log.i("AddUser", "Usuario agregado con exito");
                    return true;

                }
                if(responseCode==HttpURLConnection.HTTP_BAD_REQUEST){
                    Log.i("AddUser", "Otra cosa C:");
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
                Toast.makeText(RegistroActivity.this, "Usuario Agregado", Toast.LENGTH_SHORT).show();
                RegistroActivity.this.finish();

            }else{
                Toast.makeText(RegistroActivity.this, "Usuario Rechazado", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
