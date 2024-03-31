package com.example.tracktechsopt;


public class clase_lectura_equipo {
    private String id_equip="", serie_vehic="",  imei_vehiculo="",modedl_gps="", telefono_equipo="", estado_gps="", placa_equipo="";



    public clase_lectura_equipo(String id, String dato1, String dato2, String dato3, String dato4, String dato5, String dato6) {
   this.id_equip=id;
   this.imei_vehiculo=dato1;
   this.serie_vehic=dato2;
   this.modedl_gps=dato3;
   this.telefono_equipo=dato4;
   this.estado_gps=dato5;
   this.placa_equipo=dato6;

    }

    public String getId_equip() {
        return id_equip;
    }

    public void setId_equip(String id_equip) {
        this.id_equip = id_equip;
    }

    public String getSerie_vehic() {
        return serie_vehic;
    }

    public void setSerie_vehic(String serie_vehic) {
        this.serie_vehic = serie_vehic;
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

    public String getTelefono_equipo() {
        return telefono_equipo;
    }

    public void setTelefono_equipo(String telefono_equipo) {
        this.telefono_equipo = telefono_equipo;
    }

    public String getEstado_gps() {
        return estado_gps;
    }

    public void setEstado_gps(String estado_gps) {
        this.estado_gps = estado_gps;
    }

    public String getPlaca_equipo() {
        return placa_equipo;
    }

    public void setPlaca_equipo(String placa_equipo) {
        this.placa_equipo = placa_equipo;
    }
}

