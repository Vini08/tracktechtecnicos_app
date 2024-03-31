package com.example.tracktechsopt;


import static com.example.tracktechsopt.LoginActivity3.cheeck_user;
import static com.example.tracktechsopt.LoginActivity3.name_user;
import static com.example.tracktechsopt.MainActivity_lecturas_asignaciones.dpi_transfer_asignacion;

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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tracktech.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.osmdroid.util.GeoPoint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity_lecturas_vehiculos extends AppCompatActivity {
    private static ListView lista;

    public static double originalLat =0;
    public static double originalLng=-0;
    GeoPoint inicio_map;
    TextView tittulo;
    ArrayList<Bitmap> logos_equipos;
    public static ArrayList<String> old_lects = new ArrayList<String>();
    public static ArrayList<clase_lectura_vehiculos> Lista_clientess;
    public static Adapter_Lectura_vehiculo adapter_lectura;
    public static String gpsmodelo_tranfer="", bateria_tranfer="", tipo_transfer="",  fecha_trasfer="", veloc_transfer="", altitud_tranfer="", placa_transfer="", marca_transfer="", modelo_transfer="", imei_transfer="",estado_vehic_transfer="",  conteo="",  fecha_actual="", id_equipo_get="", modelo_get="", marca_get="", placa_get="", tipo_get="", imei_get="";
    public static boolean movim_transfer = false, ignicion_transfer=false;
    View mView_dialog1;

    public static int flag_panico=0;
    public static AlertDialog dialog_loading;



    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_vehiculos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

         Lista_clientess = new ArrayList<>();
        logos_equipos = new ArrayList<>();
        old_lects = new ArrayList<>();
        lista = (ListView) findViewById(R.id.listView_vehiculos);
        tittulo = (TextView) findViewById(R.id.titulo_u_cred_comp);
        SearchView searchView = (SearchView) findViewById(R.id.buscar_cred_comp);

        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(MainActivity_lecturas_vehiculos.this);
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
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
         get_informacion_lecturas identis = new get_informacion_lecturas();
            identis.execute();






//selecciona un cliente y muestra el detalle de cada credito
lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 TextView get_v_imei= (TextView)view.findViewById(R.id.imei_vehic);
                TextView get_v_marca= (TextView)view.findViewById(R.id.dato_marca);
                TextView get_v_modelo= (TextView)view.findViewById(R.id.dato_modelo);
                TextView get_v_placa= (TextView)view.findViewById(R.id.dato_placa);
                TextView get_v_type= (TextView)view.findViewById(R.id.type_vehic);
                TextView get_gps_model= (TextView)view.findViewById(R.id.model_gps_vehic);



                    }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (MainActivity_lecturas_vehiculos.this != null) {
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





    //obtiene ids de los lecturas
    public class get_informacion_lecturas extends AsyncTask<String,String,Bitmap> {
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
placa_get="";
marca_get="";
modelo_get="";
tipo_get="";
imei_get="";
gpsmodelo_tranfer="";

            Lista_clientess.clear();
            int count=0;
            try {
                comando = "SELECT `id_vehiculo`,`Placa`,`Marca`,`Modelo`, TIPO, IMEI, Modelo_gps, estado FROM `vehiculos` WHERE DPI='"+dpi_transfer_asignacion+"'";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    id_equipo_get = res.getString(1);
                    placa_get = res.getString(2);
                    marca_get = res.getString(3);
                    modelo_get = res.getString(4);
                    tipo_get = res.getString(5);
                    imei_get = res.getString(6);
                    gpsmodelo_tranfer = res.getString(7);
                    estado_vehic_transfer = res.getString(8);
                    Lista_clientess.add(new clase_lectura_vehiculos(id_equipo_get,modelo_get,marca_get,placa_get,tipo_get,imei_get,gpsmodelo_tranfer,estado_vehic_transfer));
                    count++;
                }
                connection.close();
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + e.toString());
            }

            /*
            try {
                comando =  "Select COUNT(`id_vehiculo`) FROM vehiculos WHERE `DPI`='"+dpi_cliente+"' and estado='Activo'";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    conteo = res.getString(1);
                }
                connection.close();
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + e.toString());
            }

             */
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (MainActivity_lecturas_vehiculos.this != null) {
               adapter_lectura = new Adapter_Lectura_vehiculo(Lista_clientess, getApplication());
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
            fecha_trasfer="";
            try {
                comando = "SELECT `Latitud`,`Longitud`,`Fecha_gps`,`Velocidad`,`Altitud`, NivelBateria, movimiento, ignicion FROM `ubicaciones_gt` WHERE `IMEI`='"+imei_transfer+"'  order by `id_ubicacion_gt` DESC LIMIT 1";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    originalLat = res.getFloat(1);
                    originalLng = res.getFloat(2);
                    fecha_trasfer = res.getString(3);
                    veloc_transfer = res.getString(4);
                    altitud_tranfer = res.getString(5);
                    bateria_tranfer = res.getString(6);
                    movim_transfer = res.getBoolean(7);
                    ignicion_transfer = res.getBoolean(8);
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
            if (MainActivity_lecturas_vehiculos.this != null) {
                if(!fecha_trasfer.equals("")){
                    startActivity(new Intent(MainActivity_lecturas_vehiculos.this, unidadActivity.class));
                    dialog_loading.dismiss();
                }
                else {
                    dialog_loading.dismiss();
                    FancyToast.makeText(MainActivity_lecturas_vehiculos.this,"Vehículo aún no tiene ubicaciones almacenadas.",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();

                }
            }
        }
    }

}
