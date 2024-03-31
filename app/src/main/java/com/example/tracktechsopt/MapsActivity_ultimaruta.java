package com.example.tracktechsopt;

import static com.example.tracktechsopt.MainActivity_lecturas_vehiculos.imei_transfer;
import static com.example.tracktechsopt.MainActivity_lecturas_vehiculos.originalLat;
import static com.example.tracktechsopt.MainActivity_lecturas_vehiculos.originalLng;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.tracktech.R;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.shashank.sony.fancytoastlib.FancyToast;

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
import java.util.List;

public class MapsActivity_ultimaruta extends FragmentActivity implements
        OnMapReadyCallback {

    private LocationManager locationManager;
    private GoogleMap mMap;
    private LocationRequest locationRequest;

    private static final int REQUEST_LOCATION = 2;

    private ConstraintLayout constraintLayout;

    private com.google.android.gms.maps.model.Marker currentLocationMarker;
    ArrayList<String> listadata_direcion1 = new ArrayList<>();
    ArrayList<String> listadata_direcion2 = new ArrayList<>();
    ArrayList<String> listadata_direcion1_dest = new ArrayList<>();
    ArrayList<String> listadata_direcion2_dest = new ArrayList<>();
    TextView Adress1, press_panic_fecha, Adress2, Adress3;
    TextView Adress_destinio, press_panic_fecha_destinio, Adress2_destinio, Adress3_destinio;

    private LatLng originLatLng;
    private LatLng destinationLatLng;
    //活動建立
    LatLngBounds bounds;

    Drawable drawable_car;

    Button types_m;
    ImageView cerrar_act;
    private int mapTypeIndex = 0;
    String result="";

    ArrayList<Double> lista_latitud = new ArrayList<>();
    ArrayList<Double> lista_longitud = new ArrayList<>();
    ArrayList<String> lista_fechas_last= new ArrayList<>();

    public static AlertDialog dialog_search_ruta;
    View mView_dialog1;
    private int[] mapTypes = {GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID, GoogleMap.MAP_TYPE_TERRAIN};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_ultimaruta);

        drawable_car = getResources().getDrawable(R.drawable.ic_car);
        types_m = (Button) findViewById(R.id.bt_tipo);
        cerrar_act = (ImageView) findViewById(R.id.imageClose_antigua);
        Adress1 = (TextView) findViewById(R.id.direccion_dest1_1);
        Adress2 = (TextView) findViewById(R.id.direccion_dest1_2);
        Adress3 = (TextView) findViewById(R.id.direccion_dest1_3);
        press_panic_fecha = (TextView) findViewById(R.id.fechas_last_origen);

        Adress_destinio = (TextView) findViewById(R.id.direccion_dest2_1);
        Adress2_destinio = (TextView) findViewById(R.id.direccion_dest2_2);
        Adress3_destinio = (TextView) findViewById(R.id.direccion_dest2_3);
        press_panic_fecha_destinio = (TextView) findViewById(R.id.fechas_last_destino);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(MapsActivity_ultimaruta.this);
        mView_dialog1 = getLayoutInflater().inflate(R.layout.dialog_ruta, null);
        mBuilder2.setView(mView_dialog1);
        dialog_search_ruta = mBuilder2.create();//Set Event

        findViewId();



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



    private void findViewId() {
        constraintLayout = findViewById(R.id.constrain);
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


    private void getRoute() {
        // Create a GeoApiContext with your Google Maps API key
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCYtMEurr3A68Cu0y7uXS66NAsfaIkVBrw")
                .build();

        // Send the directions request
        DirectionsApiRequest req = DirectionsApi.newRequest(context)
                .origin(new com.google.maps.model.LatLng(originLatLng.latitude, originLatLng.longitude))
                .destination(new com.google.maps.model.LatLng(destinationLatLng.latitude, destinationLatLng.longitude))
                .mode(com.google.maps.model.TravelMode.DRIVING); // You can change the travel mode here (e.g., DRIVING, WALKING, etc.)

        req.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(final DirectionsResult result) {
                // Process the result and draw the route
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        drawRoute(result);
                    }
                });

            }

            @Override
            public void onFailure(Throwable e) {
                // Handle the error
                Log.e("- - - - -  ERROR1", "ERROR IN CODE logg:  - - - - -- - - -" + e.toString());
            }
        });
    }

    private void drawRoute(DirectionsResult result) {
        if (result != null && result.routes != null && result.routes.length > 0) {
            DirectionsRoute route = result.routes[0];
            DirectionsLeg leg = route.legs[0];

            // Create a list of LatLng to store the points of the route
            List<LatLng> points = new ArrayList<>();
            for (DirectionsStep step : leg.steps) {
                List<LatLng> decodedPoints = PolyUtil.decode(step.polyline.getEncodedPath());
                for (LatLng point : decodedPoints) {
                    points.add(new LatLng(point.latitude, point.longitude));
                }
            }

            // Create a PolylineOptions to show the route on the map
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(points)
                    .color(Color.BLUE)
                    .width(13f)
                    .zIndex(1f);

            // Draw the Polyline on the map
            Polyline polyline = mMap.addPolyline(polylineOptions);
            // Move the camera to show the entire route
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng point : points) {
                builder.include(point);
            }
            bounds = builder.build();
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    // Espera a que se cargue completamente el mapa (layout completo) y luego ajusta la cámara
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                }
            });
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }

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
            lista_longitud.clear();
            lista_latitud.clear();
            lista_fechas_last.clear();

            try {
                comando = "SELECT `Latitud`,`Longitud`, fecha_gps\n" +
                        "FROM (\n" +
                        "  SELECT *,\n" +
                        "         (SELECT `id_ubicacion_gt`\n" +
                        "          FROM ubicaciones_completas_gt AS t2\n" +
                        "          WHERE t2.imei = '"+imei_transfer+"'\n" +
                        "          ORDER BY `id_ubicacion_gt` DESC\n" +
                        "          LIMIT 1 OFFSET 120) AS offset_id\n" +
                        "  FROM ubicaciones_completas_gt AS t1\n" +
                        "  WHERE t1.imei = '"+imei_transfer+"') AS filtered_table\n" +
                        "WHERE `id_ubicacion_gt` = (SELECT MAX(`id_ubicacion_gt`) FROM ubicaciones_completas_gt WHERE imei = '"+imei_transfer+"')\n" +
                        "   OR `id_ubicacion_gt` = offset_id";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);
                while (res.next()) {
                    lista_latitud.add(res.getDouble(1));
                    lista_longitud.add(res.getDouble(2));
                    lista_fechas_last.add(res.getString(3));
                }

                connection.close();
            } catch (SQLException e) {
                Log.e("- - - - -  ERROR1", "ERROR IN CODE:  - - - - -- - - -" + e.toString());
            }
            Log.e("- - - - -  ERROR1", "ERRALT IN CODE:  - - - - -- - - -" + lista_longitud.get(0)+"    "+imei_transfer);
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (MapsActivity_ultimaruta.this != null) {
                if (lista_latitud.size()==2) {
                    originLatLng = new LatLng(lista_latitud.get(0), lista_longitud.get(0));
                    destinationLatLng = new LatLng(lista_latitud.get(1), lista_longitud.get(1));
                    mMap.addMarker(new MarkerOptions().position(originLatLng).title("Origen").icon(BitmapDescriptorFactory.fromResource(R.drawable.apilar)));
                    mMap.addMarker(new MarkerOptions().position(destinationLatLng).title("Destino").icon(BitmapDescriptorFactory.fromResource(R.drawable.bandera)));
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(originLatLng);
                    builder.include(destinationLatLng);
                    final LatLngBounds bounds = builder.build();
                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                        }
                    });
                    getRoute();
                    //獲取定位權限
                    if (ContextCompat.checkSelfPermission(MapsActivity_ultimaruta.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MapsActivity_ultimaruta.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_LOCATION);
                    } else {
                        createLocationRequest();
                    }
                    get_address_ruta getdata = new get_address_ruta();
                    getdata.execute();
                    get_address_ruta_dest getdata_dest = new get_address_ruta_dest();
                    getdata_dest.execute();
                    dialog_search_ruta.hide();
                }else{
                    LatLng originLatLng = new LatLng(originalLat, originalLng);

                    mMap.addMarker(new MarkerOptions().position(originLatLng).title("Ubicación actual").icon(BitmapDescriptorFactory.fromResource(R.drawable.bandera)));
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(originLatLng);
                    final LatLngBounds bounds = builder.build();
                     mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));

                    FancyToast.makeText(MapsActivity_ultimaruta.this,"Vehículo aún no ha recorrido 1 hora.",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                    dialog_search_ruta.hide();
                }
            }
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
            listadata_direcion1.clear();
            listadata_direcion2.clear();
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                        lista_latitud.get(0) + "," + lista_longitud.get(0) + "&key=" + API_KEY);
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
            if (MapsActivity_ultimaruta.this != null) {
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
                press_panic_fecha.setText("Actualización: "+lista_fechas_last.get(0));
            }

        }
    }


    public class get_address_ruta_dest extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String API_KEY = "AIzaSyCYtMEurr3A68Cu0y7uXS66NAsfaIkVBrw";
            listadata_direcion2_dest.clear();
            listadata_direcion1_dest.clear();
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                        lista_latitud.get(1) + "," + lista_longitud.get(1) + "&key=" + API_KEY);
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
            if (MapsActivity_ultimaruta.this != null) {
                listadata_direcion2_dest.clear();
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
                                        listadata_direcion2_dest.add(shortName);

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
                                listadata_direcion1_dest.add(direccion_datos1);
                                //               Log.e("- - - - -  ERROR1", "ERROR2 IN CODE:  - - - - -- - - -" + direccion_datos1);

                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Adress_destinio.setText(listadata_direcion1_dest.get(0));
                Adress2_destinio.setText(listadata_direcion2_dest.get(2)+", "+listadata_direcion2_dest.get(3));
                Adress3_destinio.setText(listadata_direcion1_dest.get(3));
                press_panic_fecha_destinio.setText("Actualización: "+lista_fechas_last.get(1));
            }

        }
    }
}
