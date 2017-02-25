package com.bradpritchard.robot.provider.weather.owm.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WindConditions {

	@JsonProperty("speed")
	private Float speed;
	
	@JsonProperty("heading")
	private Float heading;
}
