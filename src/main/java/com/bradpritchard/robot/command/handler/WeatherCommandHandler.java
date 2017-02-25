package com.bradpritchard.robot.command.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.LineUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bradpritchard.robot.command.Command;
import com.bradpritchard.robot.provider.tts.TextToSpeechProvider;
import com.bradpritchard.robot.provider.weather.CurrentConditions;
import com.bradpritchard.robot.provider.weather.WeatherProvider;

@Component
public class WeatherCommandHandler implements CommandHandler {

	@Autowired
	protected TextToSpeechProvider textToSpeechProvider;
	
	@Autowired
	protected WeatherProvider weatherProvider;
	
	@Override
	public boolean handleCommand(Command command) {
		
		if (command.getCommandText() != null) {
			Pattern p = Pattern.compile("what's the (temperature|weather)( in (.*))?");
			Matcher m = p.matcher(command.getCommandText().toLowerCase());
			while (m.find()) {
				CurrentConditions currentConditions = weatherProvider.getCurrentConditions(m.group(3));
				try {
					textToSpeechProvider.sayText(String.format("It's currently %d in %s", currentConditions.getTemperature().intValue(), currentConditions.getCity()));
					return true;
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}
}
