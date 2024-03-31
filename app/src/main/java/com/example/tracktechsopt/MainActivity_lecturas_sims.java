package com.example.tracktechsopt;


import static com.example.tracktechsopt.LoginActivity3.cheeck_user;
import static com.example.tracktechsopt.LoginActivity3.name_user;
import static com.example.tracktechsopt.MainActivity_lecturas_asignaciones.asign_transfer;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tracktech.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity_lecturas_sims extends AppCompatActivity {
    private static ListView lista;

    public static double originalLat_asig =0;
    public static double originalLng_asig=-0;
    TextView tittulo;
    ArrayList<Bitmap> logos_equipos;
    public static ArrayList<String> old_lects = new ArrayList<String>();
    public static ArrayList<clase_lectura_sim> Lista_sims;
    public static Adapter_Lectura_sim adapter_lectura;
    public static String  placa_en_spiner="", id_sim_get="", telefono_sim_get="",  icc_sim_get="", ubicacion_sim_get="", marca_sim_get="",  placa_sim_get="", imei_sim_get="", estado_sim="";
    public static String fecha_actual="", observ_sim="", telefono_sim_obtenido="", placa_find_vehiculos="", marca_find_vehiculos="", modelo_find_vehiculos="", imei_find_vehiculos="", modelogos_find_vehiculos="";

    public static boolean movim_transfer_equipo = false, ignicion_transfer_equipo=false;
    public static ArrayList<String> data_placas = new ArrayList<>();
    public static ArrayList<String> data_imeis = new ArrayList<>();

    public static ArrayList<String> data_modelogps = new ArrayList<>();

    View mView_dialog1;

    public static AlertDialog dialog_loading;
    private static WeakReference<MainActivity_lecturas_sims> weakReference;
    AlertDialog install_sim;

    TextView get_telef_sim;

    TextView get_icc_sim;
    TextView imei;
    Spinner unidades_cargar;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_sims);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        weakReference = new WeakReference<>(this);

        Lista_sims = new ArrayList<>();
        logos_equipos = new ArrayList<>();
        old_lects = new ArrayList<>();
        lista = (ListView) findViewById(R.id.listView_vehiculos);
        tittulo = (TextView) findViewById(R.id.titulo_u_cred_comp);
        SearchView searchView = (SearchView) findViewById(R.id.buscar_cred_comp);

        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(MainActivity_lecturas_sims.this);
        mView_dialog1 = getLayoutInflater().inflate(R.layout.dialog_cargando, null);
        mBuilder2.setView(mView_dialog1);

        AlertDialog.Builder mBuilderInsrSim = new AlertDialog.Builder(MainActivity_lecturas_sims.this);
        View mViewInsSim = getLayoutInflater().inflate(R.layout.dialog_instalar_sim, null);
        mBuilderInsrSim.setView(mViewInsSim);
        install_sim = mBuilderInsrSim.create();


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
        get_informacion_lecturas identis = new get_informacion_lecturas();
        identis.execute();


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView bt_install_img= (ImageView)view.findViewById(R.id.image_install_sim);
                 get_telef_sim= (TextView)view.findViewById(R.id.dato_telef_sim);
                 get_icc_sim= (TextView)view.findViewById(R.id.dato_icc_sim);

                bt_install_img.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Button back_install_sim;
                        Button instalar_sim;
                        TextView tel_get_sim;
                        TextView icc_get_sim;
                        tel_get_sim = (TextView) mViewInsSim.findViewById(R.id.valor_telefono_ensim);
                        icc_get_sim = (TextView) mViewInsSim.findViewById(R.id.valor_icc_ensim);
                        back_install_sim = (Button) mViewInsSim.findViewById(R.id.back_session_install_sim);
                        instalar_sim = (Button) mViewInsSim.findViewById(R.id.boton_indtsl_sim);

                        install_sim.show();
                        imei= (TextView)mViewInsSim.findViewById(R.id.imei_spiner);
                        unidades_cargar = (Spinner) mViewInsSim.findViewById(R.id.unidades_to_gps);
                        cargar_datos_de_unidades carg = new cargar_datos_de_unidades();
                        carg.execute();
                        tel_get_sim.setText("TELEFONO: "+get_telef_sim.getText());
                        icc_get_sim.setText("ICC: "+get_icc_sim.getText());

                        unidades_cargar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                // Mostrar el elemento seleccionado en el TextView
                                imei.setText(data_imeis.get(unidades_cargar.getSelectedItemPosition()));
                                placa_en_spiner=data_placas.get(unidades_cargar.getSelectedItemPosition());
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // Acciones adicionales si nada está seleccionado
                            }

                        });

                        back_install_sim.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                install_sim.dismiss();
                            }
                        });

                        instalar_sim.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                reinstalar_Sim rein = new reinstalar_Sim();
                            rein.execute();
                            }
                        });

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
                if (MainActivity_lecturas_sims.this != null) {
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
    public static class get_informacion_lecturas extends AsyncTask<String,String,Bitmap> {
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
            id_sim_get="";
            telefono_sim_get="";
            icc_sim_get="";
            ubicacion_sim_get="";
            marca_sim_get="";
            placa_sim_get="";
            imei_sim_get="";

            Lista_sims.clear();
            int count=0;
            try {
                comando = "SELECT id_sim,`telefono`, `ICC`, `Ubicacion`, marca_sim, placa, IMEI, estado, observacion FROM `sims` WHERE id_asignacion='"+asign_transfer+"'";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    id_sim_get = res.getString(1);
                    telefono_sim_get = res.getString(2);
                    icc_sim_get = res.getString(3);
                    ubicacion_sim_get = res.getString(4);
                    marca_sim_get = res.getString(5);
                    placa_sim_get = res.getString(6);
                    imei_sim_get = res.getString(7);
                    estado_sim = res.getString(8);
                    observ_sim = res.getString(9);
                    Lista_sims.add(new clase_lectura_sim(id_sim_get,telefono_sim_get,icc_sim_get,ubicacion_sim_get,marca_sim_get,placa_sim_get,imei_sim_get,estado_sim,observ_sim));
                    count++;
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
            MainActivity_lecturas_sims activityInstance = weakReference.get();
            if (activityInstance != null) {
               adapter_lectura = new Adapter_Lectura_sim(Lista_sims, activityInstance);
                lista.setAdapter(adapter_lectura);
                lista.requestLayout();
                dialog_loading.dismiss();
            }
        }
    }



    public class cargar_datos_de_unidades extends AsyncTask<String,String,Bitmap> {
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
            data_placas.clear();
            data_imeis.clear();
            data_modelogps.clear();

                try {
                comando = "SELECT placa, imei, modelo_gps FROM vehiculos WHERE id_asignacion='"+asign_transfer+"' and SIM = 'Pendiente'";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                    data_placas.add("- - -");
                    data_imeis.add("- - -");
                    data_modelogps.add("- - -");

                while (res.next()){
                data_placas.add(res.getString(1));
                data_imeis.add(res.getString(2));
                data_modelogps.add(res.getString(3));
                }
                connection.close();
                } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN pl:  - - - - -- - - -" + e.toString());
                }
            Log.e("- - - - -  ERROR1", "ERROR IN pl:  - - - - -- - - -" + data_imeis.get(0));

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            MainActivity_lecturas_sims activityInstance = weakReference.get();
            if (activityInstance != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        MainActivity_lecturas_sims.this,android.R.layout.simple_spinner_item,data_placas );
                unidades_cargar.setAdapter(adapter);
                dialog_loading.hide();
            }
        }
    }


    public class reinstalar_Sim extends AsyncTask<String,String,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog_loading.show();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";
            conexion obj_DB_Connectio=new conexion();
            Connection connection=obj_DB_Connectio.get_connection();

//almacena movimientos
            try {
                comando = "INSERT INTO `registro_movimientos` (`IMEI`,`Modelo_gps`,`Telefono`, Placa, DPI, Fecha, id_asignacion, ultima_persona, Estado) values(?,?,?,?,?,?,?,?,?)";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, imei.getText().toString());
                pstmt.setString(2, data_modelogps.get(unidades_cargar.getSelectedItemPosition()));
                pstmt.setString(3, get_telef_sim.getText().toString());
                pstmt.setString(4, data_placas.get(unidades_cargar.getSelectedItemPosition()));
                pstmt.setString(5, dpi_transfer_asignacion);
                pstmt.setString(6, fecha_actual);
                pstmt.setString(7, asign_transfer);
                pstmt.setString(8, name_user);
                pstmt.setString(9, "Reinstalar SIM");
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE vehiculos set SIM=? WHERE Placa= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                pstmt.setString(1, get_telef_sim.getText().toString());
                pstmt.setString(2, placa_en_spiner);
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE sims set `Ubicacion`=?,`Estado`=?, Placa=?, IMEI=?  WHERE telefono= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, "En_unidad");
                pstmt.setString(2, "Instalado");
                pstmt.setString(3, placa_en_spiner);
                pstmt.setString(4, imei.getText().toString());
                pstmt.setString(5, get_telef_sim.getText().toString());
                pstmt.execute();
            }catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }

            try {
                comando = "UPDATE equipos set `Telefono`=?,`ICC`=?, observacion=?  WHERE IMEI= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setString(1, get_telef_sim.getText().toString());
                pstmt.setString(2, get_icc_sim.getText().toString());
                pstmt.setString(3, "-");
                pstmt.setString(4, imei.getText().toString());
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
            if (MainActivity_lecturas_sims.this != null) {
               FancyToast.makeText(getApplicationContext(),"El SIM fue instalado con éxito.",FancyToast.LENGTH_LONG, FancyToast.INFO,true).show();
                finish();
                dialog_loading.hide();
            }
        }
    }
}
