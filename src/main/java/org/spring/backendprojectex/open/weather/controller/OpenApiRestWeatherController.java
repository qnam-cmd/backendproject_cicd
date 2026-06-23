package org.spring.backendprojectex.open.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.open.util.OpenApiUtil;
import org.spring.backendprojectex.open.weather.dto.WeatherDto;
import org.spring.backendprojectex.open.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/open/api/weather")
@RequiredArgsConstructor
@Tag(name = "Weather API", description = "openWeatherMap API 연동 및 날씨 데이터 처리 API")
public class OpenApiRestWeatherController {
    private final WeatherService weatherService;

    @Value("${open.openWeatherMap.serviceKey}")
    private String key;

    @Operation(
            summary = "도시별 현재 날씨 및 저장",
            description = "도시명을 입력하면 openWeatherMap API에서 도시의 현재 날씨정보를 조회 후 DB 저장")
    @GetMapping("/search/{q}")
    public ResponseEntity<Map<String ,WeatherDto >> search(@PathVariable("q")String q) {
        // URL
        String apiURL = "https://api.openweathermap.org/data/2.5/weather?q="+q+"&appid="+key;

        // Header
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");  // JSON 데이터

        String responseBody = OpenApiUtil.get(apiURL, requestHeaders);
        System.out.println(responseBody+"<<< responseBody");

        // JSOn -> JAVA 연결 -> DTO에 추가 -> Entity -> DB저장
        weatherService.insertWeather(responseBody);

        // DB날씨정보 GET
        WeatherDto weatherDto = weatherService.detailWeather(q);
        System.out.println(weatherDto + "weatherDto");

        Map<String, WeatherDto> weather = new HashMap<>();
        weather.put("weather", weatherDto);   //"weather"이름으로


//        Map<String, String> weather = new HashMap<>();
//                weather.put("weather", responseBody);   //"weather"이름으로
        return ResponseEntity.status(HttpStatus.OK).body(weather);
    }
}
