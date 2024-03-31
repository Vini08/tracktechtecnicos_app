package com.example.tracktechsopt;


import static com.example.tracktechsopt.LoginActivity3.cheeck_user;
import static com.example.tracktechsopt.LoginActivity3.name_user;
import static com.example.tracktechsopt.MainActivity_lecturas_asignaciones.asign_transfer;
import static com.example.tracktechsopt.MainActivity_lecturas_asignaciones.idasignacion;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tracktech.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity_lecturas_equipos extends AppCompatActivity {
    private static ListView lista;
    public static String[] placas_asignaciones_get;

    public static double originalLat_asig =0;
    public static double originalLng_asig=-0;
    TextView tittulo;
    ArrayList<Bitmap> logos_equipos;
    public static ArrayList<String> old_lects = new ArrayList<String>();
    public static ArrayList<clase_lectura_equipo> Lista_equipos;
    public static Adapter_Lectura_equipo adapter_lectura;
    public static String  placa_get="", telefono_gps_trnasfer="", modelo_gps_tranfer_equipos="", veloc_transfer_equipo="", altitud_tranfer_equipos="", bateria_tranfer_equipo="",  imei_obtenido_equipos="", telef_get="", serie_get="",  fecha_get_equipo="", fecha_actual="", id_equipo_get="", modelo_get_equipos="",  imei_get="", estado_get="";
    public static String placa_find_vehiculos="", marca_find_vehiculos="", modelo_find_vehiculos="", imei_find_vehiculos="", modelogos_find_vehiculos="";

    public static boolean movim_transfer_equipo = false, ignicion_transfer_equipo=false;

    View mView_dialog1;

    public static AlertDialog dialog_loading;
    private static WeakReference<MainActivity_lecturas_equipos> weakReference;



    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_equipos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        weakReference = new WeakReference<>(this);

         Lista_equipos = new ArrayList<>();
        logos_equipos = new ArrayList<>();
        old_lects = new ArrayList<>();
        lista = (ListView) findViewById(R.id.listView_vehiculos);
        tittulo = (TextView) findViewById(R.id.titulo_u_cred_comp);
        SearchView searchView = (SearchView) findViewById(R.id.buscar_cred_comp);

        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(MainActivity_lecturas_equipos.this);
        mView_dialog1 = getLayoutInflater().inflate(R.layout.dialog_cargando, null);
        mBuilder2.setView(mView_dialog1);

        LottieAnimationView lottieAnimationView = mView_dialog1.findViewById(R.id.lottieAnimationView);
        lottieAnimationView.setSpeed(1.6f); // Aquí puedes ajustar el valor según lo necesites (1.0f es la velocidad normal)
        lottieAnimationView.playAnimation();
        dialog_loading = mBuilder2.create();//Set Event



        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#565656"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setElevation(0);

        DateTimeFormatter dtf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            LocalDateTime now = LocalDateTime.now();
            fecha_actual= dtf.format(now);
        }

        //muestra floating en activity tecnico
        if (cheeck_user == 1) {
            tittulo.setText(" Usuario (" + name_user  + ")");
        }
        if(cheeck_user==2){
            tittulo.setText(" Usuario (" + name_user  + ")");
        }
        get_informacion_equipos identis = new get_informacion_equipos();
        identis.execute();


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView bt_ver_img= (ImageView)view.findViewById(R.id.image_ir_punto);
                TextView imei_transfer_equipos= (TextView)view.findViewById(R.id.dato_imei_equipo);
                TextView modelo_transfer_equipos= (TextView)view.findViewById(R.id.dato_modelo_equipo);
                TextView telef= (TextView)view.findViewById(R.id.dato_telef_equipo);

                bt_ver_img.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        imei_obtenido_equipos="";
                        imei_obtenido_equipos=imei_transfer_equipos.getText().toString();
                        modelo_gps_tranfer_equipos=modelo_transfer_equipos.getText().toString();
                        telefono_gps_trnasfer=telef.getText().toString();
                        get_informacion_unidad identis = new get_informacion_unidad();
                        identis.execute();
                    }
                });

            }
        });




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (MainActivity_lecturas_equipos.this != null) {
                    adapter_lectura.filter(newText);
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }





    //obtiene ids de los lecturas de equipos
    public static class get_informacion_equipos extends AsyncTask<String,String,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog_loading.show();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";
            conexion obj_DB_Connectio = new conexion();
            Connection connection=obj_DB_Connectio.get_connection();
            id_equipo_get="";
            modelo_get_equipos="";
            serie_get="";
            imei_get="";

            Lista_equipos.clear();
            int count=0;
            try {
                comando = "SELECT id_equipo,`IMEI`, `Serie-ID`, `Modelo`, telefono, estado, placa FROM `equipos` WHERE n_asignacion='"+asign_transfer+"'";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    id_equipo_get = res.getString(1);
                    imei_get = res.getString(2);
                    serie_get = res.getString(3);
                    modelo_get_equipos = res.getString(4);
                    telef_get = res.getString(5);
                    estado_get = res.getString(6);
                    placa_get = res.getString(7);
                    Lista_equipos.add(new clase_lectura_equipo(id_equipo_get,imei_get,serie_get,modelo_get_equipos,telef_get,estado_get,placa_get));
                    count++;
                }
             } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + e.toString());
            }
            try {
                comando = "SELECT Placa FROM `vehiculos` WHERE n_asignacion='"+idasignacion+"' and `Estado`='Pendiente'";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);
                res.last();
                int rowCount = res.getRow();
                res.beforeFirst();
                placas_asignaciones_get = new String[rowCount+1];
                int i = 0;
                placas_asignaciones_get[i] = "- - -";

                while (res.next()){
                    placas_asignaciones_get[i+1] = res.getString("Placa");
                    i++;
                }
                connection.close();
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            MainActivity_lecturas_equipos activityInstance = weakReference.get();
            if (activityInstance != null) {
               adapter_lectura = new Adapter_Lectura_equipo(Lista_equipos, activityInstance);
                lista.setAdapter(adapter_lectura);
                lista.requestLayout();
                dialog_loading.dismiss();
            }
        }
    }


    public  class get_informacion_unidad extends AsyncTask<String,String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog_loading.show();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";
            conexion obj_DB_Connectio = new conexion();
            Connection connection=obj_DB_Connectio.get_connection();
            fecha_get_equipo="";
            placa_find_vehiculos="";
            marca_find_vehiculos="";
            modelo_find_vehiculos="";
            imei_find_vehiculos="";
            modelogos_find_vehiculos="";

            try {
                comando = "SELECT `Latitud`,`Longitud`,`Fecha_gps`,`Velocidad`,`Altitud`, NivelBateria, movimiento, ignicion FROM `ubicaciones_gt` WHERE `IMEI`='"+imei_obtenido_equipos+"'  order by `id_ubicacion_gt` DESC LIMIT 1";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    originalLat_asig = res.getFloat(1);
                    originalLng_asig = res.getFloat(2);
                    fecha_get_equipo = res.getString(3);
                    veloc_transfer_equipo = res.getString(4);
                    altitud_tranfer_equipos = res.getString(5);
                    bateria_tranfer_equipo = res.getString(6);
                    movim_transfer_equipo = res.getBoolean(7);
                    ignicion_transfer_equipo = res.getBoolean(8);
                }
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "SELECT `Placa`,`Marca`,`Modelo`,`IMEI`,`Modelo_gps` FROM `vehiculos` WHERE `IMEI`='"+imei_obtenido_equipos+"' ";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    placa_find_vehiculos = res.getString(1);
                    marca_find_vehiculos = res.getString(2);
                    modelo_find_vehiculos = res.getString(3);
                    imei_find_vehiculos = res.getString(4);
                    modelogos_find_vehiculos = res.getString(5);
                }
                connection.close();
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (MainActivity_lecturas_equipos.this != null) {
                if(!fecha_get_equipo.equals("")){
                    startActivity(new Intent(MainActivity_lecturas_equipos.this, unidadActivity.class));
                }
                else {
                    dialog_loading.dismiss();
                    FancyToast.makeText(MainActivity_lecturas_equipos.this,"Vehículo aún no tiene ubicaciones almacenadas.",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
                }
            }
        }
    }
}
