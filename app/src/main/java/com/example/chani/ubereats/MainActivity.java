package com.example.chani.ubereats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    Button btnIniciar, btnRegistrar;
    EditText editTextName, editTextPass;
    String user,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName=findViewById(R.id.editTextName);
        editTextPass=findViewById(R.id.editTextPass);



    }

    //metodo de inicio de sesión
    public void iniciarS(View view) {
        user=editTextName.getText().toString().trim();
        password=editTextPass.getText().toString();
        //Validar datos de la interfaz(usuario, password)
        if(TextUtils.isEmpty(user)){
            editTextName.setError("Usuario vacio");
            editTextName.setFocusable(true);
            return;
        }
        if(TextUtils.isEmpty(password)){
            editTextPass.setError("Contraseña vacia");
            editTextPass.setFocusable(true);
            return;
        }
        Log.d("datos", user +" "+password);
        new iniciarRest().execute(user,password);

    }

    //Clase AsyncTask

    class iniciarRest extends AsyncTask<String, Integer, String>{
        //variable de petición de conexión
        URLConnection connection=null;
        //variable para el resultado
        String result;

        @Override
        protected String doInBackground(String... strings) {

            try {
                connection = new URL("http://172.18.26.67/cursoAndroid/vista/usuario/iniciarSesion.php?usuario="+strings[0]+"&password="+strings[1]).openConnection();
                InputStream inputStream = (InputStream) connection.getContent();
                byte[] buffer = new byte[10000];
                int size = inputStream.read(buffer);
                result = new String(buffer, 0, size);
                Log.i("result", result);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("1")){
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            } else{
                Toast.makeText(MainActivity.this,"Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void registrarse(View view) {
            Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
            startActivity(intent);


    }
}
