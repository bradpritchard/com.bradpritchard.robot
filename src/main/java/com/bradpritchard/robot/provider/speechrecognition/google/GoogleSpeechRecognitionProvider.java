package com.bradpritchard.robot.provider.speechrecognition.google;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bradpritchard.robot.provider.speechrecognition.SpeechRecognitionProvider;
import com.google.cloud.speech.spi.v1beta1.SpeechClient;
import com.google.cloud.speech.v1beta1.RecognitionAudio;
import com.google.cloud.speech.v1beta1.RecognitionConfig;
import com.google.cloud.speech.v1beta1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1beta1.SyncRecognizeResponse;
import com.google.protobuf.ByteString;

@Service
public class GoogleSpeechRecognitionProvider implements SpeechRecognitionProvider {

	@Override
	public String convertSpeechToText(byte[] speech) {

		String translation = null;

		try {
			SpeechClient speechClient = SpeechClient.create();
			// Builds the sync recognize request
			RecognitionConfig config = RecognitionConfig.newBuilder().setEncoding(AudioEncoding.LINEAR16)
					.setSampleRate(SpeechRecognitionProvider.SAMPLE_RATE).build();
			RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(speech)).build();

			// Performs speech recognition on the audio file
			SyncRecognizeResponse response = speechClient.syncRecognize(config, audio);
			List<SpeechRecognitionResult> results = response.getResultsList();

			for (SpeechRecognitionResult result : results) {
				List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
				for (SpeechRecognitionAlternative alternative : alternatives) {
					translation = alternative.getTranscript();
				}
			}
			speechClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Translated text: " + translation);

		return translation;
	}
}
