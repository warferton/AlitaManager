package com.alexkirillov.alitamanager.security;

public enum ApplicationUserPermission {
    WORKDAYS_READ("workdays:read"),
    WORKDAYS_WRITE("workdays:write"), //ADMIN / EMPLOYEE
    CLIENTS_READ("clients:read"), //ADMIN
    CLIENTS_WRITE("clients:write"), //ADMIN
    EMPLOYEES_READ("employees:read"), //ADMIN / EMPLOYEE
    EMPLOYEES_WRITE("employees:write"), //ADMIN
    SERVICES_READ("services:read"), //ADMIN / CLIENT
    SERVICES_WRITE("services:write"), //ADMIN
    APPOINTMENTS_READ("appointments:read"), //ADMIN / EMPLOYEE
    APPOINTMENTS_WRITE("appointments:write"); //ADMIN / CLIENT / EMPLOYEE

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }


}
