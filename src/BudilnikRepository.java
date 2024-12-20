import org.codehaus.jackson.annotate.JsonIgnore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        if (budilniks.stream().filter(budilnik -> budilnik.getHours() == e.getHours() && budilnik.getMinutes() == e.getMinutes()).count()<1){
            return this.budilniks.add(e);
        }
        else {
            return false;
        }
    }

    public boolean addBudilnik (List<Integer> params){
        if (budilniks.stream().filter(budilnik -> budilnik.getHours() == params.get(1) && budilnik.getMinutes() == params.getFirst()).count()<1) {
            return this.budilniks.add(new Budilnik(params.get(0), params.get(1), params.get(2) == 1));
        }else
        {
            return false;
        }
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


    @JsonIgnore
    public boolean isAlarm(){
        Stream<Budilnik> s = budilniks.stream().filter(budilnik -> budilnik.getHours() == LocalTime.now().getHour()
                                                                            && budilnik.getMinutes() == LocalTime.now().getMinute());
        return s.count()>0;
    }

    public BudilnikRepository showActive(){
        BudilnikRepository activBudilnikRepository = new BudilnikRepository();
        if (this.budilniks.isEmpty()){
            System.out.println("Будильников еще нет, создайте свой первый будильник через меню ");
        }
        else {
            int i = 1;
            for (Budilnik b : this.budilniks) {
                if (b.isStatus()){
                    System.out.printf("[%d] ", i);
                    b.showInfo();
                    activBudilnikRepository.addBudilnik(b);
                    i++;
                }
            }
        }

        System.out.println();
        return activBudilnikRepository;
    }

    public BudilnikRepository showInActive(){
        BudilnikRepository activBudilnikRepository = new BudilnikRepository();
        if (this.budilniks.isEmpty()){
            System.out.println("Будильнов еще нет, создайте свой первый будильник черезе меню ");
        }
        else {
            int i = 1;
            for (Budilnik b : this.budilniks) {
                if (!b.isStatus()){
                    System.out.printf("[%d] ", i);
                    b.showInfo();
                    activBudilnikRepository.addBudilnik(b);
                    i++;
                }
            }
        }

        System.out.println();
        return activBudilnikRepository;
    }

    public void clear(){
        this.budilniks.clear();
    }
}
