package com.resourcemgmt.masterresource.service;

import java.time.LocalDateTime;
import java.util.List;

import com.resourcemgmt.masterresource.dto.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resourcemgmt.masterresource.dto.ClientRequest;
import com.resourcemgmt.masterresource.entity.Client;
import com.resourcemgmt.masterresource.repository.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	public void createClient(ClientRequest request) {
		if (clientRepository.existsByCode(request.getCode())) {
			throw new RuntimeException("Client code already exists");
		}

		Client client = new Client();
		client.setName(request.getName());
		client.setCode(request.getCode());
		client.setContactEmail(request.getContactEmail());
		client.setContactPhone(request.getContactPhone());
		client.setIsActive(true);
		client.setCreatedAt(LocalDateTime.now());

		clientRepository.save(client);
	}

	public ClientDTO getClientById(Long id) {
		Client client = clientRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Not found"));
		return new ClientDTO(
				client.getId(),
				client.getName(),
				client.getCode(),
				client.getContactEmail(),
				client.getContactPhone(),
				client.getIsActive(),
				client.getCreatedAt()
		);
	}


	public Client getClientByName(String name) {
		return clientRepository.findByName(name)
				.orElseThrow(() -> new RuntimeException("Client not found with name: " + name));
	}

	public List<Client> getAllClients() {
		return clientRepository.findAll();
	}
}
