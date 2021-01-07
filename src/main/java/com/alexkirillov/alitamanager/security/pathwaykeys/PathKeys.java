package com.alexkirillov.alitamanager.security.pathwaykeys;


public enum PathKeys {

    SECRET_KEY("");

    private final String load;

    PathKeys(String load) {
        this.load = load;
    }

    public String getLoad(){
        return load;
    }

}
