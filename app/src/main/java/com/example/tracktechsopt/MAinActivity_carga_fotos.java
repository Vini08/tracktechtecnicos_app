package com.example.tracktechsopt;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.tracktechsopt.MainActivity_lecturas_asignaciones.asign_transfer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tracktech.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MAinActivity_carga_fotos extends AppCompatActivity {

    public static String placa_sel="", consulta = "", parte_car="";
    private static final int PERMISSION_REQUEST_CODE = 200;
    ImageView  image_foto1,image_foto2,image_foto3,image_foto4;
    private static final int REQUEST_PERMISSION_CODE = 123;
    String deviceId="";
    String token="";
    private static final String TAG = "PushNotification";
    private static final int PICK_IMAGE_REQUEST = 1;

    Spinner spinner_unidad;
    public static String[] placas_fotos_get;
    ImageView img_global;
    View mView_dialog1;

    public static AlertDialog dialog_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_imagenes);
        getSupportActionBar().hide();
        image_foto1 = (ImageView) findViewById(R.id.Foto1);
        image_foto2 = (ImageView) findViewById(R.id.Foto2);
        image_foto3 = (ImageView) findViewById(R.id.Foto3);
        image_foto4 = (ImageView) findViewById(R.id.Foto4);
        spinner_unidad = (Spinner) findViewById(R.id.unidades_fotos);

        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(MAinActivity_carga_fotos.this);
        mView_dialog1 = getLayoutInflater().inflate(R.layout.dialog_cargando, null);
        mBuilder2.setView(mView_dialog1);

        LottieAnimationView lottieAnimationView = mView_dialog1.findViewById(R.id.lottieAnimationView);
        lottieAnimationView.setSpeed(1.6f); // Aquí puedes ajustar el valor según lo necesites (1.0f es la velocidad normal)
        lottieAnimationView.playAnimation();
        dialog_loading = mBuilder2.create();//Set Event

        obtener_placas identis = new obtener_placas();
        identis.execute();




        image_foto1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                img_global = image_foto1;
                parte_car = "Placa";
                openGallery();
            }
        });
        image_foto2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                img_global = image_foto2;
                parte_car = "Frente";
                openGallery();
            }
        });
        image_foto3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                img_global = image_foto3;
                parte_car = "Ubicacion";
                openGallery();
            }
        });
        image_foto4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                img_global = image_foto4;
                parte_car = "Equipo";
                openGallery();
            }
        });

        spinner_unidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtiene el elemento seleccionado del Spinner
                placa_sel = (String) parentView.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Se llama cuando no se ha seleccionado ningún elemento
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

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione una foto"), PICK_IMAGE_REQUEST);
    }

    private void sendImageToServer(String imageBase64, String imageFolder, String imageName) {
        // Aquí deberías realizar una solicitud HTTP para enviar la imagen al servidor
        // Puedes usar Retrofit, Volley u otra biblioteca de tu elección

        // Ejemplo usando Retrofit:
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<String> call = apiInterface.uploadImage(imageBase64,imageFolder,imageName);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Manejar la respuesta del servidor
                    Log.e("- - - - -  Upload", " IN CODE logg:  - - - - -- - - -" );
                    Log.d("Upload", "Image uploaded successfully");
                } else {
                    // Manejar el error del servidor
                    Log.e("- - - - -  ERROR1", "ERROR IN CODE logg0:  - - - - -- - - -" + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Manejar la falla en la solicitud
                Log.e("Upload", "Failure: " + t.getMessage());
                Log.e("- - - - -  ERROR3", "ERROR IN CODE logg5:  - - - - -- - - -" + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img = image_foto3;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Bitmap resizedBitmap = resizeBitmap(originalBitmap, 500, 500); // Ajusta el tamaño deseado

                img_global.setImageBitmap(resizedBitmap);

                // Convierte la imagen redimensionada a una cadena Base64 para enviarla al servidor
                String imageBase64 = convertImageToBase64(resizedBitmap);
                String imageFolders = placa_sel;

                // Envía la imagen al servidor
                sendImageToServer(imageBase64,imageFolders,parte_car);

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







    public class obtener_placas extends AsyncTask<String,String,Bitmap> {
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
            try {
                comando = "SELECT Placa FROM `vehiculos` WHERE n_asignacion='"+asign_transfer+"'";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);
                res.last();
                int rowCount = res.getRow();
                res.beforeFirst();
                placas_fotos_get = new String[rowCount+1];
                int i = 0;
                placas_fotos_get[i] = "- - -";

                while (res.next()){
                    placas_fotos_get[i+1] = res.getString("Placa");
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
            if (MAinActivity_carga_fotos.this != null) {
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MAinActivity_carga_fotos.this, android.R.layout.simple_spinner_item, placas_fotos_get);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinner_unidad.setAdapter(adapter1);
                dialog_loading.hide();
            }
        }
    }
}
