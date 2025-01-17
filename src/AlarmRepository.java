import org.codehaus.jackson.annotate.JsonIgnore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AlarmRepository {
    ArrayList<Alarm> alarms;

    public AlarmRepository() {
        this.alarms = new ArrayList<>() ;
    }

    public ArrayList<Alarm> getAlarms() {
        return alarms;
    }

    public void deleteAlarm(Alarm e){
        this.alarms.remove(e);
    }


    public boolean addAlarm(Alarm e){
        if (alarms.stream().noneMatch(alarm -> alarm.getHours() == e.getHours() && alarm.getMinutes() == e.getMinutes())){
            return this.alarms.add(e);
        }
        else {
            return false;
        }
    }

    public boolean addAlarm(List<Integer> params){
        if (alarms.stream().noneMatch(alarm -> alarm.getHours() == params.get(1) && alarm.getMinutes() == params.getFirst())) {
            return this.alarms.add(new Alarm(params.get(0), params.get(1), params.get(2) == 1));
        }else
        {
            return false;
        }
    }

    public Alarm find (int index){
        return this.alarms.get(index);
    }

    public void showAlarms(){
        if (this.alarms.isEmpty()){
            System.out.println("Будильников еще нет, создайте свой первый будильник через меню ");
        }
        else {
            int i = 1;
            for (Alarm a : this.alarms) {
                System.out.printf("[%d] ", i);
                a.showInfo();
                i++;
            }
        }
        System.out.println();
    }


    @JsonIgnore
    public boolean isAlarm(){
        Stream<Alarm> s = alarms.stream().filter(alarm -> alarm.getHours() == LocalTime.now().getHour()
                                                                            && alarm.getMinutes() == LocalTime.now().getMinute());
        return s.findAny().isPresent();
    }

    public AlarmRepository showActive(){
        AlarmRepository activeAlarmRepository = new AlarmRepository();
        if (this.alarms.isEmpty()){
            System.out.println("Будильников еще нет, создайте свой первый будильник через меню ");
        }
        else {
            int i = 1;
            for (Alarm b : this.alarms) {
                if (b.isStatus()){
                    System.out.printf("[%d] ", i);
                    b.showInfo();
                    activeAlarmRepository.addAlarm(b);
                    i++;
                }
            }
        }

        System.out.println();
        return activeAlarmRepository;
    }

    public AlarmRepository showInActive(){
        AlarmRepository activeAlarmRepository = new AlarmRepository();
        if (this.alarms.isEmpty()){
            System.out.println("Будильников еще нет, создайте свой первый будильник через меню ");
        }
        else {
            int i = 1;
            for (Alarm b : this.alarms) {
                if (!b.isStatus()){
                    System.out.printf("[%d] ", i);
                    b.showInfo();
                    activeAlarmRepository.addAlarm(b);
                    i++;
                }
            }
        }

        System.out.println();
        return activeAlarmRepository;
    }

    public void offAll(){
        this.alarms.forEach(alarm -> alarm.setStatus(false));
    }

    public void clear(){
        this.alarms.clear();
    }
}
