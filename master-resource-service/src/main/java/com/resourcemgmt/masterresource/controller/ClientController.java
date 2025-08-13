package com.resourcemgmt.masterresource.controller;

import com.resourcemgmt.masterresource.dto.ClientDTO;
import com.resourcemgmt.masterresource.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resourcemgmt.masterresource.activities.ActivityContextHolder;
import com.resourcemgmt.masterresource.activities.LogActivity;
import com.resourcemgmt.masterresource.dto.ClientRequest;
import com.resourcemgmt.masterresource.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@LogActivity(action = "Created Client", module = "Client Management")
	@PostMapping
	public ResponseEntity<String> createClient(@RequestBody ClientRequest request) {
		clientService.createClient(request);

		ActivityContextHolder.setDetail("Client", request.getName());

		return ResponseEntity.ok("Client created successfully");
	}

	@GetMapping("/{clientId}")
	public ResponseEntity<ClientDTO> getClientById(@PathVariable Long clientId) {
		return ResponseEntity.ok(clientService.getClientById(clientId));
	}

	@GetMapping("/getByName/{name}")
	public ResponseEntity<Client> getClientByName(@PathVariable String name) {
		return ResponseEntity.ok(clientService.getClientByName(name));
	}

	@GetMapping
	public ResponseEntity<List<Client>> getAllClients() {
		return ResponseEntity.ok(clientService.getAllClients());
	}

}
