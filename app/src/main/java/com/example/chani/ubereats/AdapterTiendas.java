package com.example.chani.ubereats;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import modelos.Tienda;

//Adaptador para las tiendas, usando list_store_item como layout
public class AdapterTiendas extends ArrayAdapter {

    private Context context;
    private List<Tienda> datos;

    public AdapterTiendas(@NonNull Context context, List<Tienda> datos) {
        super(context, R.layout.list_store_item, datos);
        this.context = context;
        this.datos = datos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_store_item, null);
        TextView lblNombre = view.findViewById(R.id.lblNombre);
        TextView lblDesc = view.findViewById(R.id.lblDescripcion);
        lblDesc.setText(datos.get(position).getDescripcion());
        lblNombre.setText(datos.get(position).getNombre());
        return view;
    }
}
