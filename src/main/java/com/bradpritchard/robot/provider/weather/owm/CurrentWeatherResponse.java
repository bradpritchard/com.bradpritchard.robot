package com.bradpritchard.robot.provider.weather.owm;

import com.bradpritchard.robot.provider.weather.owm.entities.Coordinates;
import com.bradpritchard.robot.provider.weather.owm.entities.WeatherConditions;
import com.bradpritchard.robot.provider.weather.owm.entities.WindConditions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeatherResponse {

	@JsonProperty("coord")
	private Coordinates coordinates;
	
	@JsonProperty("main")
	private WeatherConditions currentConditions;

	@JsonProperty("wind")
	private WindConditions windConditions;

	@JsonProperty("dt")
	private Long timestamp;
	
	@JsonProperty("name")
	private String cityName;
}
