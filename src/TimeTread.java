import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class TimeTread implements Runnable{
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void run(){
        System.out.printf("%s started... \n", Thread.currentThread().getName());
        BudilnikRepository budilnikRepository = new BudilnikRepository();
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        while (true){
            try{
                try {
                    File file = new File("budilnik.json");
                    budilnikRepository = objectMapper.readValue(file, BudilnikRepository.class);
                } catch (IOException e) {
                    Thread.sleep(3000);
                }
                Thread.sleep(3000);
                if (budilnikRepository.alarm()){
                    System.out.printf("Allarm %d:%d \n",  LocalTime.now().getHour() , LocalTime.now().getMinute());
                }

            }
            catch(InterruptedException e){
                System.out.println("Thread has been interrupted");
            }
        }
    }
}
