package com.example.tracktechsopt;


public class clase_lectura_sim {
    private String id_sim="", telefono_sim="", icc_sim="", ubicaion_sim="", marca_sim="", placa_sim="", imei_sim="", estado_sim="", observac_sim="";



    public clase_lectura_sim(String id, String dato1, String dato2, String dato3, String dato4, String dato5, String dato6, String dato7, String dato8) {
   this.id_sim=id;
   this.telefono_sim=dato1;
   this.icc_sim=dato2;
   this.ubicaion_sim=dato3;
   this.marca_sim=dato4;
   this.placa_sim=dato5;
   this.imei_sim=dato6;
   this.estado_sim=dato7;
   this.observac_sim=dato8;

    }

    public String getId_sim() {
        return id_sim;
    }

    public void setId_sim(String id_sim) {
        this.id_sim = id_sim;
    }

    public String getTelefono_sim() {
        return telefono_sim;
    }

    public void setTelefono_sim(String telefono_sim) {
        this.telefono_sim = telefono_sim;
    }

    public String getIcc_sim() {
        return icc_sim;
    }

    public void setIcc_sim(String icc_sim) {
        this.icc_sim = icc_sim;
    }

    public String getUbicaion_sim() {
        return ubicaion_sim;
    }

    public void setUbicaion_sim(String ubicaion_sim) {
        this.ubicaion_sim = ubicaion_sim;
    }

    public String getMarca_sim() {
        return marca_sim;
    }

    public void setMarca_sim(String marca_sim) {
        this.marca_sim = marca_sim;
    }

    public String getPlaca_sim() {
        return placa_sim;
    }

    public void setPlaca_sim(String placa_sim) {
        this.placa_sim = placa_sim;
    }

    public String getImei_sim() {
        return imei_sim;
    }

    public void setImei_sim(String imei_sim) {
        this.imei_sim = imei_sim;
    }

    public String getEstado_sim() {
        return estado_sim;
    }

    public void setEstado_sim(String estado_sim) {
        this.estado_sim = estado_sim;
    }

    public String getObservac_sim() {
        return observac_sim;
    }

    public void setObservac_sim(String observac_sim) {
        this.observac_sim = observac_sim;
    }
}

