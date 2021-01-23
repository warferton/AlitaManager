package com.alexkirillov.alitamanager.models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Objects;

@Document(collection = "services")
public class Service {
    @Id
    private String id;
    @NotBlank
    private String service_name;
    @NotNull
    private Duration estimated_time;
    @NotNull
    private double price;

    public Service(@NotBlank String service_name, @NotNull Duration estimated_time, @NotNull double price) {
        this.service_name = service_name;
        this.estimated_time = estimated_time;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getService_name() {
        return service_name;
    }

    public Duration getEstimatedTime() {
        return estimated_time;
    }

    public double getPrice() {
        return price;
    }

    public void setEstimatedTime(Duration estimated_time) {
        this.estimated_time = estimated_time;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service)) return false;
        Service service = (Service) o;
        return Double.compare(service.getPrice(), getPrice()) == 0 &&
                Objects.equals(getId(), service.getId()) &&
                Objects.equals(getService_name(), service.getService_name()) &&
                Objects.equals(getEstimatedTime(), service.getEstimatedTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getService_name(), getEstimatedTime(), getPrice());
    }

    @Override
    public String toString() {
        return "Service{" +
                "service_name='" + service_name + '\'' +
                ", estimated_time=" + estimated_time +
                ", price=" + price +
                '}';
    }

}
