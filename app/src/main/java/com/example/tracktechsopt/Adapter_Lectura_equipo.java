package com.example.tracktechsopt;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tracktech.R;

import java.util.ArrayList;
import java.util.Locale;

public class Adapter_Lectura_equipo extends ArrayAdapter<clase_lectura_equipo> implements View.OnClickListener {
public static ArrayList<clase_lectura_equipo> dataSet;
 ArrayList<clase_lectura_equipo> dataSet2;
        Context mContext;
    TextView imei;
    TextView serie;
    TextView id_equipp;
    TextView modelo;
    TextView state;
    TextView state_sim_enequipo;

    TextView placa;

    TextView teelfono;
    ImageView img_ir;
    ImageView img_estado;


    clase_lectura_equipo dat;
    @Override
    public void onClick(View view) {

    }


    public Adapter_Lectura_equipo(ArrayList<clase_lectura_equipo> data, Context context) {
        super(context, R.layout.item_listado_equipo, data);
        this.dataSet = data;
        this.mContext=context;
        dataSet2=new ArrayList<clase_lectura_equipo>();
        dataSet2.addAll(dataSet);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
         dat = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_listado_equipo, parent, false);
        }
        // Lookup view for data population
        imei = (TextView) convertView.findViewById(R.id.dato_imei_equipo);
        serie = (TextView) convertView.findViewById(R.id.dato_serie_equipo);
        modelo = (TextView) convertView.findViewById(R.id.dato_modelo_equipo);
        id_equipp = (TextView) convertView.findViewById(R.id.dato_id_equipo);
        teelfono = (TextView) convertView.findViewById(R.id.dato_telef_equipo);
        state = (TextView) convertView.findViewById(R.id.dato_estado_asig);
        state_sim_enequipo = (TextView) convertView.findViewById(R.id.dato_sims_equipo);
        placa = (TextView) convertView.findViewById(R.id.dato_placaequipo);

        img_ir = (ImageView) convertView.findViewById(R.id.image_ir_punto);
        img_estado = (ImageView) convertView.findViewById(R.id.image_estado_asig);

        notifyDataSetChanged();


        // Populate the data into the template view using the data object
        imei.setText(dat.getImei_vehiculo());
        serie.setText(dat.getSerie_vehic());
        modelo.setText(dat.getModedl_gps());
        id_equipp.setText(dat.getId_equip());
        teelfono.setText(dat.getTelefono_equipo());
        state.setText(dat.getEstado_gps());

        if(dat.getPlaca_equipo().equals("-")){
        placa.setVisibility(convertView.INVISIBLE);
        }
        else{
        placa.setText("INSTALADO EN: "+dat.getPlaca_equipo());
        placa.setVisibility(convertView.VISIBLE);
        }

        img_ir.setImageResource(R.drawable.visiont);

        if(dat.getTelefono_equipo().equals("0")){
        state_sim_enequipo.setText("Equipo sin SIM");
        }
        else{
            state_sim_enequipo.setText("");
        }

        if(state.getText().equals("Disponible"))
        {
            img_estado.setImageResource(R.drawable.dispon);
            state.setBackgroundColor(Color.rgb(0, 227, 113));
        }
        if(state.getText().equals("Instalado"))
        {
            img_estado.setImageResource(R.drawable.instalado);
            state.setBackgroundColor(Color.rgb(100, 225, 220));
        }
        if(state.getText().equals("Desinstalado"))
        {
            placa.setText("FUE INSTALADO EN: "+dat.getPlaca_equipo());
            img_estado.setImageResource(R.drawable.desinstalar);
            state.setBackgroundColor(Color.rgb(250, 59, 59));
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
            for (clase_lectura_equipo c : dataSet2) {
                if (c.getImei_vehiculo().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    dataSet.add(c);
                }
            }
        }
        notifyDataSetChanged();


    }

}

