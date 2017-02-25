package com.bradpritchard.robot.provider.speechrecognition;

public interface SpeechRecognitionProvider {

	public static final int SAMPLE_RATE = 16000;

	String convertSpeechToText(byte[] speech);
}
