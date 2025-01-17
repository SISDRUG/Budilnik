import org.codehaus.jackson.map.ObjectMapper;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

public class TimeThread implements Runnable {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private volatile boolean paused = false;
    private static final String FILE_PATH = "alarm.json";

    public void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notify();
    }

    public void run() {
        System.out.printf("%s started... \n", Thread.currentThread().getName());
        AlarmRepository alarmRepository = new AlarmRepository();
        while (true) {
            while (!paused) {
                try {
                    try {
                        File file = new File(FILE_PATH);
                        alarmRepository = objectMapper.readValue(file, AlarmRepository.class);
                    } catch (IOException _) {

                    }
                    Thread.sleep(100);
                    if (alarmRepository.isAlarm()) {
                        System.out.printf("Alarm %d:%d \n", LocalTime.now().getHour(), LocalTime.now().getMinute());
                        System.out.println("Stop для выключения");
                        try {
                            File soundFile = new File("src/signal-elektronnogo-budilnika-33304.wav");
                            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
                            Clip clip = AudioSystem.getClip();
                            clip.open(ais);
                            clip.setFramePosition(0);
                            clip.start();
                            Thread.sleep(clip.getMicrosecondLength() / 1000);
                            clip.stop();
                            clip.close();
                        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
                            exc.printStackTrace();
                        } catch (InterruptedException _) {
                        }
                        Thread.sleep(3000);
                    }


                } catch (InterruptedException e) {
                    System.out.println("Thread has been interrupted");
                }
            }
            try {
                Thread.sleep(60_000 - LocalTime.now().getSecond() * 1000);
                resume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
