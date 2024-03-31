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

public class adapter_Lectura_panicos extends ArrayAdapter<clase_lectura_panicos> implements View.OnClickListener {
public static ArrayList<clase_lectura_panicos> dataSet;
 ArrayList<clase_lectura_panicos> dataSet2;
        Context mContext;
    TextView fecha;
    TextView latti;
    TextView longii;
    TextView id_cred;
    ImageView img_ver;
    clase_lectura_panicos dat;
    @Override
    public void onClick(View view) {

    }


    public adapter_Lectura_panicos(ArrayList<clase_lectura_panicos> data, Context context) {
        super(context, R.layout.item_listado_panicos, data);
        this.dataSet = data;
        this.mContext=context;
        dataSet2=new ArrayList<clase_lectura_panicos>();
        dataSet2.addAll(dataSet);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
         dat = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_listado_panicos, parent, false);
        }
        // Lookup view for data population
        fecha = (TextView) convertView.findViewById(R.id.fecha_press_panico);
        latti = (TextView) convertView.findViewById(R.id.lat_press_panico);
        longii = (TextView) convertView.findViewById(R.id.long_press_panico);
        id_cred = (TextView) convertView.findViewById(R.id.producto_id);
        img_ver = (ImageView) convertView.findViewById(R.id.image_ir_punto);

        notifyDataSetChanged();


        // Populate the data into the template view using the data object
        fecha.setText(dat.getFecha_press());
        latti.setText(dat.getLatt_press());
        longii.setText(dat.getLong_press());
        id_cred.setText(dat.getIdpanico());
        img_ver.setImageResource(R.drawable.gps_start);



            notifyDataSetChanged();
        notifyDataSetChanged();
        // Return the completed view to render on screen
        return convertView;
    }




}

