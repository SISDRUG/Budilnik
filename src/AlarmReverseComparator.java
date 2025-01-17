import java.util.Comparator;

class AlarmReverseComparator implements Comparator<Alarm> {
    @Override
    public int compare(Alarm a1, Alarm a2) {
        int hourComparison = Integer.compare(a2.getHours(), a1.getHours()); // Обратный порядок
        if (hourComparison != 0) {
            return hourComparison;
        }
        return Integer.compare(a2.getMinutes(), a1.getMinutes()); // Обратный порядок
    }
}
