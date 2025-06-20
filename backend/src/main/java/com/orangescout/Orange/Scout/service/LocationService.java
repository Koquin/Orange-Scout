package com.orangescout.Orange.Scout.service;

import com.orangescout.Orange.Scout.dto.LocationDTO;
import com.orangescout.Orange.Scout.exception.ResourceNotFoundException;
import com.orangescout.Orange.Scout.model.Location;
import com.orangescout.Orange.Scout.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationDTO> getAllLocations() {
        logger.info("Fetching all locations.");
        return locationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LocationDTO getLocationById(Long id) {
        logger.info("Fetching location with ID: {}", id);
        return locationRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    logger.warn("Location not found with ID: {}", id);
                    return new ResourceNotFoundException("Location not found with ID: " + id);
                });
    }

    @Transactional
    public LocationDTO saveLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());
        location.setPlaceName(locationDTO.getVenueName());

        Location savedLocation = locationRepository.save(location);
        logger.info("Location saved with ID: {}", savedLocation.getId());
        return convertToDTO(savedLocation);
    }

    @Transactional
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent location with ID: {}", id);
            throw new ResourceNotFoundException("Location not found with ID: " + id);
        }
        locationRepository.deleteById(id);
        logger.info("Location with ID {} deleted successfully.", id);
    }

    private LocationDTO convertToDTO(Location location) {
        LocationDTO locationDto = new LocationDTO();
        locationDto.setId(location.getId());
        locationDto.setVenueName(location.getPlaceName());
        locationDto.setLatitude(location.getLatitude());
        locationDto.setLongitude(location.getLongitude());
        return locationDto;
    }

    private Location convertToEntity(LocationDTO locationDto) {
        Location location = new Location();
        location.setId(locationDto.getId());
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        location.setPlaceName(locationDto.getVenueName());
        return location;
    }
}