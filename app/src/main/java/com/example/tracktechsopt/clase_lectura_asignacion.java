package com.example.tracktechsopt;


public class clase_lectura_asignacion {
    private String idasignacion="", clienteasignacion="", lugarasignacion="", fechaasignacion="", obserasignacion="", ubicaasignacion="", unidadesasignacion="", telefasignacion="", dpiasignacion;



    public clase_lectura_asignacion(String id_asig, String clientasig, String lugasig, String fechasig, String obsasig, String ubicasig, String uniasig, String telasig, String dpi) {
        this.idasignacion=id_asig;
        this.clienteasignacion=clientasig;
        this.lugarasignacion=lugasig;
        this.fechaasignacion=fechasig;
        this.obserasignacion=obsasig;
        this.ubicaasignacion=ubicasig;
        this.unidadesasignacion=uniasig;
        this.telefasignacion=telasig;
        this.dpiasignacion=dpi;
    }

    public String getIdasignacion() {
        return idasignacion;
    }

    public void setIdasignacion(String idasignacion) {
        this.idasignacion = idasignacion;
    }

    public String getClienteasignacion() {
        return clienteasignacion;
    }

    public void setClienteasignacion(String clienteasignacion) {
        this.clienteasignacion = clienteasignacion;
    }

    public String getLugarasignacion() {
        return lugarasignacion;
    }

    public void setLugarasignacion(String lugarasignacion) {
        this.lugarasignacion = lugarasignacion;
    }

    public String getFechaasignacion() {
        return fechaasignacion;
    }

    public void setFechaasignacion(String fechaasignacion) {
        this.fechaasignacion = fechaasignacion;
    }

    public String getObserasignacion() {
        return obserasignacion;
    }

    public void setObserasignacion(String obserasignacion) {
        this.obserasignacion = obserasignacion;
    }

    public String getUbicaasignacion() {
        return ubicaasignacion;
    }

    public void setUbicaasignacion(String ubicaasignacion) {
        this.ubicaasignacion = ubicaasignacion;
    }

    public String getUnidadesasignacion() {
        return unidadesasignacion;
    }

    public void setUnidadesasignacion(String unidadesasignacion) {
        this.unidadesasignacion = unidadesasignacion;
    }

    public String getTelefasignacion() {
        return telefasignacion;
    }

    public void setTelefasignacion(String telefasignacion) {
        this.telefasignacion = telefasignacion;
    }

    public String getDpiasignacion() {
        return dpiasignacion;
    }

    public void setDpiasignacion(String dpiasignacion) {
        this.dpiasignacion = dpiasignacion;
    }
}

