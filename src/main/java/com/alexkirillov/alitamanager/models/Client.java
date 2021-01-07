package com.alexkirillov.alitamanager.models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document(collection = "clients")
public class  Client {
    @Id
    private String id;
    @NotBlank @Indexed(direction = IndexDirection.ASCENDING)
    private String first_name;
    @NotBlank
    private String last_name;
    private String telegram_name;
    @NotBlank
    private String telephone_number;

    public Client(@NotBlank String first_name, @NotBlank String last_name
            , @NotBlank String telephone_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.telegram_name = "-";
        this.telephone_number = telephone_number;
    }
    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setTelegramName(String telegram_name) {
        this.telegram_name = telegram_name;
    }

    public void setTelephoneNumber(String telephone_number) {
        this.telephone_number = telephone_number;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getTelegramName() {
        return telegram_name;
    }

    public String getTelephoneNumber() {
        return telephone_number;
    }

    public String getId(){
        return id;
    };

    public DBObject toDBObject(){
        return new BasicDBObject("userName", telegram_name).append("firstName", first_name)
                .append("lastName", last_name).append("telephone", telephone_number);
    }
}
