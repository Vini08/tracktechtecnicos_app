package com.example.tracktechsopt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tracktech.R;

import java.util.ArrayList;
import java.util.Locale;

public class Adapter_Lectura_vehiculo extends ArrayAdapter<clase_lectura_vehiculos> implements View.OnClickListener {
public static ArrayList<clase_lectura_vehiculos> dataSet;
 ArrayList<clase_lectura_vehiculos> dataSet2;
        Context mContext;
    TextView placa;
    TextView modelo;
    TextView marca;
    TextView id_cred;
    TextView type;
    TextView imei;

    TextView estad;
    TextView modelogps;
    ImageView img_ver;
    clase_lectura_vehiculos dat;
    @Override
    public void onClick(View view) {

    }


    public Adapter_Lectura_vehiculo(ArrayList<clase_lectura_vehiculos> data, Context context) {
        super(context, R.layout.item_lista_vehiculo, data);
        this.dataSet = data;
        this.mContext=context;
        dataSet2=new ArrayList<clase_lectura_vehiculos>();
        dataSet2.addAll(dataSet);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
         dat = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_vehiculo, parent, false);
        }
        // Lookup view for data population
        placa = (TextView) convertView.findViewById(R.id.dato_placa);
        marca = (TextView) convertView.findViewById(R.id.dato_marca);
        modelo = (TextView) convertView.findViewById(R.id.dato_modelo);
        id_cred = (TextView) convertView.findViewById(R.id.producto_id);
        type = (TextView) convertView.findViewById(R.id.dato_type);
        imei = (TextView) convertView.findViewById(R.id.imei_vehic);
        modelogps = (TextView) convertView.findViewById(R.id.model_gps_vehic);
        estad = (TextView) convertView.findViewById(R.id.estado_vehic);

        img_ver = (ImageView) convertView.findViewById(R.id.image_vehic);

        notifyDataSetChanged();


        // Populate the data into the template view using the data object
        placa.setText(dat.getPlaca_vehic());
        marca.setText(dat.getMarca_vehic());
        modelo.setText(dat.getModel_vehic());
        id_cred.setText(dat.getId_vehic());
        type.setText(dat.getTipo_vehic());
        imei.setText(dat.getImei_vehiculo());
        modelogps.setText(dat.getModedl_gps());
        estad.setText(dat.getEstado_vehicul());

        if(estad.getText().equals("Pendiente")){
            img_ver.setImageResource(R.drawable.pendiente);
        }
        if(estad.getText().equals("Instalado")){
            img_ver.setImageResource(R.drawable.marcacheck);
        }



            notifyDataSetChanged();
        notifyDataSetChanged();
        // Return the completed view to render on screen
        return convertView;
    }


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        dataSet.clear();
        if(charText.length()==0){
            dataSet.addAll(dataSet2);
        }
        else{
            for (clase_lectura_vehiculos c : dataSet2) {
                if (c.getPlaca_vehic().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    dataSet.add(c);
                }
            }
        }
        notifyDataSetChanged();


    }

}

