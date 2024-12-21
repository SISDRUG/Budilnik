public class Budilnik {
    private int minutes;
    private int hours;
    private boolean status;

    public Budilnik(){

    }

    public Budilnik(int minutes, int hours, boolean status) {
        this.minutes = minutes;
        this.hours = hours;
        this.status = status;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
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
