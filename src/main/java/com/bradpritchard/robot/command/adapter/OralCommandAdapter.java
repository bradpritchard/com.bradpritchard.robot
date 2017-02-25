package com.bradpritchard.robot.command.adapter;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bradpritchard.robot.command.Command;
import com.bradpritchard.robot.provider.speechrecognition.SpeechRecognitionProvider;
import com.bradpritchard.robot.provider.tts.TextToSpeechProvider;

@Service
public class OralCommandAdapter extends CommandAdapter<ByteArrayOutputStream> {

	@Autowired
	protected SpeechRecognitionProvider speechRecognitionProvider;

	@Autowired
	protected TextToSpeechProvider textToSpeechProvider;

	private ExecutorService inputOralCommands;
	
	private Random randomGenerator = new Random();
	private List<String> pleaseWaits;

	public OralCommandAdapter() {
		inputOralCommands = Executors.newSingleThreadExecutor();
		pleaseWaits = Arrays.asList("Just a second", "Hold on a sec", "Checking", "Let me check on that");
	}

	@Override
	public void enable() {
		inputOralCommands.submit(new Runnable() {
			@Override
			public void run() {
				
				TargetDataLine microphone;
				try {
					AudioFormat audioFormat = new AudioFormat(SpeechRecognitionProvider.SAMPLE_RATE, 8, 2, true, true);
					
					microphone = AudioSystem.getTargetDataLine(audioFormat);

					DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
					microphone = (TargetDataLine) AudioSystem.getLine(info);
					microphone.open(audioFormat);

					ByteArrayOutputStream out = new ByteArrayOutputStream();
					int numBytesRead;
					int CHUNK_SIZE = 2048;
					byte[] data = new byte[microphone.getBufferSize() / 5];
					microphone.start();

					int bytesRead = 0;
					boolean speechStarted = false;
					boolean speechEnded = false;
					boolean allSilence;
					int silenceCount = 0;
					while (bytesRead < 1000000 && !(speechStarted && speechEnded)) {
						numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
						bytesRead += numBytesRead;

						out.write(data, 0, numBytesRead);

						double rms = volumeRMS(data);
						allSilence = rms < 0.01d;
						if (allSilence) {
							silenceCount++;
							if (speechStarted && silenceCount > 20) {
								speechEnded = true;
							}
						} else {
							silenceCount = 0;
							if (!speechStarted) {
							}
							speechStarted = true;
						}
					}
					microphone.stop();
					microphone.close();

					try {
						textToSpeechProvider.sayText(pleaseWaits.get(randomGenerator.nextInt(pleaseWaits.size())));
					} catch (Exception e) {
						e.printStackTrace();
					}

					String convertedText = speechRecognitionProvider.convertSpeechToText(out.toByteArray());
					commandDispatcher.processCommand(new Command(convertedText));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private double volumeRMS(byte[] bytes) {

				// Convert byte array to double array
				double[] raw = new double[bytes.length];
				for (int i = 0; i < bytes.length; i++) {
					raw[i] = ((double) bytes[i]) / 128;
				}

				double sum = 0d;
				if (raw.length == 0) {
					return sum;
				} else {
					for (int ii = 0; ii < raw.length; ii++) {
						sum += raw[ii];
					}
				}
				double average = sum / raw.length;

				double sumMeanSquare = 0d;
				for (int ii = 0; ii < raw.length; ii++) {
					sumMeanSquare += Math.pow(raw[ii] - average, 2d);
				}
				double averageMeanSquare = sumMeanSquare / raw.length;
				double rootMeanSquare = Math.sqrt(averageMeanSquare);

				return rootMeanSquare;
			}
		});
	}

	@Override
	public void disable() {
	}
}
