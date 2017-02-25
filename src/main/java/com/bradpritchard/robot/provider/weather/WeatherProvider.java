package com.bradpritchard.robot.provider.weather;

public interface WeatherProvider {

	CurrentConditions getCurrentConditions(String zipCode);
}
