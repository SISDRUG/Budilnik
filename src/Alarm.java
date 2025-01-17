public class Alarm {
    private int minutes;
    private int hours;
    private boolean status;

    public Alarm(){

    }

    public Alarm(int minutes, int hours, boolean status) {
        this.minutes = minutes;
        this.hours = hours;
        this.status = status;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public void showInfo(){

        System.out.printf("%02d:%02d %s \n",this.hours, this.minutes, this.status? "Включен": "Выключен" );
    }
}
