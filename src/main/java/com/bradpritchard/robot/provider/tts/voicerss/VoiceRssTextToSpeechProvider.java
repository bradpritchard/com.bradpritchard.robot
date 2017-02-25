package com.bradpritchard.robot.provider.tts.voicerss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.bradpritchard.robot.provider.tts.TextToSpeechProvider;

@Service
public class VoiceRssTextToSpeechProvider implements TextToSpeechProvider {

	protected ExecutorService speechThreadPool;

	public VoiceRssTextToSpeechProvider() {
		speechThreadPool = Executors.newSingleThreadExecutor();
	}

	@Override
	public byte[] convertTextToSpeech(String text) {

		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("hl", "en-ca");
		parameters.add("c", "WAV");
		parameters.add("r", "1");
		parameters.add("src", text);

		try (InputStream inputStream = buildUriComponents(parameters).toUri().toURL().openStream();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
			IOUtils.copy(inputStream, outputStream);
			return outputStream.toByteArray();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

	@Override
	public void sayText(String text) {

		speechThreadPool.submit(new SpeakerCallable(text));
	}

	private UriComponents buildUriComponents(MultiValueMap<String, String> parameters) {

		parameters.add("key", "07b0673e5ff8468c8219eef87c3c1f5e");

		return UriComponentsBuilder.fromHttpUrl("http://api.voicerss.org").queryParams(parameters).build();
	}

	class SpeakerCallable implements Callable<Void>, LineListener {

		private String textToSay;
		private boolean doneSpeaking;

		public SpeakerCallable(String textToSay) {
			this.textToSay = textToSay;
		}

		@Override
		public Void call() throws Exception {
			try {
				byte[] textBytes = convertTextToSpeech(textToSay);

				AudioInputStream audioStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(textBytes));
				AudioFormat format = audioStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				Clip audioClip = (Clip) AudioSystem.getLine(info);
				audioClip.addLineListener(this);
				audioClip.open(audioStream);
				audioClip.start();
				while (!doneSpeaking) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				audioClip.close();
				audioStream.close();

				// SourceDataLine speakers;
				// AudioFormat format = new AudioFormat(8000f, 8, 1, true,
				// true);
				// DataLine.Info dataLineInfo = new
				// DataLine.Info(SourceDataLine.class, format);
				// speakers = (SourceDataLine)
				// AudioSystem.getLine(dataLineInfo);
				// speakers.open(format);
				// speakers.start();
				//
				// speakers.write(textBytes, 0, textBytes.length);
				// speakers.drain();
				// speakers.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		public void update(LineEvent event) {
			if (event.getType() == LineEvent.Type.STOP) {
				doneSpeaking = true;
			}
		}
	}

}
