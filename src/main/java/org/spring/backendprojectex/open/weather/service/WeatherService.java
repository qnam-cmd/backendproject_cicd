package org.spring.backendprojectex.open.weather.service;

import org.spring.backendprojectex.open.weather.dto.WeatherDto;

public interface WeatherService {

    void insertWeather(String responseBody);

    WeatherDto detailWeather(String q);
}
