package com.bradpritchard.robot.provider.weather.owm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.bradpritchard.robot.provider.weather.CurrentConditions;
import com.bradpritchard.robot.provider.weather.WeatherProvider;

@Service
public class OpenWeatherMapProvider implements WeatherProvider {

	public static final String US_COUNTRY = "us";
	public static final String US_UNITS = "imperial";

	private static final String OWM_BASE_URL = "http://api.openweathermap.org/data/2.5";

	@Value("${weather.owm.apiKey:4ddcf38f93c80f2ee4050962ff697cf9}")
	private String apiKey;

	@Override
	public CurrentConditions getCurrentConditions(String zipCode) {

		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("zip", String.format("%s,%s", zipCode == null ? "29715" : zipCode, US_COUNTRY));

		CurrentWeatherResponse currentWeather = new RestTemplateBuilder().build()
				.getForObject(buildUriComponents("/weather", parameters).toUriString(), CurrentWeatherResponse.class);

		CurrentConditions currentConditions = new CurrentConditions();
		currentConditions.setCity(currentWeather.getCityName());
		currentConditions.setTemperature(currentWeather.getCurrentConditions().getTemperature());
		currentConditions.setWindSpeed(currentWeather.getWindConditions().getSpeed());

		return currentConditions;
	}

	private UriComponents buildUriComponents(String path, MultiValueMap<String, String> parameters) {

		parameters.add("units", US_UNITS);
		parameters.add("APPID", apiKey);

		return UriComponentsBuilder.fromHttpUrl(String.format("%s%s", OWM_BASE_URL, path)).queryParams(parameters)
				.build();
	}
}
