package com.example.tracktechsopt;


import static com.example.tracktechsopt.LoginActivity3.cheeck_user;
import static com.example.tracktechsopt.LoginActivity3.name_user;
import static com.example.tracktechsopt.menu_opciones.consulta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tracktech.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity_lecturas_asignaciones extends AppCompatActivity {
    private static ListView lista;

    public static double originalLat =0;
    public static double originalLng=-0;
    TextView tittulo;
    ArrayList<Bitmap> logos_equipos;
    public static ArrayList<String> old_lects = new ArrayList<String>();
    public static ArrayList<clase_lectura_asignacion> Lista_asgnaciones;
    public static Adapter_Lectura_asignacion adapter_lectura_asignacion;
     String coordenada_cliente="", fecha_actual="",  clienteasignacion="", lugarasignacion="", fechaasignacion="", obserasignacion="", ubicaasignacion="", unidadesasignacion="", telefasignacion="", dpiasigna="";
public  static String asign_transfer="", dpi_transfer_asignacion="", idasignacion="";

    View mView_dialog1;

    public static int flag_panico=0;
    public static AlertDialog dialog_loading;
    private static final long TIEMPO_INACTIVIDAD = 10 * 60 * 1000; // 10 minutos en milisegundos
    private Handler inactividadHandler = new Handler(Looper.getMainLooper());
    private Runnable inactividadRunnable = () -> finish();
    private static final int INTERVALO_TIEMPO = 2000; // Intervalo de tiempo en milisegundos para doble clic
    private boolean botonAtrasPresionado = false;
    private Handler mHandler = new Handler();


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_asignaciones);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        Lista_asgnaciones = new ArrayList<>();
        logos_equipos = new ArrayList<>();
        old_lects = new ArrayList<>();
        lista = (ListView) findViewById(R.id.listView_vehiculos);
        tittulo = (TextView) findViewById(R.id.titulo_u_cred_comp);
        SearchView searchView = (SearchView) findViewById(R.id.buscar_cred_comp);

        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(MainActivity_lecturas_asignaciones.this);
        mView_dialog1 = getLayoutInflater().inflate(R.layout.dialog_cargando, null);
        mBuilder2.setView(mView_dialog1);

        LottieAnimationView lottieAnimationView = mView_dialog1.findViewById(R.id.lottieAnimationView);
        lottieAnimationView.setSpeed(1.6f); // Aquí puedes ajustar el valor según lo necesites (1.0f es la velocidad normal)
        lottieAnimationView.playAnimation();
        dialog_loading = mBuilder2.create();//Set Event

        if(consulta.equals("Usado")){
        }
        if(consulta.equals("Nuevo")){

        }

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
        if (cheeck_user == 3) {
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
                ImageView bt_equipos_img= (ImageView)view.findViewById(R.id.image_equipos_asignacion);
                ImageView bt_unidades_img= (ImageView)view.findViewById(R.id.image_unidades_Asignacion);
                ImageView bt_sims_img= (ImageView)view.findViewById(R.id.image_sim_asignacion);
                ImageView bt_llamar= (ImageView)view.findViewById(R.id.image_llamar_asignacion);
                ImageView bt_ir_a_punto= (ImageView)view.findViewById(R.id.image_mapa_asignacion);
                ImageView bt_fotos= (ImageView)view.findViewById(R.id.image_cargar_fotos);
                TextView id_asign_transfer= (TextView)view.findViewById(R.id.pr_id_asignacion);
                TextView dpi_asign_transfer= (TextView)view.findViewById(R.id.dpi_asignacion);
                TextView coords= (TextView)view.findViewById(R.id.coordenadas_asignacion);

                    bt_equipos_img.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dpi_transfer_asignacion=dpi_asign_transfer.getText().toString();
                        asign_transfer=id_asign_transfer.getText().toString();
                    startActivity(new Intent(MainActivity_lecturas_asignaciones.this, MainActivity_lecturas_equipos.class));
                    }
                    });

                bt_unidades_img.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                    dpi_transfer_asignacion=dpi_asign_transfer.getText().toString();
                    startActivity(new Intent(MainActivity_lecturas_asignaciones.this, MainActivity_lecturas_vehiculos.class));
                    }
                });

                bt_sims_img.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dpi_transfer_asignacion=dpi_asign_transfer.getText().toString();
                        asign_transfer=id_asign_transfer.getText().toString();
                        startActivity(new Intent(MainActivity_lecturas_asignaciones.this, MainActivity_lecturas_sims.class));
                    }
                });

                bt_fotos.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dpi_transfer_asignacion=dpi_asign_transfer.getText().toString();
                        asign_transfer=id_asign_transfer.getText().toString();
                        startActivity(new Intent(MainActivity_lecturas_asignaciones.this, MAinActivity_carga_fotos.class));
                    }
                });


                bt_llamar.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String numeroTelefono = "tel:" + telefasignacion;
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(numeroTelefono));
                        startActivity(intent);
                    }
                });


                bt_ir_a_punto.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        coordenada_cliente=coords.getText().toString();
                        String uri = "http://maps.google.com/maps?q="  + coordenada_cliente;

                        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?q="  + coordenada_cliente );

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                        intent.setPackage("com.google.android.apps.maps");

                            startActivity(intent);

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
                if (MainActivity_lecturas_asignaciones.this != null) {
                    adapter_lectura_asignacion.filter(newText);
                }
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (botonAtrasPresionado) {
            // Realizar acciones para cerrar la aplicación, por ejemplo, llamar a finish()
            finish();
        } else {
            botonAtrasPresionado = true;
            Toast.makeText(this, "Presiona de nuevo para salir", Toast.LENGTH_SHORT).show();

            // Restablecer el indicador después del intervalo de tiempo especificado
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    botonAtrasPresionado = false;
                }
            }, INTERVALO_TIEMPO);
        }
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        reiniciarContadorInactividad();
    }

    private void reiniciarContadorInactividad() {
        inactividadHandler.removeCallbacks(inactividadRunnable);
        inactividadHandler.postDelayed(inactividadRunnable, TIEMPO_INACTIVIDAD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reiniciarContadorInactividad();
    }

    @Override
    protected void onStop() {
        super.onStop();
        inactividadHandler.removeCallbacks(inactividadRunnable);
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
idasignacion="";
clienteasignacion="";
lugarasignacion="";
fechaasignacion="";
obserasignacion="";
ubicaasignacion="";
unidadesasignacion="";
telefasignacion="";

            Lista_asgnaciones.clear();
           try {
                comando = "SELECT  n_asignacion, `Cliente`, `Lugar`, `Fecha_hora`,  `Observaciones`, `ubicacion`, `unidades`,  `telefono`, dpi FROM `asignacion_trabajos` WHERE tecnico='"+name_user+"'";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    idasignacion = res.getString(1);
                    clienteasignacion = res.getString(2);
                    lugarasignacion = res.getString(3);
                    fechaasignacion = res.getString(4);
                    obserasignacion = res.getString(5);
                    ubicaasignacion = res.getString(6);
                    unidadesasignacion = res.getString(7);
                    telefasignacion = res.getString(8);
                    dpiasigna = res.getString(9);

                    Lista_asgnaciones.add(new clase_lectura_asignacion(idasignacion,clienteasignacion,lugarasignacion,fechaasignacion,obserasignacion,ubicaasignacion,unidadesasignacion,telefasignacion,dpiasigna));

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
            if (MainActivity_lecturas_asignaciones.this != null) {
                adapter_lectura_asignacion = new Adapter_Lectura_asignacion(Lista_asgnaciones, getApplication());
                lista.setAdapter(adapter_lectura_asignacion);
                lista.requestLayout();
                dialog_loading.dismiss();
            }
        }
    }



}
