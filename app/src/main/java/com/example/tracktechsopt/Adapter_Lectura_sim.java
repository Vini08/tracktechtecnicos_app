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

public class Adapter_Lectura_sim extends ArrayAdapter<clase_lectura_sim> implements View.OnClickListener {
public static ArrayList<clase_lectura_sim> dataSet;
 ArrayList<clase_lectura_sim> dataSet2;
        Context mContext;
    TextView imeisim;
    TextView placasim;
    TextView id_simm;
    TextView marcasim;
    TextView telefonosim;
    TextView iccsim;

    TextView observ;
    TextView estadosim;
TextView ubicasim;

    ImageView img_estado;
    ImageView img_install_sim;


    clase_lectura_sim dat;
    @Override
    public void onClick(View view) {

    }


    public Adapter_Lectura_sim(ArrayList<clase_lectura_sim> data, Context context) {
        super(context, R.layout.item_listado_sim, data);
        this.dataSet = data;
        this.mContext=context;
        dataSet2=new ArrayList<clase_lectura_sim>();
        dataSet2.addAll(dataSet);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
         dat = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_listado_sim, parent, false);
        }
        // Lookup view for data population
        id_simm = (TextView) convertView.findViewById(R.id.dato_id_sim);
        telefonosim = (TextView) convertView.findViewById(R.id.dato_telef_sim);
        iccsim = (TextView) convertView.findViewById(R.id.dato_icc_sim);
        ubicasim = (TextView) convertView.findViewById(R.id.dato_ubic_sim);
        marcasim = (TextView) convertView.findViewById(R.id.dato_marca_sim);
        placasim = (TextView) convertView.findViewById(R.id.dato_placaimei_sim);
        estadosim = (TextView) convertView.findViewById(R.id.dato_estado_sim);
        observ = (TextView) convertView.findViewById(R.id.dato_observac_sim);

        img_estado = (ImageView) convertView.findViewById(R.id.image_estado_sim);
        img_install_sim = (ImageView) convertView.findViewById(R.id.image_install_sim);

        notifyDataSetChanged();


        // Populate the data into the template view using the data object
        id_simm.setText(dat.getId_sim());
        telefonosim.setText(dat.getTelefono_sim());
        iccsim.setText(dat.getIcc_sim());
        ubicasim.setText(dat.getUbicaion_sim());
        marcasim.setText(dat.getMarca_sim());
        placasim.setText(dat.getPlaca_sim()+" / "+dat.getImei_sim());
       // imeisim.setText(dat.getImei_sim());
        estadosim.setText(dat.getEstado_sim());
        observ.setText(dat.getObservac_sim());


        if(estadosim.getText().equals("Disponible"))
        {
            img_estado.setImageResource(R.drawable.dispon);
            estadosim.setBackgroundColor(Color.rgb(0, 227, 113));
            img_install_sim.setVisibility(convertView.VISIBLE);
        }
        if(estadosim.getText().equals("Instalado"))
        {
            img_estado.setImageResource(R.drawable.instalado);
            estadosim.setBackgroundColor(Color.rgb(100, 225, 220));
            img_install_sim.setVisibility(convertView.INVISIBLE);
        }
        if(estadosim.getText().equals("Desinstalado"))
        {
            img_estado.setImageResource(R.drawable.desinstalar);
            estadosim.setBackgroundColor(Color.rgb(250, 59, 59));
            img_install_sim.setVisibility(convertView.INVISIBLE);
        }
        if(estadosim.getText().equals("Pre-instalado"))
        {
            img_estado.setImageResource(R.drawable.preinst);
            estadosim.setBackgroundColor(Color.rgb(164, 252, 196  ));
            img_install_sim.setVisibility(convertView.INVISIBLE);
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
            for (clase_lectura_sim c : dataSet2) {
                if (c.getTelefono_sim().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    dataSet.add(c);
                }
            }
        }
        notifyDataSetChanged();


    }

}

