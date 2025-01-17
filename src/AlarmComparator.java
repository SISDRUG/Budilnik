import java.util.Comparator;

class AlarmComparator implements Comparator<Alarm> {
    @Override
    public int compare(Alarm a1, Alarm a2) {
        int hourComparison = Integer.compare(a1.getHours(), a2.getHours());
        if (hourComparison != 0) {
            return hourComparison;
        }
        return Integer.compare(a1.getMinutes(), a2.getMinutes());
    }
}