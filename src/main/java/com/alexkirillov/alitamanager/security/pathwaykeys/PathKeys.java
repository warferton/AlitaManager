package com.alexkirillov.alitamanager.security.pathwaykeys;


public enum PathKeys {

    SECRET_KEY("p24du33fj3jf3ari3fka34");

    private final String load;

    PathKeys(String load) {
        this.load = load;
    }

    public String getLoad(){
        return load;
    }

}
