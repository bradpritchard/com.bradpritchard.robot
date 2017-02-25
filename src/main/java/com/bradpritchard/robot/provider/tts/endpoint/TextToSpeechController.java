package com.bradpritchard.robot.provider.tts.endpoint;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bradpritchard.robot.provider.tts.TextToSpeechProvider;
import com.bradpritchard.robot.provider.weather.CurrentConditions;
import com.bradpritchard.robot.provider.weather.WeatherProvider;

@RestController
@RequestMapping("/tts")
public class TextToSpeechController {

	@Autowired
	private TextToSpeechProvider textToSpeechProvider;

	@Autowired
	private WeatherProvider weatherProvider;

	@RequestMapping(value = "/say", method = RequestMethod.GET, produces = "audio/wav")
	public ResponseEntity<InputStreamResource> say(@RequestParam String text) throws IOException {
		byte[] audio = textToSpeechProvider.convertTextToSpeech(text);

		return ResponseEntity.ok().contentLength(audio.length).contentType(MediaType.parseMediaType("audio/wav"))
				.body(new InputStreamResource(new ByteArrayInputStream(audio)));
	}

	@RequestMapping(value = "/temperature", method = RequestMethod.GET, produces = "audio/wav")
	public ResponseEntity<InputStreamResource> temperature(@RequestParam String location) throws IOException {

		CurrentConditions currentConditions = weatherProvider.getCurrentConditions(location);

		byte[] audio = textToSpeechProvider.convertTextToSpeech(
				String.format("The current temperature is %d", currentConditions.getTemperature().intValue()));

		return ResponseEntity.ok().contentLength(audio.length).contentType(MediaType.parseMediaType("audio/wav"))
				.body(new InputStreamResource(new ByteArrayInputStream(audio)));
	}
}
