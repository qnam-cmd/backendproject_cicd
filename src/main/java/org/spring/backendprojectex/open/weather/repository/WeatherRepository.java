package org.spring.backendprojectex.open.weather.repository;

import org.spring.backendprojectex.open.weather.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
    Optional<WeatherEntity> findByName(String name);
}
