package com.orangescout.Orange.Scout.controller;

import com.orangescout.Orange.Scout.dto.LocationDTO;
import com.orangescout.Orange.Scout.exception.BadRequestException;
import com.orangescout.Orange.Scout.service.LocationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        logger.info("Request to fetch location with ID: {}", id);
        LocationDTO locationDTO = locationService.getLocationById(id);
        logger.debug("Location with ID {} found: {}", id, locationDTO.getId());
        return ResponseEntity.ok(locationDTO);
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        logger.info("Request to fetch all locations.");
        List<LocationDTO> locations = locationService.getAllLocations();
        logger.debug("Returning {} locations.", locations.size());
        return ResponseEntity.ok(locations);
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@Valid @RequestBody LocationDTO locationDTO) {
        logger.info("Request to create new location: {}", locationDTO.getVenueName());
        if (locationDTO.getId() != null) {
            logger.warn("Attempt to create location with ID already provided: {}", locationDTO.getId());
            throw new BadRequestException("ID should not be provided when creating a new location.");
        }
        LocationDTO savedLocation = locationService.saveLocation(locationDTO);
        logger.info("Location with ID {} created successfully.", savedLocation.getId());
        return new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable Long id, @Valid @RequestBody LocationDTO locationDTO) {
        logger.info("Request to update location with ID {}: {}", id, locationDTO.getVenueName());
        if (locationDTO.getId() == null || !locationDTO.getId().equals(id)) {
            logger.warn("ID inconsistency: Path ID {} vs Body ID {}", id, locationDTO.getId());
            throw new BadRequestException("The ID in the request body must match the ID in the URL.");
        }
        LocationDTO updatedLocation = locationService.saveLocation(locationDTO);
        logger.info("Location with ID {} updated successfully.", updatedLocation.getId());
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        logger.info("Request to delete location with ID: {}", id);
        locationService.deleteLocation(id);
        logger.info("Location with ID {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }
}