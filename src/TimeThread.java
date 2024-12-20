import org.codehaus.jackson.map.ObjectMapper;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Scanner;

public class TimeThread implements Runnable{

    private volatile boolean paused = false;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notify();
    }
    public void run(){
        System.out.printf("%s started... \n", Thread.currentThread().getName());
        BudilnikRepository budilnikRepository = new BudilnikRepository();
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        while (true){
            while (!paused){
                try{
                    try {
                        File file = new File("budilnik.json");
                        budilnikRepository = objectMapper.readValue(file, BudilnikRepository.class);
                    } catch (IOException e) {

                    }
                    Thread.sleep(100);
                    if (budilnikRepository.isAlarm()){
                        System.out.printf("Allarm %d:%d \n",  LocalTime.now().getHour() , LocalTime.now().getMinute());
                        System.out.println("Stop для выключения");
                        try {
                            File soundFile = new File("src/signal-elektronnogo-budilnika-33304.wav");
                            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
                            Clip clip = AudioSystem.getClip();
                            Scanner in = new Scanner(System.in);
                            clip.open(ais);
                            clip.setFramePosition(0);
                            clip.start();
                            Thread.sleep(clip.getMicrosecondLength() / 1000);
                            clip.stop();
                            clip.close();
                        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
                            exc.printStackTrace();
                        } catch (InterruptedException exc) {}
                        Thread.sleep(3000);
                    }


                }
                catch(InterruptedException e){
                    System.out.println("Thread has been interrupted");
                }
            }
            try {
                Thread.sleep(30_000);
                resume();
                System.out.println("i alive");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
