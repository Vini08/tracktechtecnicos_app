package com.example.tracktechsopt;

import android.graphics.Bitmap;

public class modelo_date_panicos {

    private String id,lecturaH,lecturaV,consumoL,stateL,fechaL,fechaH, nombr, hora_a, hora_fin;
Bitmap img;
    public modelo_date_panicos(String ids, String idin, String iduserr, String eje, String cli, String mod, Bitmap im, String imeis, String nam, String ha, String hx) {
        this.id=ids;
        this.lecturaH=idin;
        this.lecturaV=iduserr;
        this.consumoL=eje;
        this.stateL=cli;
        this.fechaL=mod;
        this.img=im;
        this.fechaH=imeis;
        this.nombr=nam;
        this.hora_a=ha;
        this.hora_fin=hx;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }

    public String getHora_a() {
        return hora_a;
    }

    public void setHora_a(String hora_a) {
        this.hora_a = hora_a;
    }

    public String getNombr() {
        return nombr;
    }

    public void setNombr(String nombr) {
        this.nombr = nombr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLecturaH() {
        return lecturaH;
    }

    public void setLecturaH(String lecturaH) {
        this.lecturaH = lecturaH;
    }

    public String getLecturaV() {
        return lecturaV;
    }

    public void setLecturaV(String lecturaV) {
        this.lecturaV = lecturaV;
    }

    public String getConsumoL() {
        return consumoL;
    }

    public void setConsumoL(String consumoL) {
        this.consumoL = consumoL;
    }

    public String getStateL() {
        return stateL;
    }

    public void setStateL(String stateL) {
        this.stateL = stateL;
    }

    public String getFechaL() {
        return fechaL;
    }

    public void setFechaL(String fechaL) {
        this.fechaL = fechaL;
    }

    public String getFechaH() {
        return fechaH;
    }

    public void setFechaH(String fechaH) {
        this.fechaH = fechaH;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
