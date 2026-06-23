package org.spring.backendprojectex.open.weather.dto;
import lombok.Data;

@Data
public class Wind {
    private String speed;
    private String deg;
    private String gust;
}
