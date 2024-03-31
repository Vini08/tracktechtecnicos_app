package com.example.tracktechsopt;

import static com.example.tracktechsopt.MainActivity_lecturas_vehiculos.imei_transfer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.tracktech.R;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MapsActivity_panicopresionado extends FragmentActivity implements
        OnMapReadyCallback {

    private LocationManager locationManager;
    private GoogleMap mMap;
    private LocationRequest locationRequest;

    private static final int REQUEST_LOCATION = 2;


    String fecha_presionado="";
    String result="";

    private com.google.android.gms.maps.model.Marker currentLocationMarker;

    private LatLng originLatLng;
    //活動建立
    LatLngBounds bounds;

    Drawable drawable_car;

    Button types_m;
    ImageView cerrar_act;
    private int mapTypeIndex = 0;

    double Latitud_panico=0.0, Longitud_panico=0.0;

    public static AlertDialog dialog_search_ruta;
    View mView_dialog1;
    ArrayList<String> listadata_direcion1 = new ArrayList<>();
    ArrayList<String> listadata_direcion2 = new ArrayList<>();

    private int[] mapTypes = {GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID, GoogleMap.MAP_TYPE_TERRAIN};
    TextView Adress1, press_panic_fecha, Adress2, Adress3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_porpanico);

        drawable_car = getResources().getDrawable(R.drawable.ic_car);
        types_m = (Button) findViewById(R.id.bt_tipo);
        cerrar_act = (ImageView) findViewById(R.id.imageClose_antigua_last);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_panic);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(MapsActivity_panicopresionado.this);
        mView_dialog1 = getLayoutInflater().inflate(R.layout.dialog_cargando, null);
        mBuilder2.setView(mView_dialog1);
        dialog_search_ruta = mBuilder2.create();//Set Event

        Adress1 = (TextView) findViewById(R.id.direccion1);
        Adress2 = (TextView) findViewById(R.id.direccion2);
        Adress3 = (TextView) findViewById(R.id.direccion3);
        press_panic_fecha = (TextView) findViewById(R.id.date_press_panic);



        types_m.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mapTypeIndex++;

                // Si el contador supera el número de tipos de mapas, lo reinicia a cero
                if (mapTypeIndex >= mapTypes.length) {
                    mapTypeIndex = 0;
                }

                // Actualiza el mapa con el nuevo tipo seleccionado
                if (mMap != null) {
                    mMap.setMapType(mapTypes[mapTypeIndex]);
                }
            }
        });

        cerrar_act.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_search_ruta.hide();
                finish();

            }
        });
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest(); //新增位置請求設置
        locationRequest.setInterval(1000); //設定回報速率
        locationRequest.setFastestInterval(1000); //設定最快回報速率
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //設定精確度
    }







    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
// en esta aprte se inializaz el mapa que muestra ubicaion actual y la ruta entre dos puntos marcados ---------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //呼叫google map物件
        mMap = googleMap;

            get_coordenasdas_rutas old_rout = new get_coordenasdas_rutas();
            old_rout.execute();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        try {

            if (lastLocation != null) {

            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }




    }








    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createLocationRequest();

                } else {

                }
        }
    }



    private BitmapDescriptor getBitmapDescriptorFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            // Crea un Bitmap desde el Drawable si no es instancia de BitmapDrawable
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public class get_coordenasdas_rutas extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog_search_ruta.show();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";
            conexion obj_DB_Connectio = new conexion();
            Connection connection = obj_DB_Connectio.get_connection();
            Latitud_panico=0.0;
            Longitud_panico=0.0;

            try {
                comando = "SELECT `latitud`,`longitud`, fecha_presionado FROM `listado_panicos` WHERE `IMEI`='"+imei_transfer+"'ORDER by id_panico desc limit 1";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);
                while (res.next()) {
                    Latitud_panico = res.getDouble(1);
                    Longitud_panico = res.getDouble(2);
                    fecha_presionado = res.getString(3);
                }

                connection.close();
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + e.toString());
            }
            Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + Latitud_panico+"    "+imei_transfer);
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (MapsActivity_panicopresionado.this != null) {

                    LatLng originLatLng = new LatLng(Latitud_panico, Longitud_panico);
                    Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Latitud_panico, Longitud_panico))
                        .title("Ubicación de SOS")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.locat)));

                    // mMap.addMarker(new MarkerOptions().position(originLatLng).title("Ubicación de Pánico").icon(BitmapDescriptorFactory.fromResource(R.drawable.locat)));
                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            marker.showInfoWindow();
                        }
                    });
                   LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(originLatLng);

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(originLatLng, 15f);
                    mMap.moveCamera(cameraUpdate);


                get_address_ruta addres = new get_address_ruta();
                addres.execute();

            }
            dialog_search_ruta.hide();
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

            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                        Latitud_panico + "," + Longitud_panico + "&key=" + API_KEY);
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
            if (MapsActivity_panicopresionado.this != null) {
                listadata_direcion2.clear();
                String direccion_datos1="";

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
                                        listadata_direcion2.add(shortName);

                                        //      Log.e("- - - - -  ERROR1", "ERROR1 IN CODE:  - - - - -- - - -" + shortName);
                                    }
                                }
                            }
                        }
                        JSONArray resultsArrays = json.getJSONArray("results");
                        for (int i = 0; i < resultsArrays.length(); i++) {
                            JSONObject result = resultsArrays.getJSONObject(i);
                            if (result.has("formatted_address")) {
                                direccion_datos1 = result.getString("formatted_address");
                                System.out.println("Formatted Address: " + direccion_datos1);
                                listadata_direcion1.add(direccion_datos1);
                                //               Log.e("- - - - -  ERROR1", "ERROR2 IN CODE:  - - - - -- - - -" + direccion_datos1);

                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Adress1.setText(listadata_direcion1.get(0));
                Adress2.setText(listadata_direcion2.get(2)+", "+listadata_direcion2.get(3));
                Adress3.setText(listadata_direcion1.get(3));
                press_panic_fecha.setText("Pánico Presionado: "+fecha_presionado);
            }

        }
    }
}
