package com.alexkirillov.alitamanager.api;

import com.alexkirillov.alitamanager.dao.clientrepo.ClientRepository;
import com.alexkirillov.alitamanager.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"https://alita-manager-app.herokuapp.com",
        "http://fast-sierra-37663.herokuapp.com"})
@RequestMapping("/api/clients")
public class ClientController {
    private ClientRepository clientRepository;
    @Autowired
    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    //CRUD
    @GetMapping(value = {"","/"})
    @PreAuthorize("hasAuthority('clients:read')")
    public List<Client> getAllClients(){
        List<Client> clients = clientRepository.findAll();

        return clients;
    }

    @PostMapping(value = {"/update","/update/"})
    @PreAuthorize("hasAuthority('clients:write')")
    public void updateClients(@RequestBody Client new_client){
        this.clientRepository.save(new_client);
    }

    @PutMapping(value = {"/add/one/", "/add/one"})
    @PreAuthorize("hasAuthority('clients:write')")
    public void insertClient(@RequestBody Client new_client){
        this.clientRepository.insert(new_client);
    }

    @PutMapping(value ={"/add/many/", "/add/many"})
    @PreAuthorize("hasAuthority('clients:write')")
    public void insertClientList(@RequestBody List<Client> new_client_list){
        this.clientRepository.insert(new_client_list);
    }

    @DeleteMapping(value = {"/delete/id/{id}","/delete/id/{id}/"})
    @PreAuthorize("hasAuthority('clients:write')")
    public void deleteClient(@PathVariable("id") String id){
        this.clientRepository.deleteById(id);
    }

    //TODO Search (NOT IMPLEMENTED)

//    @GetMapping(value = {"find/id/{id}","find/id/{id}/"})
//    @PreAuthorize("hasAuthority('clients:read')")
//    public Optional<Client> getById(@PathVariable("id") String id){
//        return this.clientRepository.findById(id);
//    }
//
//    @GetMapping(value = {"find/name/{name}","find/name/{name}/"})
//    @PreAuthorize("hasAuthority('clients:read')")
//    public List<Client> getByName(@PathVariable("name") String name){
//        QClient client = new QClient("client");
//        BooleanExpression filter_first_name = client.first_name.containsIgnoreCase(name);
//        BooleanExpression filter_last_name = client.last_name.containsIgnoreCase(name);
//
//       return (List<Client>) this.clientRepository.findAll(filter_first_name.or(filter_last_name));
//    }



}
