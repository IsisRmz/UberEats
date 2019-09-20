package com.example.chani.ubereats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {

    EditText editTextName, editTextAdress, editTextUser, editTexPass, editTextEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        editTextName = findViewById(R.id.editTextName);
        editTextAdress = findViewById(R.id.editTextAdress);
        editTextUser = findViewById(R.id.editTexUser);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTexPass = findViewById(R.id.editTextPass);
    }

    public void registrarse(View view) {

        crearUser();
    }

    private void crearUser() {
        String correo, pass, nombre, direccion, usuario;
        nombre= editTextName.getText().toString();
        direccion=editTextAdress.getText().toString();
        usuario=editTextEmail.getText().toString();
        correo = editTextEmail.getText().toString();
        pass = editTexPass.getText().toString();

        if (nombre.isEmpty()){
            editTextName.setError("Falta nombre");
            return;
        }
        if(direccion.isEmpty()){
            editTextAdress.setError("Falta direccion");
            return;
        }
        if(usuario.isEmpty()){
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
    }
}
