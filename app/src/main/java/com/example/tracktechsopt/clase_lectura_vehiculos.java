package com.example.tracktechsopt;


public class clase_lectura_vehiculos {
    private String id_vehic="", model_vehic="",marca_vehic="", placa_vehic="", tipo_vehic="", imei_vehiculo="",modedl_gps="", estado_vehicul;



    public clase_lectura_vehiculos(String id, String dato1, String dato2, String dato3, String dato4, String dato5, String dato6, String dato7) {
   this.id_vehic=id;
   this.model_vehic=dato1;
   this.marca_vehic=dato2;
   this.placa_vehic=dato3;
   this.tipo_vehic=dato4;
   this.imei_vehiculo=dato5;
   this.modedl_gps=dato6;
   this.estado_vehicul=dato7;

    }

    public String getId_vehic() {
        return id_vehic;
    }

    public void setId_vehic(String id_vehic) {
        this.id_vehic = id_vehic;
    }

    public String getModel_vehic() {
        return model_vehic;
    }

    public void setModel_vehic(String model_vehic) {
        this.model_vehic = model_vehic;
    }

    public String getMarca_vehic() {
        return marca_vehic;
    }

    public void setMarca_vehic(String marca_vehic) {
        this.marca_vehic = marca_vehic;
    }

    public String getPlaca_vehic() {
        return placa_vehic;
    }

    public void setPlaca_vehic(String placa_vehic) {
        this.placa_vehic = placa_vehic;
    }

    public String getTipo_vehic() {
        return tipo_vehic;
    }

    public void setTipo_vehic(String tipo_vehic) {
        this.tipo_vehic = tipo_vehic;
    }

    public String getImei_vehiculo() {
        return imei_vehiculo;
    }

    public void setImei_vehiculo(String imei_vehiculo) {
        this.imei_vehiculo = imei_vehiculo;
    }

    public String getModedl_gps() {
        return modedl_gps;
    }

    public void setModedl_gps(String modedl_gps) {
        this.modedl_gps = modedl_gps;
    }

    public String getEstado_vehicul() {
        return estado_vehicul;
    }

    public void setEstado_vehicul(String estado_vehicul) {
        this.estado_vehicul = estado_vehicul;
    }
}

