package org.spring.backendprojectex.open.weather.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.open.weather.dto.WeatherApiDto;
import org.spring.backendprojectex.open.weather.dto.WeatherDto;
import org.spring.backendprojectex.open.weather.entity.WeatherEntity;
import org.spring.backendprojectex.open.weather.repository.WeatherRepository;
import org.spring.backendprojectex.open.weather.service.WeatherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    // JSON -> JAVA
    private final ObjectMapper objectMapper;

    @Override
    public void insertWeather(String responseBody) {

        try {
            // JSON -> JAVA 객체반환
            WeatherApiDto weatherApiDto = objectMapper.readValue(responseBody, WeatherApiDto.class);
            log.info("API 응답 완료:{}", weatherApiDto);
            Optional<WeatherEntity> weatherEntity = weatherRepository.findByName(weatherApiDto.getName());
            // 2. 중복 체크: lat, lon, country, name 기준으로 중복체크
            //
            if(weatherEntity.isEmpty()) {
                WeatherEntity newEntity = WeatherEntity.builder()
                        .name(weatherApiDto.getName())
                        .lat(weatherApiDto.getCoord().getLat())
                        .lon(weatherApiDto.getCoord().getLon())
                        .icon(weatherApiDto.getWeather().get(0).getIcon())
                        .temp_max(weatherApiDto.getMain().getTemp_max())
                        .temp_min(weatherApiDto.getMain().getTemp_min())
                        .country(weatherApiDto.getSys().getCountry())
                        .sunrise(weatherApiDto.getSys().getSunrise())
                        .sunset(weatherApiDto.getSys().getSunset())
                        .build();
                weatherRepository.save(newEntity);
                log.info("날씨저장 완료 {}", newEntity);
            } else{
                log.info("이미 저장됨 - 날씨 갱신");
                // 2. 이미 저장된 도시라면 최신 온도로 갱신 (Update)
                WeatherEntity existingEntity = weatherEntity.get();

                existingEntity.setLat(weatherApiDto.getCoord().getLat());
                existingEntity.setLon(weatherApiDto.getCoord().getLon());
                existingEntity.setIcon(weatherApiDto.getWeather().get(0).getIcon());
                existingEntity.setDescription(weatherApiDto.getWeather().get(0).getDescription());
                existingEntity.setTemp_max(weatherApiDto.getMain().getTemp_max());
                existingEntity.setTemp_min(weatherApiDto.getMain().getTemp_min());
                existingEntity.setCountry(weatherApiDto.getSys().getCountry());
                existingEntity.setSunrise(weatherApiDto.getSys().getSunrise());
                existingEntity.setSunset(weatherApiDto.getSys().getSunset());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("API 응답 에러");
        }
    }

    @Override
    public WeatherDto detailWeather(String q) {
        Optional<WeatherEntity> optionalWeatherEntity = weatherRepository.findByName(q);
        if(!optionalWeatherEntity.isPresent()) {
            throw new IllegalStateException("도시가 존재하지 않습니다.");
        }
        WeatherEntity weatherEntity = optionalWeatherEntity.get();

        return WeatherDto.builder()
                .id(weatherEntity.getId())
                .name(weatherEntity.getName())
                .lat(weatherEntity.getLat())
                .lon(weatherEntity.getLon())
                .temp_max(weatherEntity.getTemp_max())
                .temp_min(weatherEntity.getTemp_min())
                .country(weatherEntity.getCountry())
                .icon(weatherEntity.getIcon())
                .sunrise(weatherEntity.getSunrise())
                .sunset(weatherEntity.getSunset())
                .description(weatherEntity.getDescription())
                .build();

    }
}
