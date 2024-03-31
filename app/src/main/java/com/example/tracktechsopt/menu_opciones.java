package com.example.tracktechsopt;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.tracktechsopt.LoginActivity3.cheeck_user;
import static com.example.tracktechsopt.LoginActivity3.dpi_cliente;
import static com.example.tracktechsopt.LoginActivity3.id_user_login;
import static com.example.tracktechsopt.LoginActivity3.image_huellas;
import static com.example.tracktechsopt.LoginActivity3.name_user;
import static com.example.tracktechsopt.LoginActivity3.noty_check;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tracktech.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class menu_opciones extends AppCompatActivity {

    public static String consulta = "";
    Button  bt_usado_sim, bt_nuevos_sim, unidades, usados, nuevo_reg, nuevo_sim;
    private static final int PERMISSION_REQUEST_CODE = 200;
    TextView label_dueño_equipos;
    private static final int REQUEST_PERMISSION_CODE = 123;
    CheckBox notys;
    String deviceId="";
    String token="";
    private static final String TAG = "PushNotification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        getSupportActionBar().hide();


        bt_nuevos_sim = (Button) findViewById(R.id.bt_sim_nuevos);
        bt_usado_sim = (Button) findViewById(R.id.bt_sim_usados);
        usados = (Button) findViewById(R.id.bt_usados);
        unidades = (Button) findViewById(R.id.bt_unidades);
        nuevo_reg = (Button) findViewById(R.id.bt_nuevo);
        label_dueño_equipos =(TextView)  findViewById(R.id.label_tecs);
        notys = findViewById(R.id.checkBox_noty);

        image_huellas.setImageResource(R.drawable.huella);
        label_dueño_equipos.setText(name_user);
           if(cheeck_user==2){
            nuevo_reg.setVisibility(View.INVISIBLE);
            nuevo_sim.setVisibility(View.INVISIBLE);
           }
            if (noty_check == 0) {
                notys.setChecked(false);
            }
            if (noty_check == 1) {
                notys.setChecked(true);
            }


            getToken();


        if (checkPermission()) {
            Log.e("- - - - -  ok", "permitido" );
        } else {
            requestPermission();
        }


        notys.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Verifica si el CheckBox está marcado o desmarcado
                if (isChecked) {
                    noty_check=1;
                    actualizar_notificaciones act_noty = new actualizar_notificaciones();
                    act_noty.execute();
                    // Muestra un Toast cuando el CheckBox es marcado
                } else {
                    noty_check=0;
                    actualizar_notificaciones act_noty = new actualizar_notificaciones();
                    act_noty.execute();
                    // Muestra un Toast cuando el CheckBox es desmarcado
                }
            }
        });


        nuevo_reg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(menu_opciones.this, MainActivity_lecturas_asignaciones.class));

              }
        });



        //boton para opcion 4 de creditos
        unidades.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                consulta="Nuevo";
                startActivity(new Intent(menu_opciones.this, MainActivity_lecturas_asignaciones.class));
            }
        });

        //boton para ver sims usados
        bt_usado_sim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                consulta="Usado";

            }
        });

        //boton para ver sims nuevos
        bt_nuevos_sim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                consulta="Nuevo";

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();

    }




    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }


    // Check if the app has permission to access external storage
    private boolean hasStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Request permission to access external storage
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_CODE);
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    // Handle the result of permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now access external storage
            } else {
                // Permission denied
            }
        }
    }



    class actualizar_notificaciones extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... args) {

            String comando = "";
            conexion obj_DB_Connectio = new conexion();
            Connection connection = obj_DB_Connectio.get_connection();

            try {
                comando = "UPDATE `usuarios_app` SET `notificacion`= ? WHERE  id_usuario= ?";
                PreparedStatement pstmt = connection.prepareStatement(comando);
                //pstmt.setString(6, get_server_gps);
                pstmt.setInt(1, noty_check);
                pstmt.setString(2, id_user_login);
                pstmt.execute();
                connection.close();
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "insert CODE:  - - - - -- - - -" + e.toString());
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Muestra un Toast cuando el CheckBox es marcado
            Toast.makeText(getApplicationContext(), "Notificaciones actualizadas.", Toast.LENGTH_SHORT).show();

        }
    }




    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onComplete(@NonNull Task<String> task) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //If task is failed then
                if (!task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Failed to get the Token");
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId = Settings.Secure.getString(
                            getBaseContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                } else {
                    final TelephonyManager mTelephony = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);  if (ActivityCompat.checkSelfPermission(menu_opciones.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if (mTelephony.getDeviceId() != null) {
                        deviceId = mTelephony.getDeviceId();
                    } else {
                        deviceId = Settings.Secure.getString(
                                getBaseContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                    }
                }


                String deviceName = Build.MODEL;
                String deviceManufacturer = Build.MANUFACTURER;
                //Token
                token = task.getResult();
                Log.d(TAG, "onComplete: " + token);
                registerToken(token, deviceName, deviceId);
            }
        });
    }


    private void registerToken(String token, String devname, String Manufact) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .add("deviceName", devname)
                .add("id_unico", Manufact)
                .add("dpiclient", dpi_cliente)
                .build();

        Request request = new Request.Builder()
                .url("https://xilic.ihj.tep.mybluehost.me/save_datos_tracktech.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
