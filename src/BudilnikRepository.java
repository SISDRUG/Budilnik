import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BudilnikRepository {
    ArrayList<Budilnik> budilniks;

    public BudilnikRepository() {
        this.budilniks = new ArrayList<Budilnik>() ;
    }

    public BudilnikRepository(ArrayList<Budilnik> budilniks) {
        this.budilniks = budilniks;
    }

    public ArrayList<Budilnik> getBudilniks() {
        return budilniks;
    }

    public void setBudilniks(ArrayList<Budilnik> budilniks) {
        this.budilniks = budilniks;
    }

    public boolean deletBudilnik (Budilnik e){
        return this.budilniks.remove(e);
    }

    public boolean addBudilnik (Budilnik e){
        return this.budilniks.add(e);
    }

    public boolean addBudilnik (List<Integer> params){
        return this.budilniks.add(new Budilnik(params.get(0),params.get(1), params.get(2) == 1));
    }

    public Budilnik find (int index){
        return this.budilniks.get(index);
    }

    public void showBudilniks (){
        if (this.budilniks.isEmpty()){
            System.out.println("Будильнов еще нет, создайте свой первый будильник черезе меню ");
        }
        else {
            int i = 1;
            for (Budilnik b : this.budilniks) {
                System.out.printf("[%d] ", i);
                b.showInfo();
                i++;
            }
        }
        System.out.println();
    }
}
