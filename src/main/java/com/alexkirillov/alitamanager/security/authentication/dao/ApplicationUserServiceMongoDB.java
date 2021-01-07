package com.alexkirillov.alitamanager.security.authentication.dao;

import com.alexkirillov.alitamanager.security.authentication.ApplicationUser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("mongoDB")
public class ApplicationUserServiceMongoDB implements ApplicationUserDao {

    //instantiate connection and get users collection
    MongoClient client = MongoClients.create();
    MongoDatabase database = client.getDatabase("WebTest");
    MongoCollection<Document> users_collection = database.getCollection("users");

    private PasswordEncoder passwordEncoder;

    public ApplicationUserServiceMongoDB(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectUserByUserName(String username) {

        Document user = users_collection.find(new Document("username", username)).first();


        return Optional.empty() ;
    }







}
