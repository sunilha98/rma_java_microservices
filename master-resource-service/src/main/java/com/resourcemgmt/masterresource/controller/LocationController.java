package com.resourcemgmt.masterresource.controller;

import java.util.List;

import com.resourcemgmt.masterresource.activities.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.resourcemgmt.masterresource.activities.ActivityContextHolder;
import com.resourcemgmt.masterresource.activities.LogActivity;
import com.resourcemgmt.masterresource.entity.Location;
import com.resourcemgmt.masterresource.service.LocationService;

@RestController
@RequestMapping("/locations")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@GetMapping
	public List<Location> getAllLocations() {
		return locationService.getAllLocations();
	}

	@PostMapping
	@LogActivity(action = "Created Location", module = "Location Management")
	public Location createLocation(@RequestBody Location location, @RequestHeader("X-Bearer-Token") String token, @RequestHeader("X-Auth-Username") String username) {

		Location resLocation = locationService.createLocation(location, username);

		ActivityLogService.TOKEN = token;
		ActivityContextHolder.setDetail("Location", resLocation.getName());

		return resLocation;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
		return locationService.getLocationById(id)
				.map(location -> ResponseEntity.ok().body(location))
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	@LogActivity(action = "Updated Location", module = "Location Management")
	public Location updateLocation(@PathVariable Long id, @RequestBody Location location, @RequestHeader("X-Bearer-Token") String token, @RequestHeader("X-Auth-Username") String username) {

		Location resLocation = locationService.updateLocation(id, location, username);

		ActivityLogService.TOKEN = token;
		ActivityContextHolder.setDetail("Location", resLocation.getName());
		return resLocation;
	}

	@DeleteMapping("/{id}")
	@LogActivity(action = "Deleted Location", module = "Location Management")
	public ResponseEntity<?> deleteLocation(@PathVariable Long id, @RequestHeader("X-Bearer-Token") String token) {
		locationService.deleteLocation(id);
		ActivityLogService.TOKEN = token;
		ActivityContextHolder.setDetail("Location Id", id.toString());
		return ResponseEntity.ok().build();
	}
}
