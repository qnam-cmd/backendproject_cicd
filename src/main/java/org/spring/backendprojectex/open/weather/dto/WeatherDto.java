package org.spring.backendprojectex.open.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {
    private Long id;
    private String name;
    private String lat;
    private String lon;
    private String country;
    private String temp_max;
    private String temp_min;
    private String icon;
    private String sunset;      // 해 지는시간
    private String sunrise;     // 해 뜨는시간
    private String description;
}
