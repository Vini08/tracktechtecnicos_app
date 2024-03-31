package com.example.tracktechsopt;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tracktech.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executor;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity3 extends AppCompatActivity {


    // UI references.
    private static final String TAG = "PushNotification";
    public static String  id_user_login = "", name_user_app, pass, alias, level_in;
    TextView usuario;
    TextView contraseña;
    String deviceId="0";
    int i = 0;
    public static final int PERMISSION_BLUETOOTH = 1;
    private final Locale locale = new Locale("id", "ID");
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
    public static int cheeck_user = 5, noty_check=0;
    public static String name_user = "",deviceName="";
    public static ArrayList<claseDatos> ListaDatos;
    public static int closs = 0;
    FrameLayout lay;
    ProgressBar progressBar;
    String token="";
    public static ArrayList<String> concat_nm_id = new ArrayList<String>();
    public static ArrayList<String> name_id_cliente = new ArrayList<String>();
    public static ArrayList<String> id_todos_clientes = new ArrayList<String>();
    public static String aux_cel="", celular="",  dpi_cliente = "";
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
    final String formattedDate = df.format(c.getTime());
    Button iniciar,back_pago;
    AlertDialog dialog_alert_users;
    AlertDialog dialog_alert_huella;
    public static ImageView image_huellas;
    AlertDialog.Builder mBuilder_lock;
    View mView_dialog1;
    private static final int PICK_IMAGE_REQUEST = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);
        // Set up the login form.
        lay = (FrameLayout) findViewById(R.id.sub_login);
        usuario = (TextView) findViewById(R.id.usuar);
        contraseña = (TextView) findViewById(R.id.passwd);
        ListaDatos = new ArrayList<>();
        iniciar = (Button) findViewById(R.id.btn_inicio_session);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        concat_nm_id = new ArrayList<>();
        name_id_cliente = new ArrayList<>();
        id_todos_clientes = new ArrayList<>();
        image_huellas = findViewById(R.id.image_huella);

        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(LoginActivity3.this);
        mView_dialog1 = getLayoutInflater().inflate(R.layout.dialog_huella, null);
        mBuilder2.setView(mView_dialog1);
        Context context = null;
        dialog_alert_huella = mBuilder2.create();//Set Event

        Executor executor = ContextCompat.getMainExecutor(this);
           final androidx.biometric.BiometricPrompt biometricPrompt = new androidx.biometric.BiometricPrompt(LoginActivity3.this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                dialog_alert_huella.show();
                getToken_for_huella();
                ususrios_con_huella huell = new ususrios_con_huella();
                huell.execute();
            }

            @Override
            public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            }
            });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("TrackTech GT")
                .setDescription("Usa tu huella para continuar.")
        .setNegativeButtonText("Cancelar")
        .build();

        image_huellas.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {

        biometricPrompt.authenticate(promptInfo);
        }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



                iniciar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectingToInternet(LoginActivity3.this)) {
                    Mostrar asynk = new Mostrar();
                    asynk.execute();
                } else {
                    //Boton que sirve para reiniciar en caso que no tenga Datos para internet
                    ImageButton btns = new ImageButton(LoginActivity3.this);
                    btns.setImageResource(R.drawable.refrescar);
                    btns.setBackgroundColor(getResources().getColor(R.color.colorB));
                    btns.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    btns.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reStartMain();
                            if (isConnectingToInternet(LoginActivity3.this)) {
                                if (closs == 0) {
                                    int secondsDelayed = 1;
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            Mostrar asynk = new Mostrar();
                                            asynk.execute();
                                        }
                                    }, secondsDelayed * 300);
                                    closs = 1;
                                }
                            }
                        }
                    });
                    //muestra mensaje y boton cuando no tiene internet
                    lay.addView(btns);

                }
            }
        });


    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Bitmap resizedBitmap = resizeBitmap(originalBitmap, 300, 500); // Ajusta el tamaño deseado

                image_huellas.setImageBitmap(resizedBitmap);

                // Convierte la imagen redimensionada a una cadena Base64 para enviarla al servidor
                String imageBase64 = convertImageToBase64(resizedBitmap);
                String imageName = "P160KQV";

                // Envía la imagen al servidor

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Bitmap resizeBitmap(Bitmap originalBitmap, int newWidth, int newHeight) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, true);
    }

    private String convertImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // Ajusta la calidad según tus necesidades
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        usuario.setText("");
        contraseña.setText("");
        usuario.requestFocus();
    }

    //metodo para entrar con huella
    class ususrios_con_huella extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... args) {
            name_user_app = "";
            alias = "";
            pass = "";
            name_user = "";
            cheeck_user = 5;
            id_user_login = "";
            aux_cel="";

            String comando = "";
            conexion obj_DB_Connectio = new conexion();
            Connection connection = obj_DB_Connectio.get_connection();


            try {
                comando = "SELECT * FROM tecnicos where id_unico='"+deviceId+"'";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next())
                {
                    id_user_login = res.getString(1);
                    name_user_app = res.getString(2);
                    dpi_cliente = res.getString(3);
                    alias = res.getString(4);
                    pass = res.getString(5);
                    level_in = res.getString(6);
                    aux_cel = res.getString(10);
                    noty_check = res.getInt(11);
                    ListaDatos.add(new claseDatos(id_user_login, name_user_app, pass, alias, level_in,aux_cel,"deptoX",dpi_cliente));
                }
            } catch (SQLException e)
            {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE logg:  - - - - -- - - -" + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          if(LoginActivity3.this != null) {
              if(ListaDatos.size()!=0) {
                 image_huellas.setImageResource(R.drawable.huellacheck);
                 dialog_alert_huella.hide();

                  //operador
                  name_user = ListaDatos.get(i).getName();
                  id_user_login = ListaDatos.get(i).getId();
                  celular = ListaDatos.get(i).getCelular();
                  dpi_cliente = ListaDatos.get(i).getCliente_dpi();
                  cheeck_user = 3;
                  startActivity(new Intent(LoginActivity3.this, MainActivity_lecturas_asignaciones.class));
                  finish();
              }

              if(ListaDatos.size()==0){
                  FancyToast.makeText(getApplicationContext(),"Debe ingresar usuario y contraseña para generar nuevas credenciales.",FancyToast.LENGTH_LONG, FancyToast.CONFUSING,true).show();
                  dialog_alert_huella.hide();
              }
          }
        }
    }





    //metodo en segundo plano para descargar los datos de los usuarios
    class Mostrar extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMax(10);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... args) {
            cheeck_user = 5;
            id_user_login = "";
            name_user_app = "";
            alias = "";
            pass = "";
            name_user = "";
            cheeck_user = 5;
            id_user_login = "";
            aux_cel="";

            String comando = "";
            conexion obj_DB_Connectio = new conexion();
            Connection connection = obj_DB_Connectio.get_connection();

            try {
                comando = "SELECT * FROM `tecnicos`";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()) {
                    id_user_login = res.getString(1);
                    name_user_app = res.getString(2);
                    dpi_cliente = res.getString(3);
                    alias = res.getString(4);
                    pass = res.getString(5);
                    level_in = res.getString(6);
                    aux_cel = res.getString(10);
                    noty_check = res.getInt(11);
                    ListaDatos.add(new claseDatos(id_user_login, name_user_app, pass, alias, level_in,aux_cel,String.valueOf(noty_check),dpi_cliente));
                }
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE logg:  - - - - -- - - -" + e.toString());
            }


            for (i = 0; i < ListaDatos.size(); i++) {
                if ((ListaDatos.get(i).getAlias().equals(usuario.getText().toString()))) {
                    if (ListaDatos.get(i).getContra().equals(contraseña.getText().toString())) {

                        //cliente
                        if (ListaDatos.get(i).getLevel().equals("1")) {
                            name_user = ListaDatos.get(i).getName();
                            id_user_login = ListaDatos.get(i).getId();
                            celular= ListaDatos.get(i).getCelular();
                            dpi_cliente = ListaDatos.get(i).getCliente_dpi();
                            cheeck_user = 1;
                            noty_check = Integer.parseInt(ListaDatos.get(i).getDepar());
                        }
                        //adminsitrador
                        if (ListaDatos.get(i).getLevel().equals("3")) {
                            name_user = ListaDatos.get(i).getName();
                            id_user_login = ListaDatos.get(i).getId();
                            celular= ListaDatos.get(i).getCelular();
                            //depart_of_tec = ListaDatos.get(i).getDepar();
                            dpi_cliente = ListaDatos.get(i).getCliente_dpi();
                            cheeck_user = 3;
                            noty_check = Integer.parseInt(ListaDatos.get(i).getDepar());
                        }

                    }
                }
            }


            return null;
        }

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!name_user.equals("")) {
                getToken();
                if(cheeck_user==3){
                     startActivity(new Intent(LoginActivity3.this, MainActivity_lecturas_asignaciones.class));
                     finish();
                }
                if(cheeck_user==2){
                  finish();
                }
                finish();
            } else {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity3.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_recibir_panico, null);
                mBuilder.setView(mView);
                TextView  fecha_cuota;
                back_pago = (Button) mView.findViewById(R.id.back_session);
                back_pago.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog_alert_users.dismiss();
                    }
                });
                dialog_alert_users = mBuilder.create();
                dialog_alert_users.show();
                progressBar.setVisibility(View.INVISIBLE);
                contraseña.setText("");
                usuario.setText("");
                usuario.requestFocus();
            }

        }
    }



    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) for (int i = 0; i < info.length; i++)
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
        }
        return false;
    }

    public void reStartMain() {
        finish();
        startActivity(getIntent());
    }


    private void getToken_for_huella() {
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
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);  if (ActivityCompat.checkSelfPermission(LoginActivity3.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
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
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);  if (ActivityCompat.checkSelfPermission(LoginActivity3.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
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
                .url("https://xilic.ihj.tep.mybluehost.me/save_tracktech_soporte.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

