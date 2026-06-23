package org.spring.backendprojectex.open.weather.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Weather {
    private String id;
    private String main;
    private String description;
    private String icon;
}
