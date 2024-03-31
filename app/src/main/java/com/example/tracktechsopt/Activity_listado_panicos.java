package com.example.tracktechsopt;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tracktech.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Activity_listado_panicos extends FragmentActivity implements
        OnMapReadyCallback {
    private static ListView lista;

    ArrayList<Bitmap> logos_equipos;
    public static ArrayList<String> old_lects = new ArrayList<String>();
    public static String  fecha_actual="", id_panico_get="", imei_get="", latti_get="", longii_get="", fecha_get="";
    public static String fecha_panic_transfer="", latitud_panic_transfer="", longi_panic_transfer="",cantidad_panics="";

    Button back_asignar, asignar_equip;
   private static final int PERMISSION_REQUEST_CODE = 200;

    public static ArrayList<clase_lectura_panicos> Lista_panicos;
    public static adapter_Lectura_panicos adapter_lectura_pan;
    View mView_dialog1;
    public static AlertDialog dialog_loading;
    TextView count_panicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_btnpanicos);


        Lista_panicos = new ArrayList<>();
        lista = (ListView) findViewById(R.id.listView_panicos_listado);
        count_panicos = (TextView) findViewById(R.id.conteo_panicos);


        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(Activity_listado_panicos.this);
        mView_dialog1 = getLayoutInflater().inflate(R.layout.dialog_cargando, null);
        mBuilder2.setView(mView_dialog1);
        LottieAnimationView lottieAnimationView = mView_dialog1.findViewById(R.id.lottieAnimationView);
        lottieAnimationView.setSpeed(1.6f); // Aquí puedes ajustar el valor según lo necesites (1.0f es la velocidad normal)
        lottieAnimationView.playAnimation();
        dialog_loading = mBuilder2.create();//Set Event

        DateTimeFormatter dtf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            fecha_actual= dtf.format(now);
        }

        if (checkPermission()) {
        } else {
            requestPermission();
        }


        obtener_listado_panicos identis = new obtener_listado_panicos();
        identis.execute();





//selecciona un cliente y muestra el detalle de cada credito
lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text1 = (TextView) view.findViewById(R.id.fecha_press_panico);
                TextView text2 = (TextView) view.findViewById(R.id.lat_press_panico);
                TextView text3 = (TextView) view.findViewById(R.id.long_press_panico);
                TextView text4 = (TextView) view.findViewById(R.id.producto_id);
                fecha_panic_transfer=text1.getText().toString();
                latitud_panic_transfer=text2.getText().toString();
                longi_panic_transfer=text3.getText().toString();
                ImageView ir_punto= (ImageView)view.findViewById(R.id.image_ir_punto);

                ir_punto.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog_loading.hide();
                        startActivity(new Intent(Activity_listado_panicos.this, MapsActivity_panicopresionado.class));

                    }
                });



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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    public class obtener_listado_panicos extends AsyncTask<String,String,Bitmap> {
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
            id_panico_get="";
            imei_get="";
            latti_get="";
            longii_get="";
            fecha_get="";

            Lista_panicos.clear();
            int count=0;
            try {
                comando = "Select `id_panico`, `fecha_presionado`, `latitud`, `longitud`, `IMEI`, (SELECT COUNT(`id_panico`) FROM listado_panicos WHERE `IMEI`='') AS count FROM `listado_panicos` where `IMEI`='' ORDER by `fecha_presionado` desc";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    id_panico_get = res.getString(1);
                    fecha_get = res.getString(2);
                    latti_get = res.getString(3);
                    longii_get = res.getString(4);
                    imei_get = res.getString(5);
                    cantidad_panics = res.getString(6);
                    Lista_panicos.add(new clase_lectura_panicos(id_panico_get,fecha_get,latti_get,longii_get,imei_get));
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
            if (Activity_listado_panicos.this != null) {
                adapter_lectura_pan = new adapter_Lectura_panicos(Lista_panicos, getApplication());
                lista.setAdapter(adapter_lectura_pan);
                lista.requestLayout();
                count_panicos.setText(cantidad_panics);
                dialog_loading.hide();
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with accessing the file

            }
        }
    }
    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }


}
