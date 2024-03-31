package com.example.tracktechsopt;

import static com.example.tracktechsopt.MainActivity_lecturas_equipos.fecha_get_equipo;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.imei_obtenido_equipos;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.movim_transfer_equipo;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.originalLat_asig;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.originalLng_asig;
import static com.example.tracktechsopt.MainActivity_lecturas_equipos.veloc_transfer_equipo;
import static com.example.tracktechsopt.unidadActivity.Adress1;
import static com.example.tracktechsopt.unidadActivity.Adress2;
import static com.example.tracktechsopt.unidadActivity.Adress3;
import static com.example.tracktechsopt.unidadActivity.date_ACtl;
import static com.example.tracktechsopt.unidadActivity.map;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.tracktech.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class custom_detalle_ubicacion extends InfoWindow {
    boolean isViewHidden = false;
    Marker markert;
    boolean isAnimatorListenerActive = false;
    ArrayList<Double> lista_latitud = new ArrayList<>();
    ArrayList<Double> lista_longitud = new ArrayList<>();
    Drawable drawable_final;
    Drawable drawable_car;
    Drawable drawable_inicio;
    View mView_dialog;
    View dialog_ruta;
    View dialog_opciones_Carro;
     AlertDialog dialog_apagado;
    AlertDialog dialog_apagado_negado;
    AlertDialog dialog_encendido;
    AlertDialog dialog_refresh_ruta;

    TextView titleTextView3;
    Marker marker;
    Context context;
    Polygon plygones;

    String result="";
    private static int ANIMATION_DURATION = 1100; // Duración de la animación en milisegundos
    public static double CIRCLE_RADIUS = 0.0005;
    private MapController myMapController;
    public  static Polygon polygon;
    AlertDialog.Builder mBuilder2;

    ArrayList<String> listadata_direcion1 = new ArrayList<>();
    ArrayList<String> listadata_direcion2 = new ArrayList<>();

    public custom_detalle_ubicacion(MapView mapView) {
        super(R.layout.detalle_vehiculo_selected, mapView);
    }

    @Override
    public void onOpen(Object item) {
        // Obtener la vista del InfoWindow
        View view = getView();
         context = getView().getContext(); // Obtiene el contexto de la aplicación
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBuilder2 = new AlertDialog.Builder(getView().getContext());
        dialog_ruta = inflater.inflate(R.layout.dialog_recargando_ruta, null);


        drawable_car = ContextCompat.getDrawable(context, R.drawable.ic_car);
        drawable_final = ContextCompat.getDrawable(context, R.drawable.destination_svgrepo_com);
        drawable_inicio = ContextCompat.getDrawable(context, R.drawable.cube_svgrepo_com);

        // Obtener referencias a los elementos de interfaz de usuario en el layout personalizado
        TextView movimient_vehic = view.findViewById(R.id.dato_vehic1);

        titleTextView3 = view.findViewById(R.id.valor_vehic3);

        TextView ignicion_vehic = view.findViewById(R.id.valor_vehic6);
        ImageView close = view.findViewById(R.id.imageClose);
        ImageView refrescar_ruta = view.findViewById(R.id.refresh_ruta);

        lista_latitud = new ArrayList<>();
        lista_longitud = new ArrayList<>();
        marker = (Marker) item;
        String placa_num = marker.getTitle();



        if (movim_transfer_equipo == false) {
            movimient_vehic.setText("DETENIDO");
            movimient_vehic.setBackgroundColor(Color.rgb(225, 46, 46));
        }
        if (movim_transfer_equipo == true) {
            movimient_vehic.setText("MOVIÉNDOSE");
            movimient_vehic.setBackgroundColor(Color.rgb(128, 228, 58));
        }
        if (movim_transfer_equipo == false) {
            ignicion_vehic.setText("APAGADO");
            ignicion_vehic.setTextColor(Color.rgb(225, 8, 8));
        }
        if (movim_transfer_equipo == true) {
            ignicion_vehic.setText("ENCENDIDO");
            ignicion_vehic.setTextColor(Color.rgb(76, 244, 97 ));
        }

        BigDecimal numeroBD = null;
           try {
                numeroBD = new BigDecimal(veloc_transfer_equipo);
                numeroBD = numeroBD.setScale(2, RoundingMode.HALF_EVEN);

                } catch (NumberFormatException e) {
                e.printStackTrace();
                }
                titleTextView3.setText(String.valueOf(numeroBD));







        //boton para recargar ruta
        refrescar_ruta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                get_informacion_unidad get_unidad = new get_informacion_unidad();
                get_unidad.execute();
                get_address_ruta get_new_rut = new get_address_ruta();
                get_new_rut.execute();


            }
        });



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.closeInfoWindow();
                isAnimatorListenerActive = false;
            }
        });

    }

    @Override
    public void onClose() {
        // Implementa este método si necesitas realizar alguna acción al cerrar el InfoWindow
    }


    public class get_informacion_unidad extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBuilder2.setView(dialog_ruta);
            dialog_refresh_ruta = mBuilder2.create();//Set Event
            dialog_refresh_ruta.show();

        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            String comando = "";
            conexion obj_DB_Connectio = new conexion();
            Connection connection = obj_DB_Connectio.get_connection();
            originalLat_asig = 0.0;
            originalLng_asig = 0.0;

            try {
                comando = "SELECT `Latitud`,`Longitud`,`Fecha_gps`,`Velocidad`, movimiento FROM `ubicaciones_gt` WHERE `IMEI`='"+imei_obtenido_equipos+"'  order by `id_ubicacion_gt` DESC LIMIT 1";
                Statement s = null;
                s = connection.prepareStatement(comando);
                ResultSet res = s.executeQuery(comando);

                while (res.next()){
                    originalLat_asig = res.getFloat(1);
                    originalLng_asig = res.getFloat(2);
                    fecha_get_equipo = res.getString(3);
                    veloc_transfer_equipo = res.getString(4);
                    movim_transfer_equipo = res.getBoolean(5);
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
            marker.closeInfoWindow();
            titleTextView3.setText(veloc_transfer_equipo);
            List<Overlay> overlays = map.getOverlays();
            for (Overlay overlay : overlays) {
                map.getOverlayManager().remove(overlay);
            }
            map.invalidate();


            GeoPoint markerPoint = new GeoPoint(originalLat_asig, originalLng_asig);
            markert = new Marker(map);
            markert.setPosition(markerPoint);
            markert.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            markert.setTitle(imei_obtenido_equipos);
            markert.setInfoWindow(new custom_detalle_ubicacion(map));
            markert.setIcon(drawable_car);
            map.getOverlays().add(markert);
            map.invalidate();

            polygon = new Polygon();
            polygon.setFillColor(Color.TRANSPARENT);
            polygon.setStrokeColor(Color.CYAN);
            polygon.setStrokeWidth(15f); // Establecer el ancho del contorno en píxeles
            map.getOverlays().add(polygon);
            getLocationMarker();
            startCircleAnimation();

            // Restaurar la ubicación original del MapView
            GeoPoint currentLocation = (GeoPoint) map.getMapCenter();
            GeoPoint originalLocation = new GeoPoint(originalLat_asig, originalLng_asig);

            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(1500); // Duración de la animación en milisegundos
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.addUpdateListener(animation -> {

                float fraction = animation.getAnimatedFraction();
                double lat = originalLocation.getLatitude() * fraction + currentLocation.getLatitude() * (1 - fraction);
                double lng = originalLocation.getLongitude() * fraction + currentLocation.getLongitude() * (1 - fraction);

                myMapController = (MapController) map.getController();
                myMapController.setCenter(markerPoint);
                myMapController.setZoom(17);
                myMapController.setCenter(new GeoPoint(lat, lng));
                myMapController.setZoom(17);
            });
            animator.start();
            dialog_refresh_ruta.hide();
        }
    }

    public void getLocationMarker() {

        map.invalidate();
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







    public class get_address_ruta extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            String API_KEY = "AIzaSyCYtMEurr3A68Cu0y7uXS66NAsfaIkVBrw";

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
            protected void onPostExecute(String resultD) {
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
                                        String longName = component.getString("long_name");
                                        listadata_direcion2.add(shortName);

                                     //         Log.e("- - - - -  ERROR1", "ERROR1 IN CODE:  - - - - -- - - -" + shortName);
                                    }
                                }
                            }
                        }
                        JSONArray resultsArrays = json.getJSONArray("results");
                        for (int i = 0; i < resultsArrays.length(); i++) {
                            JSONObject resultx = resultsArrays.getJSONObject(i);
                            if (resultx.has("formatted_address")) {
                                direccion_datos1 = resultx.getString("formatted_address");
                                System.out.println("Formatted Address: " + direccion_datos1);
                                listadata_direcion1.add(direccion_datos1);
                                    //           Log.e("- - - - -  ERROR1", "ERROR2 IN CODE:  - - - - -- - - -" + direccion_datos1);

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
        }
    }
}