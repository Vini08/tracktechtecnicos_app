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

public class Adapter_Lectura_asignacion extends ArrayAdapter<clase_lectura_asignacion> implements View.OnClickListener {
public static ArrayList<clase_lectura_asignacion> dataSet;
 ArrayList<clase_lectura_asignacion> dataSet2;
        Context mContext;
    TextView cliente;
    TextView lugar;
    TextView fecha;
    TextView observacion;
    TextView unidades;
    TextView dpi;

    TextView id_Asig;
    TextView telefono;

    TextView coordenada;
    ImageView img_mapa;
    ImageView img_equipos;
    ImageView img_llamadas;
    ImageView img_vehiculos;
    ImageView img_sim;
    ImageView img_foto;

    ImageView im_cliet, im_lugar, im_fecha, im_observ, im_unidadds, im_tele;
    clase_lectura_asignacion dat;
    @Override
    public void onClick(View view) {

    }


    public Adapter_Lectura_asignacion(ArrayList<clase_lectura_asignacion> data, Context context) {
        super(context, R.layout.item_lista_asignacion, data);
        this.dataSet = data;
        this.mContext=context;
        dataSet2=new ArrayList<clase_lectura_asignacion>();
        dataSet2.addAll(dataSet);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
         dat = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_asignacion, parent, false);
        }
        // Lookup view for data population
        cliente = (TextView) convertView.findViewById(R.id.dato_cliente_asignacion);
        lugar = (TextView) convertView.findViewById(R.id.dato_lugar_asignacion);
        fecha = (TextView) convertView.findViewById(R.id.dato_fecha_asignacion);
        observacion = (TextView) convertView.findViewById(R.id.dato_observ_asignacion);
        unidades = (TextView) convertView.findViewById(R.id.dato_unidades_asignacion);
        telefono = (TextView) convertView.findViewById(R.id.dato_telefono_asignacion);
        id_Asig = (TextView) convertView.findViewById(R.id.pr_id_asignacion);
        dpi = (TextView) convertView.findViewById(R.id.dpi_asignacion);
        coordenada = (TextView) convertView.findViewById(R.id.coordenadas_asignacion);

        img_equipos= (ImageView) convertView.findViewById(R.id.image_equipos_asignacion);
        img_mapa= (ImageView) convertView.findViewById(R.id.image_mapa_asignacion);
        img_vehiculos= (ImageView) convertView.findViewById(R.id.image_unidades_Asignacion);
        img_llamadas= (ImageView) convertView.findViewById(R.id.image_llamar_asignacion);
        img_sim= (ImageView) convertView.findViewById(R.id.image_sim_asignacion);

        im_cliet= (ImageView) convertView.findViewById(R.id.image_cliente);
        im_lugar= (ImageView) convertView.findViewById(R.id.image_lugar);
        im_fecha= (ImageView) convertView.findViewById(R.id.image_fecha);
        im_observ= (ImageView) convertView.findViewById(R.id.image_observacio);
        im_unidadds= (ImageView) convertView.findViewById(R.id.image_unidad);
        im_tele= (ImageView) convertView.findViewById(R.id.image_llamd );
        img_foto= (ImageView) convertView.findViewById(R.id.image_cargar_fotos );

        notifyDataSetChanged();


        // Populate the data into the template view using the data object
        cliente.setText(dat.getClienteasignacion());
        lugar.setText(dat.getLugarasignacion());
        fecha.setText(dat.getFechaasignacion());
        observacion.setText(dat.getObserasignacion());
        unidades.setText(dat.getUnidadesasignacion());
        telefono.setText(dat.getTelefasignacion());
        id_Asig.setText(dat.getIdasignacion());
        dpi.setText(dat.getDpiasignacion());
        coordenada.setText(dat.getUbicaasignacion());

        img_equipos.setImageResource(R.drawable.satelite);
        img_mapa.setImageResource(R.drawable.mapag);
        img_vehiculos.setImageResource(R.drawable.car);
        img_llamadas.setImageResource(R.drawable.llamada);
        img_sim.setImageResource(R.drawable.tarjetasim);

        im_cliet.setImageResource(R.drawable.cliente);
        im_lugar.setImageResource(R.drawable.alfiler);
        im_fecha.setImageResource(R.drawable.calendario);
        im_observ.setImageResource(R.drawable.vision);
        im_unidadds.setImageResource(R.drawable.coche);
        im_tele.setImageResource(R.drawable.viber);
        img_foto.setImageResource(R.drawable.fotografia);




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
            for (clase_lectura_asignacion c : dataSet2) {
                if (c.getClienteasignacion().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    dataSet.add(c);
                }
            }
        }
        notifyDataSetChanged();


    }

}

