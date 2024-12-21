import java.util.Comparator;

class BudilnikReverseComparator implements Comparator<Budilnik> {
    @Override
    public int compare(Budilnik b1, Budilnik b2) {
        int hourComparison = Integer.compare(b2.getHours(), b1.getHours()); // Обратный порядок
        if (hourComparison != 0) {
            return hourComparison;
        }
        return Integer.compare(b2.getMinutes(), b1.getMinutes()); // Обратный порядок
    }
}
