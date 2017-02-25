package com.bradpritchard.robot.provider.weather.owm.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Coordinates {

	@JsonProperty("lat")
	private Float latitude;

	@JsonProperty("lon")
	private Float longitude;
}
