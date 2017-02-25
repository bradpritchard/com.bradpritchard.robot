package com.bradpritchard.robot.provider.weather;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CurrentConditions {

	private String city;
	private Float temperature;
	private Float windSpeed;
	private String windDirection;
}
