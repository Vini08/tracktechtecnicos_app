package com.example.tracktechsopt;

public class claseDatos {
    private String id,name, contra, celular, depar="";
    private String alias="",level="",cliente_dpi="";

    public claseDatos(String ids, String names, String passw, String alia, String nivel, String cel, String dep, String dpicliente){
        this.id=ids;
        this.name=names;
        this.contra=passw;
        this.alias=alia;
        this.level=nivel;
        this.celular=cel;
        this.depar=dep;
        this.cliente_dpi=dpicliente;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDepar() {
        return depar;
    }

    public void setDepar(String depar) {
        this.depar = depar;
    }

    public String getCliente_dpi() {
        return cliente_dpi;
    }

    public void setCliente_dpi(String cliente_dpi) {
        this.cliente_dpi = cliente_dpi;
    }
}
