package com.example.tracktechsopt;


import static com.example.tracktechsopt.LoginActivity3.name_user;
import static com.example.tracktechsopt.MainActivity_lecturas_asignaciones.asign_transfer;
import static com.example.tracktechsopt.MainActivity_lecturas_asignaciones.dpi_transfer_asignacion;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.dialog_loading;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.fecha_get_equipo;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.imei_find_vehiculos;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.imei_obtenido_equipos;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.marca_find_vehiculos;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.modelo_find_vehiculos;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.modelo_gps_tranfer_equipos;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.originalLat_asig;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.originalLng_asig;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.placa_find_vehiculos;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.telefono_gps_trnasfer;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.veloc_transfer_equipo;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tracktech.BuildConfig;
import com.example.tracktech.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class unidadActivity extends AppCompatActivity implements MapEventsReceiver {
     private MapController myMapController;
    public  static  MapView map;
    Spinner spinner_unidad;
    public static   TextView datos_carro, Adress1, Velocc, data_gps, Adress2, Adress3, date_ACtl;
    String locality="", pais="";
     Marker marker;
      Button bt_apagar_carro;
      Button bt_encender_carro;

      Button panicos_btn;

    public static String[] placas_asignaciones_get;

    private static int ANIMATION_DURATION = 1100; // Duración de la animación en milisegundos
    public static double CIRCLE_RADIUS = 0.0005;
    public  static Polygon polygon;
    public static boolean isAnimatorListenerActive = false;
    public static String estado_prueba_tecnico="", fecha_actual="", result = "", texto_observacion="", texto_observacion_sim="", get_placa_unidad="- - -", imei_panico="",lat_panico="", longi_panico="", fecha_press_panico="",imei_apagado="",lat_apagado="", longi_apagado="", fecha_press_apagado="";

    ArrayList<String> listadata_direcion1 = new ArrayList<>();
    ArrayList<String> listadata_direcion2 = new ArrayList<>();
    Button confirm_panicos = null;

    Button confirmar_apagado;
    Button asignar_equipo = null, removersim=null, removergps=null;


    TextView recibir_datos_sos;
    TextView recibir_datos_apagado;

    TextView esperar, esperar_apagado;
    conexion obj_DB_Connectio = new conexion();
    Connection connection=obj_DB_Connectio.get_connection();
    String Fecha_Actual="";
    AlertDialog dialog_alert_users;
    AlertDialog dialog_alert_apagadomotor;
    AlertDialog dialog_remove_gps;
    AlertDialog dialog_remove_sim;

    public static int check=2, check_apagado=2;
TextView selec_unidad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidad);
        getSupportActionBar().hide();
        Window window = getWindow();
        window.setNavigationBarColor(getResources().getColor(R.color.colorB));

        Context ctx = getApplicationContext();
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        obj_DB_Connectio = new conexion();
        connection=obj_DB_Connectio.get_connection();

        data_gps = (TextView) findViewById(R.id.gps_data_txt);
        Adress1 = (TextView) findViewById(R.id.tvCurrentLocation);
        Adress2 = (TextView) findViewById(R.id.tvAddress);
        Adress3 = (TextView) findViewById(R.id.tvAddress2);
        Velocc = (TextView) findViewById(R.id.velocidad_unidad_equipo);
        date_ACtl = (TextView) findViewById(R.id.actualiza);
        datos_carro = (TextView) findViewById(R.id.datos_unidad_envista);
        selec_unidad = (TextView) findViewById(R.id.label_selecc_unidad);

        spinner_unidad = (Spinner) findViewById(R.id.unidades_to_gps);
        bt_apagar_carro = (Button) findViewById(R.id.bt_apagar);
        bt_encender_carro = (Button) findViewById(R.id.bt_encedr);
        asignar_equipo = (Button) findViewById(R.id.bt_asig_gps);
        panicos_btn = (Button) findViewById(R.id.bt_recibir_panico);
        removergps = (Button) findViewById(R.id.bt_remover_equipo);
        removersim = (Button) findViewById(R.id.bt_remover_sim);

        DateTimeFormatter dtf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            LocalDateTime now = LocalDateTime.now();
            fecha_actual= dtf.format(now);
        }

        AlertDialog.Builder mBuilderRemover = new AlertDialog.Builder(unidadActivity.this);
        View mViewRemover = getLayoutInflater().inflate(R.layout.dialog_remover_gps, null);
        mBuilderRemover.setView(mViewRemover);
        dialog_remove_gps = mBuilderRemover.create();

        AlertDialog.Builder mBuilderRemoverSim = new AlertDialog.Builder(unidadActivity.this);
        View mViewRemoverSim = getLayoutInflater().inflate(R.layout.dialog_remover_sim, null);
        mBuilderRemoverSim.setView(mViewRemoverSim);
        dialog_remove_sim = mBuilderRemoverSim.create();


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(unidadActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_recibir_panico, null);
        mBuilder.setView(mView);
        dialog_alert_users = mBuilder.create();

        AlertDialog.Builder mBuilder_mt = new AlertDialog.Builder(unidadActivity.this);
        View mView_mt = getLayoutInflater().inflate(R.layout.dialog_recibir_apagado, null);
        mBuilder_mt.setView(mView_mt);
        dialog_alert_apagadomotor = mBuilder_mt.create();


        get_address_ruta addres = new get_address_ruta();
        addres.execute();



        GeoPoint inicio = new GeoPoint(originalLat_asig, originalLng_asig);
        map = findViewById(R.id.mapViewX);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(true);
        myMapController = (MapController) map.getController();
        myMapController.setCenter(inicio);
        myMapController.setZoom(17);

        polygon = new Polygon();
        polygon.setFillColor(Color.TRANSPARENT);
        polygon.setStrokeColor(Color.CYAN);
        polygon.setStrokeWidth(15f); // Establecer el ancho del contorno en píxeles
        map.getOverlays().add(polygon);

        getLocationMarker();
        startCircleAnimation();

//boton para remover gps
        removergps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_remove_gps.show();
                Button back_remover_gps;
                Button confirmar_remover_gps;
                EditText observacion;

                back_remover_gps = (Button) mViewRemover.findViewById(R.id.back_session_remove_gps);
                confirmar_remover_gps = (Button) mViewRemover.findViewById(R.id.boton_remover_gps);
                observacion = (EditText) mViewRemover.findViewById(R.id.obser_remove_gpss);


                back_remover_gps.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        dialog_remove_gps.dismiss();
                    }
                });


                confirmar_remover_gps.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        texto_observacion = observacion.getText().toString().trim(); // Obtener el texto ingresado y eliminar espacios en blanco

                        if (!texto_observacion.isEmpty()) {
                            remover_equipo asig = new remover_equipo();
                            asig.execute();
                        }
                        else {
                            FancyToast.makeText(unidadActivity.this,"Debe de ingresar un motivo.",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();

                        }
                    }
                });

            }
        });


        //boton para remover sim
        removersim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_remove_sim.show();
                Button back_remover_sim;
                Button confirmar_remover_sim;
                EditText observacion_sim;

                back_remover_sim = (Button) mViewRemoverSim.findViewById(R.id.back_session_remove_sim);
                confirmar_remover_sim = (Button) mViewRemoverSim.findViewById(R.id.boton_remover_sim);
                observacion_sim = (EditText) mViewRemoverSim.findViewById(R.id.obser_remove_sim);


                back_remover_sim.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog_remove_gps.dismiss();
                    }
                });


                confirmar_remover_sim.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        texto_observacion_sim = observacion_sim.getText().toString().trim(); // Obtener el texto ingresado y eliminar espacios en blanco

                        if (!texto_observacion_sim.isEmpty()) {
                            remover_sim asig = new remover_sim();
                            asig.execute();
                        }
                        else {
                            FancyToast.makeText(unidadActivity.this,"Debe de ingresar un motivo.",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();

                        }
                    }
                });

            }
        });

        //boton para apagar carro
        bt_apagar_carro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                float veloc = Float.parseFloat(Velocc.getText().toString());
                if (veloc > 15)
                {
                    FancyToast.makeText(unidadActivity.this,"No se puede apagar porque velocidad de vehículo es mayor a 15km/h ",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
                }
                else {
                    SendPostRequest_apagar ssend=new SendPostRequest_apagar();
                    ssend.execute();

                    check_apagado=2;
                    obj_DB_Connectio = new conexion();
                    connection=obj_DB_Connectio.get_connection();
                    dialog_alert_apagadomotor.show();

                    bt_apagar_carro.setText("APAGADO ON");
                    bt_encender_carro.setText("ENCENDIDO OFF");


                    Button back_panicos;
                    confirmar_apagado = (Button) mView_mt.findViewById(R.id.confirm_apagado);
                    recibir_datos_apagado= (TextView) mView_mt.findViewById(R.id.data_apagado_receiver);
                    esperar_apagado = (TextView) mView_mt.findViewById(R.id.v1_wait_apagado);

                    confirmar_apagado.setVisibility(View.INVISIBLE);
                    recibir_datos_apagado.setVisibility(View.INVISIBLE);

                    back_panicos = (Button) mView_mt.findViewById(R.id.back_apagado);
                    back_panicos.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            check_apagado=1;
                            try{
                                connection.close();
                            }catch(SQLException e){
                                Log.e("- - - - -  ERROR1", "Cerrada conex:  - - - - -- - - -" + e.toString());
                            }
                            dialog_alert_apagadomotor.dismiss();
                        }
                    });

                    confirmar_apagado.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            SendPostRequest_encender ssend=new SendPostRequest_encender();
                            ssend.execute();

                            confirmar_apagado panic = new confirmar_apagado();
                            panic.execute();

                            bt_apagar_carro.setText("APAGADO OFF");
                            bt_encender_carro.setText("ENCENDIDO ON");
                        }
                    });
                }
            }
        });


    //boton para encender carro
        bt_encender_carro.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {

                SendPostRequest_encender ssend=new SendPostRequest_encender();
                ssend.execute();
                bt_apagar_carro.setText("APAGADO OFF");
                bt_encender_carro.setText("ENCENDIDO ON");

        }
    });


        //boton para asignar gps a carro
        asignar_equipo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              if(!get_placa_unidad.equals("- - -")) {
                asignar_equipos asig = new asignar_equipos();
                asig.execute();
                }
              else{
                  FancyToast.makeText(getApplicationContext(),"Debe seleccionar una placa.",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();

              }
            }
        });


        //boton para ver panicos
        panicos_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                check=2;
                obj_DB_Connectio = new conexion();
                connection=obj_DB_Connectio.get_connection();
                dialog_alert_users.show();

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Fecha_Actual= sdf.format(date);

                get_info_panicos get_data = new get_info_panicos();
                get_data.execute();

                Button back_panicos;
                confirm_panicos = (Button) mView.findViewById(R.id.confirm_panico);
                recibir_datos_sos = (TextView) mView.findViewById(R.id.data_panico_receiver);
                esperar = (TextView) mView.findViewById(R.id.v1_wait);

                confirm_panicos.setVisibility(View.INVISIBLE);
                recibir_datos_sos.setVisibility(View.INVISIBLE);

                back_panicos = (Button) mView.findViewById(R.id.back_panic);
                back_panicos.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        check=1;
                        try{
                            connection.close();
                        }catch(SQLException e){
                            Log.e("- - - - -  ERROR1", "Cerrada conex:  - - - - -- - - -" + e.toString());
                        }
                        dialog_alert_users.dismiss();
                    }
                });

                confirm_panicos.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                     confirmar_panico panic = new confirmar_panico();
                     panic.execute();
                    }
                });
            }
        });


        //selecionar spinner de modedlo
        spinner_unidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                get_placa_unidad = spinner_unidad.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
}

    public class get_info_panicos extends AsyncTask<String,String,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";

            imei_panico="";
            lat_panico="";
            longi_panico="";
            fecha_press_panico="";

            try {
                comando = "SELECT * FROM listado_panicos WHERE fecha_presionado BETWEEN DATE_SUB('"+Fecha_Actual+"', INTERVAL 4 MINUTE) \n" +
                        "    AND \n" +
                        "    DATE_ADD('"+Fecha_Actual+"', INTERVAL 2 MINUTE) and `IMEI`='"+imei_obtenido_equipos+"';";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    imei_panico = res.getString(2);
                    lat_panico = res.getString(3);
                    longi_panico = res.getString(4);
                    fecha_press_panico = res.getString(5);

                }
             } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (unidadActivity.this != null) {
                //si imei esta vacio pasa al else de lo contrario muestra el dato del panico enviado
                if(!imei_panico.equals("")){
                    confirm_panicos.setVisibility(View.VISIBLE);
                    recibir_datos_sos.setVisibility(View.VISIBLE);
                    esperar.setVisibility(View.INVISIBLE);
                    recibir_datos_sos.setText("SE RECIBIÓ EL SIGUIENTE PÁNICO\n "+fecha_press_panico);
                    try{
                    connection.close();
                    }catch(SQLException e){
                        Log.e("- - - - -  ERROR1", "Cerrada conex:  - - - - -- - - -" + e.toString());
                    }
                    insertar_evento_unidad inser_event = new insertar_evento_unidad();
                    inser_event.execute();
                }
                else{
                //si check sigue siendo 2 vuelve a repetir el metodo para seguir buscando si hay panicos nuevos
                if(check!=1) {
                    get_info_panicos get_data = new get_info_panicos();
                    get_data.execute();
                }
                else{
                    try{
                        connection.close();
                    }catch(SQLException e){
                        Log.e("- - - - -  ERROR1", "Cerrada conex:  - - - - -- - - -" + e.toString());
                    }
                }
                }

            }
        }
    }



    public class confirmar_panico extends AsyncTask<String,String,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";
            conexion obj_DB_Connectio=new conexion();
            Connection connection=obj_DB_Connectio.get_connection();

            try {
                comando = "INSERT INTO `detalle_asignaciones` (`n_asignacion`, `DPI`, `IMEI`, `Unidad`, `Fecha`, `Tecnico`, `Estado`) values(?,?,?,?,?,?,?)";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, asign_transfer);
                pstmt.setString(2, dpi_transfer_asignacion);
                pstmt.setString(3, imei_obtenido_equipos);
                pstmt.setString(4, "Pendiente");
                pstmt.setString(5, fecha_actual);
                pstmt.setString(6, name_user);
                pstmt.setString(7, "Botón de pánico");
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert detall_asig:  - - - - -- - - -" + e.toString());
            }
            try {
                comando = "INSERT INTO `pruebas_tecnicos` (`IMEI`,`latitud`,`longitud`,`fecha_presionado`,`Estado`,`id_asignacion`) values(?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, imei_panico);
                pstmt.setString(2, lat_panico);
                pstmt.setString(3, longi_panico);
                pstmt.setString(4, fecha_press_panico);
                pstmt.setString(5, "Confirmado_panico");
                pstmt.setString(6,asign_transfer);
                pstmt.execute();
                connection.close();
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (unidadActivity.this != null) {
                check=2;
                dialog_alert_users.dismiss();
                FancyToast.makeText(getApplicationContext(),"Evento se registró con éxito.",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
            }
        }
    }


    public class obtener_apagados_enviados extends AsyncTask<String,String,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";

            imei_apagado="";
            lat_apagado="";
            longi_apagado="";
            fecha_press_apagado="";

            try {
                comando = "SELECT * FROM listado_apagadomotor WHERE fecha_presionado BETWEEN DATE_SUB('"+Fecha_Actual+"', INTERVAL 4 MINUTE) \n" +
                        "    AND \n" +
                        "    DATE_ADD('"+Fecha_Actual+"', INTERVAL 2 MINUTE) and `IMEI`='"+imei_obtenido_equipos+"';";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    imei_apagado = res.getString(2);
                    lat_apagado = res.getString(3);
                    longi_apagado = res.getString(4);
                    fecha_press_apagado = res.getString(5);

                }
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + e.toString());
            }
            Log.e("- - - - -  ERROR1", "ENTRA IN APAGADO:  - - - - -- - - -" + imei_apagado+ "   "+check_apagado);

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (unidadActivity.this != null) {
                if(!imei_apagado.equals("")){
                    recibir_datos_apagado.setText("SE RECIBIÓ EL SIGUIENTE APAGADO\n "+fecha_press_apagado);
                    confirmar_apagado.setVisibility(View.VISIBLE);
                    recibir_datos_apagado.setVisibility(View.VISIBLE);
                    esperar_apagado.setVisibility(View.INVISIBLE);
                    try{
                        connection.close();
                    }catch(SQLException e){
                        Log.e("- - - - -  ERROR1", "Cerrada conex:  - - - - -- - - -" + e.toString());
                    }

                    insertar_evento_unidad inser_event = new insertar_evento_unidad();
                    inser_event.execute();
                }
                else{
                    if(check_apagado!=1) {
                        obtener_apagados_enviados get_data = new obtener_apagados_enviados();
                        get_data.execute();
                    }
                    else{
                        try{
                        connection.close();
                        }catch(SQLException e){
                            Log.e("- - - - -  ERROR1", "Cerrada conex:  - - - - -- - - -" + e.toString());
                        }
                    }
                }

            }
        }
    }


    public class confirmar_apagado extends AsyncTask<String,String,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";
            conexion obj_DB_Connectio=new conexion();
            Connection connection=obj_DB_Connectio.get_connection();

            try {
                comando = "INSERT INTO `detalle_asignaciones` (`n_asignacion`, `DPI`, `IMEI`, `Unidad`, `Fecha`, `Tecnico`, `Estado`) values(?,?,?,?,?,?,?)";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, asign_transfer);
                pstmt.setString(2, dpi_transfer_asignacion);
                pstmt.setString(3, imei_obtenido_equipos);
                pstmt.setString(4, "Pendiente");
                pstmt.setString(5, fecha_actual);
                pstmt.setString(6, name_user);
                pstmt.setString(7, "Apagado de motor");
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert detall_asig:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "INSERT INTO `pruebas_tecnicos` (`IMEI`,`latitud`,`longitud`,`fecha_presionado`,`Estado`,`id_asignacion`) values(?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, imei_apagado);
                pstmt.setString(2, lat_apagado);
                pstmt.setString(3, longi_apagado);
                pstmt.setString(4, fecha_press_apagado);
                pstmt.setString(5, "Confirmado_apagado");
                pstmt.setString(6,asign_transfer);
                pstmt.execute();
                connection.close();
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (unidadActivity.this != null) {
                check_apagado=2;
                dialog_alert_apagadomotor.dismiss();
                FancyToast.makeText(getApplicationContext(),"Evento se registró con éxito.",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
            }
        }
    }


    public class asignar_equipos extends AsyncTask<String,String,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";
            conexion obj_DB_Connectio=new conexion();
            Connection connection=obj_DB_Connectio.get_connection();

            try {
                comando = "UPDATE asignacion_trabajos set `Estado_asignacion`=? WHERE n_asignacion= ? and tecnico= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, "Fin "+get_placa_unidad);
                pstmt.setString(2, asign_transfer);
                pstmt.setString(3, name_user);
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "update state asig:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "INSERT INTO `detalle_asignaciones` (`n_asignacion`, `DPI`, `IMEI`, `Unidad`, `Fecha`, `Tecnico`, `Estado`) values(?,?,?,?,?,?,?)";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, asign_transfer);
                pstmt.setString(2, dpi_transfer_asignacion);
                pstmt.setString(3, imei_obtenido_equipos);
                pstmt.setString(4, get_placa_unidad);
                pstmt.setString(5, fecha_actual);
                pstmt.setString(6, name_user);
                pstmt.setString(7, "GPS instalado.");
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert detall_asig:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE vehiculos set `IMEI`=?,`Modelo_gps`=?,`Estado`=? WHERE Placa= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, imei_obtenido_equipos);
                pstmt.setString(2, modelo_gps_tranfer_equipos);
                pstmt.setString(3, "Instalado");
                pstmt.setString(4, get_placa_unidad);
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE sims set `Ubicacion`=?,`Estado`=?,`Placa`=?,`IMEI`=?, observacion=?   WHERE telefono= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, "En_unidad");
                pstmt.setString(2, "Instalado");
                pstmt.setString(3, get_placa_unidad);
                pstmt.setString(4, imei_obtenido_equipos);
                pstmt.setString(5, "-");
                pstmt.setString(6, telefono_gps_trnasfer);
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE equipos set `Ubicacion`=?,`Estado`=?,`Placa`=?, observacion=? WHERE IMEI= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, "En_unidad");
                pstmt.setString(2, "Instalado");
                pstmt.setString(3, get_placa_unidad);
                pstmt.setString(4, "-");
                pstmt.setString(5, imei_obtenido_equipos);
                pstmt.execute();
                connection.close();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (unidadActivity.this != null) {
                MainActivity_lecturas_equipos.get_informacion_equipos identis = new MainActivity_lecturas_equipos.get_informacion_equipos();
                identis.execute();
                FancyToast.makeText(getApplicationContext(),"El GPS fue instalado con éxito.",FancyToast.LENGTH_LONG, FancyToast.INFO,true).show();
                finish();
            }
        }
    }




    public class remover_equipo extends AsyncTask<String,String,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";
            conexion obj_DB_Connectio=new conexion();
            Connection connection=obj_DB_Connectio.get_connection();

            try {
                comando = "INSERT INTO `detalle_asignaciones` (`n_asignacion`, `DPI`, `IMEI`, `Unidad`, `Fecha`, `Tecnico`, `Estado`) values(?,?,?,?,?,?,?)";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, asign_transfer);
                pstmt.setString(2, dpi_transfer_asignacion);
                pstmt.setString(3, imei_obtenido_equipos);
                pstmt.setString(4, placa_find_vehiculos);
                pstmt.setString(5, fecha_actual);
                pstmt.setString(6, name_user);
                pstmt.setString(7, "Removió GPS");
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert detall_asig:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE vehiculos set `IMEI`=?,`Modelo_gps`=?,`Estado`=?, SIM=? WHERE Placa= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, "-");
                pstmt.setString(2, "-");
                pstmt.setString(3, "Pendiente");
                pstmt.setString(4, "Pendiente");
                pstmt.setString(5, placa_find_vehiculos);
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE sims set `Ubicacion`=?,`Estado`=?,`Placa`=?,`IMEI`=?, observacion=?  WHERE telefono= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, "En_mano");
                pstmt.setString(2, "Desinstalado");
                pstmt.setString(3, placa_find_vehiculos);
                pstmt.setString(4, imei_obtenido_equipos);
                pstmt.setString(5, texto_observacion);
                pstmt.setString(6, telefono_gps_trnasfer);
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE equipos set `Ubicacion`=?,`Estado`=?,`Placa`=?, observacion=?  WHERE IMEI= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, "En_mano");
                pstmt.setString(2, "Desinstalado");
                pstmt.setString(3, placa_find_vehiculos);
                pstmt.setString(4, texto_observacion);
                pstmt.setString(5, imei_obtenido_equipos);
                pstmt.execute();
                connection.close();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (unidadActivity.this != null) {
                MainActivity_lecturas_equipos.get_informacion_equipos identis = new MainActivity_lecturas_equipos.get_informacion_equipos();
                identis.execute();
                FancyToast.makeText(getApplicationContext(),"El GPS fue desinstalado con éxito.",FancyToast.LENGTH_LONG, FancyToast.INFO,true).show();
                dialog_remove_gps.dismiss();
                finish();
            }
        }
    }







    public class remover_sim extends AsyncTask<String,String,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";
            conexion obj_DB_Connectio=new conexion();
            Connection connection=obj_DB_Connectio.get_connection();

            try {
                comando = "INSERT INTO `detalle_asignaciones` (`n_asignacion`, `DPI`, `IMEI`, `Unidad`, `Fecha`, `Tecnico`, `Estado`) values(?,?,?,?,?,?,?)";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, asign_transfer);
                pstmt.setString(2, dpi_transfer_asignacion);
                pstmt.setString(3, imei_obtenido_equipos);
                pstmt.setString(4, placa_find_vehiculos);
                pstmt.setString(5, fecha_actual);
                pstmt.setString(6, name_user);
                pstmt.setString(7, "Removió SIM");
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert detall_asig:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE vehiculos set SIM=? WHERE Placa= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                pstmt.setString(1, "Pendiente");
                pstmt.setString(2, placa_find_vehiculos);
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE sims set `Ubicacion`=?,`Estado`=?, observacion=?  WHERE telefono= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, "En_mano");
                pstmt.setString(2, "Desinstalado");
                pstmt.setString(3,texto_observacion_sim );
                pstmt.setString(4, telefono_gps_trnasfer);
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE equipos set `Telefono`=?,`ICC`=?, observacion=?  WHERE IMEI= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, "0");
                pstmt.setString(2, "0");
                pstmt.setString(3, texto_observacion_sim);
                pstmt.setString(4, imei_obtenido_equipos);
                pstmt.execute();
                connection.close();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (unidadActivity.this != null) {
                MainActivity_lecturas_equipos.get_informacion_equipos identis = new MainActivity_lecturas_equipos.get_informacion_equipos();
                identis.execute();
                FancyToast.makeText(getApplicationContext(),"El SIM fue desinstalado con éxito.",FancyToast.LENGTH_LONG, FancyToast.INFO,true).show();
                dialog_remove_gps.dismiss();
                finish();
            }
        }
    }



    public class insertar_evento_unidad extends AsyncTask<String,String,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";
            conexion obj_DB_Connectio=new conexion();
            Connection connection=obj_DB_Connectio.get_connection();




            try {
                comando = "UPDATE asignacion_trabajos set `Estado_asignacion`=? WHERE n_asignacion= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, "Pruebas de tecnico");
                pstmt.setString(2, asign_transfer);
                pstmt.execute();
                connection.close();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "update state asig:  - - - - -- - - -" + e.toString());
            }



            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

        }
    }



    public void getLocationMarker() {
    GeoPoint markerPoint = new GeoPoint(originalLat_asig, originalLng_asig);
    marker = new Marker(map);
    marker.setPosition(markerPoint);
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
    marker.setTitle(imei_obtenido_equipos);
    marker.setInfoWindow(new custom_detalle_ubicacion(map));
        Bitmap carIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_car_gps);
        Bitmap rotatedCarIcon = rotateBitmap(carIcon, 0); // Ángulo de rotación en grados

        marker.setIcon(new BitmapDrawable(getResources(), rotatedCarIcon));

        map.getOverlays().add(marker);
    map.invalidate();
}


    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void startCircleAnimation() {
        // Crear un ValueAnimator para la animación del contorno
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(ANIMATION_DURATION);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (isAnimatorListenerActive) {
                    // Obtener el valor actualizado de la animación

                    float animationValue = (float) valueAnimator.getAnimatedValue();

                    // Actualizar el radio del círculo en función del valor de animación
                    double newRadius = CIRCLE_RADIUS * animationValue;
                    List<GeoPoint> circlePoints = generateCirclePoints(marker.getPosition(), newRadius);
                    polygon.setPoints(circlePoints);

                    // Actualizar el color del contorno en función del valor de animación
                    int newStrokeColor = interpolateColor(Color.CYAN, Color.BLUE, animationValue);
                    polygon.setStrokeColor(newStrokeColor);

                    map.invalidate();
                }
            }
        });

        animator.start();
    }

    // Generar puntos de un círculo alrededor de un centro dado
    private List<GeoPoint> generateCirclePoints(GeoPoint center, double radius) {
        List<GeoPoint> circlePoints = new ArrayList<>();

        int numberOfPoints = 100;
        double angle;
        double angleIncrement = (2 * Math.PI) / numberOfPoints;

        for (int i = 0; i < numberOfPoints; i++) {
            angle = i * angleIncrement;
            double latitude = center.getLatitude() + (radius * Math.cos(angle));
            double longitude = center.getLongitude() + (radius * Math.sin(angle));
            circlePoints.add(new GeoPoint(latitude, longitude));
        }

        return circlePoints;
    }

    // Función para interpolar entre dos colores
    private int interpolateColor(int startColor, int endColor, float fraction) {
        int startAlpha = Color.alpha(startColor);
        int startRed = Color.red(startColor);
        int startGreen = Color.green(startColor);
        int startBlue = Color.blue(startColor);

        int endAlpha = Color.alpha(endColor);
        int endRed = Color.red(endColor);
        int endGreen = Color.green(endColor);
        int endBlue = Color.blue(endColor);

        int interpolatedAlpha = (int) (startAlpha + fraction * (endAlpha - startAlpha));
        int interpolatedRed = (int) (startRed + fraction * (endRed - startRed));
        int interpolatedGreen = (int) (startGreen + fraction * (endGreen - startGreen));
        int interpolatedBlue = (int) (startBlue + fraction * (endBlue - startBlue));

        return Color.argb(interpolatedAlpha, interpolatedRed, interpolatedGreen, interpolatedBlue);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isAnimatorListenerActive = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        isAnimatorListenerActive = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint geoPoint) {
        return false;
    }

    public static List<Coordinate> getIntermediateCoordinates(double lat1, double lon1, double lat2, double lon2, int numWaypoints) {
        List<Coordinate> intermediateCoordinates = new ArrayList<>();
        double dLat = (lat2 - lat1) / (numWaypoints + 1);
        double dLon = (lon2 - lon1) / (numWaypoints + 1);

        for (int i = 1; i <= numWaypoints; i++) {
            double lat = lat1 + i * dLat;
            double lon = lon1 + i * dLon;
            intermediateCoordinates.add(new Coordinate(lat, lon));
        }

        return intermediateCoordinates;
    }

    public static class Coordinate {
        private double latitude;
        private double longitude;

        public Coordinate(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public class get_address_ruta extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String API_KEY = "AIzaSyCYtMEurr3A68Cu0y7uXS66NAsfaIkVBrw";

            String comando = "";
            conexion obj_DB_Connectio = new conexion();
            Connection connection2=obj_DB_Connectio.get_connection();
            try {
                comando = "UPDATE asignacion_trabajos set `Estado_asignacion`=? WHERE n_asignacion= ?";
                PreparedStatement pstmt = connection2.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, "Técnico inicia");
                pstmt.setString(2, asign_transfer);
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "update state asig:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "SELECT Placa FROM `vehiculos` WHERE n_asignacion='"+asign_transfer+"' and `Estado`='Pendiente'";
                Statement s = null;
                s = connection2.prepareStatement(comando);
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
                connection2.close();
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + e.toString());
            }

            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                        originalLat_asig + "," + originalLng_asig + "&key=" + API_KEY);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (unidadActivity.this != null) {
                listadata_direcion2.clear();
                String direccion_datos1="";
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(unidadActivity.this, android.R.layout.simple_spinner_item, placas_asignaciones_get);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinner_unidad.setAdapter(adapter1);

                 try {
                    JSONObject json = new JSONObject(result);

                    if (json.has("results")) {
                        JSONArray resultsArray = json.getJSONArray("results");
                        if (resultsArray.length() > 0) {
                            JSONObject firstResult = resultsArray.getJSONObject(0);
                            if (firstResult.has("address_components")) {
                                JSONArray addressComponentsArray = firstResult.getJSONArray("address_components");
                                for (int i = 0; i < addressComponentsArray.length(); i++) {
                                    JSONObject component = addressComponentsArray.getJSONObject(i);
                                    if (component.has("short_name") && component.has("long_name")) {
                                        String shortName = component.getString("short_name");
                                        String longName = component.getString("long_name");
                                        listadata_direcion2.add(shortName);}
                                }
                            }
                        }
                            JSONArray resultsArrays = json.getJSONArray("results");
                            for (int i = 0; i < resultsArrays.length(); i++) {
                                JSONObject results = resultsArrays.getJSONObject(i);
                                if (results.has("formatted_address")) {
                                    direccion_datos1 = results.getString("formatted_address");
                                         System.out.println("Formatted Address: " + direccion_datos1);
                                        listadata_direcion1.add(direccion_datos1);
                                 }
                            }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Adress1.setText(listadata_direcion1.get(0));
                Adress2.setText(listadata_direcion2.get(2)+", "+listadata_direcion2.get(3));
                Adress3.setText(listadata_direcion1.get(3)+"   "+veloc_transfer_equipo+"km/h");
                date_ACtl.setText("Actualización: "+fecha_get_equipo);
                data_gps.setText(imei_obtenido_equipos+"  /  "+telefono_gps_trnasfer);
                Velocc.setText(veloc_transfer_equipo);

                if(telefono_gps_trnasfer.equals("0")){
                removersim.setVisibility(View.INVISIBLE);
                }
                if(imei_find_vehiculos.equals("")){
                    datos_carro.setText("GPS no ha sido asignado a ningún vehículo.");
                    removergps.setVisibility(View.INVISIBLE);
                    removersim.setVisibility(View.INVISIBLE);
                }
                if(!imei_find_vehiculos.equals("")){
                    datos_carro.setText("GPS asignado a: "+placa_find_vehiculos+" / "+marca_find_vehiculos+" / "+modelo_find_vehiculos);
                    spinner_unidad.setVisibility(View.INVISIBLE);
                    asignar_equipo.setVisibility(View.INVISIBLE);
                    selec_unidad.setVisibility(View.INVISIBLE);
                }
                dialog_loading.dismiss();

            }
        }
    }


    public class SendPostRequest_apagar extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
            }};

// Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();
            Request request=null;
            String responseBody="";
            String commnd_actividad_modelo="";
            try {
                if (modelo_gps_tranfer_equipos.equals("GV300W")) {
                    commnd_actividad_modelo="apagar";
                    request = new Request.Builder()
                            .url("https://tracktechgt.com//command.php?imei="+imei_obtenido_equipos+"&cmd="+commnd_actividad_modelo+"")
                            .build();
                    client.newCall(request).execute();
                    Response response = client.newCall(request).execute();
                    responseBody = response.body().string();
                }
                if (modelo_gps_tranfer_equipos.equals("GV75W")) {
                    commnd_actividad_modelo="apagar75";
                    request = new Request.Builder()
                            .url("https://tracktechgt.com//command.php?imei="+imei_obtenido_equipos+"&cmd="+commnd_actividad_modelo+"")
                            .build();
                    client.newCall(request).execute();
                    Response response = client.newCall(request).execute();
                    responseBody = response.body().string();
                }
                if (modelo_gps_tranfer_equipos.equals("SP1824")) {
                    commnd_actividad_modelo="bloquear";
                    request = new Request.Builder()
                            .url("https://tracktechgt.com//command.php?imei="+imei_obtenido_equipos+"&cmd="+commnd_actividad_modelo+"")
                            .build();
                    client.newCall(request).execute();
                    Response response = client.newCall(request).execute();
                    responseBody = response.body().string();
                }
            } catch (IOException e) {
                Log.e("- - - - -  ERROR1", "Ephp2:  - - - - -- - - -" + e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Fecha_Actual= sdf.format(date);

            obtener_apagados_enviados get_data = new obtener_apagados_enviados();
            get_data.execute();
        }
    }


    public class SendPostRequest_encender extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            Request request=null;
            String responseBody="";
            String commnd_actividad_modelo="";
            try {
                if (modelo_gps_tranfer_equipos.equals("GV300W")) {
                    commnd_actividad_modelo="encender";
                    request = new Request.Builder()
                            .url("https://tracktechgt.com//command.php?imei="+imei_obtenido_equipos+"&cmd="+commnd_actividad_modelo+"")
                            .build();
                    client.newCall(request).execute();
                    Response response = client.newCall(request).execute();
                    responseBody = response.body().string();
                }
                if (modelo_gps_tranfer_equipos.equals("GV75W")) {
                    commnd_actividad_modelo="encender75";
                    request = new Request.Builder()
                            .url("https://tracktechgt.com//command.php?imei="+imei_obtenido_equipos+"&cmd="+commnd_actividad_modelo+"")
                            .build();
                    client.newCall(request).execute();
                    Response response = client.newCall(request).execute();
                    responseBody = response.body().string();
                }
                if (modelo_gps_tranfer_equipos.equals("SP1824")) {
                    commnd_actividad_modelo="desbloquear";
                    request = new Request.Builder()
                            .url("https://tracktechgt.com//command.php?imei="+imei_obtenido_equipos+"&cmd="+commnd_actividad_modelo+"")
                            .build();
                    client.newCall(request).execute();
                    Response response = client.newCall(request).execute();
                    responseBody = response.body().string();
                }
            } catch (IOException e) {
                Log.e("- - - - -  ERROR1", "Ephp2:  - - - - -- - - -" + e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Manejar la respuesta aquí
                Log.d("Response", result);
            } else {
                // Manejar error
            }
        }
    }





}