package com.example.tracktechsopt;


public class clase_lectura_panicos {
    private String idpanico="", imei_press="", fecha_press="",latt_press="", long_press="";



    public clase_lectura_panicos(String id_p, String fech, String lattP, String longP, String imeiP) {
   this.idpanico=id_p;
        this.fecha_press=fech;
        this.latt_press=lattP;
   this.long_press=longP;
   this.imei_press=imeiP;

    }

    public String getIdpanico() {
        return idpanico;
    }

    public void setIdpanico(String idpanico) {
        this.idpanico = idpanico;
    }

    public String getFecha_press() {
        return fecha_press;
    }

    public void setFecha_press(String fecha_press) {
        this.fecha_press = fecha_press;
    }

    public String getLatt_press() {
        return latt_press;
    }

    public void setLatt_press(String latt_press) {
        this.latt_press = latt_press;
    }

    public String getLong_press() {
        return long_press;
    }

    public void setLong_press(String long_press) {
        this.long_press = long_press;
    }

    public String getImei_press() {
        return imei_press;
    }

    public void setImei_press(String imei_press) {
        this.imei_press = imei_press;
    }
}

