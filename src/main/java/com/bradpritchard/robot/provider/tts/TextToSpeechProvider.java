package com.bradpritchard.robot.provider.tts;

import javax.sound.sampled.LineUnavailableException;

public interface TextToSpeechProvider {

	byte[] convertTextToSpeech(String text);

	void sayText(String text) throws LineUnavailableException;
}
