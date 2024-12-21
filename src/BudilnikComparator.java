import java.util.Comparator;

class BudilnikComparator implements Comparator<Budilnik> {
    @Override
    public int compare(Budilnik b1, Budilnik b2) {
        int hourComparison = Integer.compare(b1.getHours(), b2.getHours());
        if (hourComparison != 0) {
            return hourComparison;
        }
        return Integer.compare(b1.getMinutes(), b2.getMinutes());
    }
}