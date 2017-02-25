package com.bradpritchard.robot.provider.weather.owm.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WeatherConditions {

	@JsonProperty("temp")
	private Float temperature;

	@JsonProperty("pressure")
	private Float barometricPressure;

	private Float humidity;
}
