package com.example.chani.ubereats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import modelos.Producto;
import modelos.Tienda;

public class AdapterProductos extends ArrayAdapter {
    private Context context;
    private List<Producto> productos;
    public AdapterProductos(@NonNull Context context, List<Producto> productos) {
        super(context, R.layout.list_product_item, productos);
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_product_item, null);
        TextView lblDescripcion, lblNombre, lblStock, lblPrecio;
        lblDescripcion = view.findViewById(R.id.lblDescripcionProductoL);
        lblNombre = view.findViewById(R.id.lblNombreProductoL);
        lblStock = view.findViewById(R.id.lblStockProductoL);
        lblPrecio = view.findViewById(R.id.lblPrecioProductoL);
        lblDescripcion.setText(productos.get(position).getDescripcion());
        lblNombre.setText(productos.get(position).getNombre());
        lblStock.setText(String.valueOf(productos.get(position).getStock()));
        lblPrecio.setText(String.valueOf(productos.get(position).getPrecio()));
        return view;
    }
}
