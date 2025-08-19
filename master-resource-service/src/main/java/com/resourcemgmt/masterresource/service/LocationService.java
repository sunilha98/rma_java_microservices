package com.resourcemgmt.masterresource.service;

import com.resourcemgmt.masterresource.entity.Location;
import com.resourcemgmt.masterresource.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location createLocation(Location location, String username) {
        location.setCreatedBy(username);
        location.setCreatedAt(LocalDateTime.now());
        location.setIsActive(true);
        return locationRepository.save(location);
    }

    public Location updateLocation(Long id, Location updatedLocation, String username) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        location.setName(updatedLocation.getName());
        location.setCreatedAt(LocalDateTime.now());
        return locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }
}
