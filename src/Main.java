/*
 ToDO
 *  1. Создать класс и обьект Будильник
 *  2. Создать репозиторий для хранения обьектов типа Будильник
 *  3. Можно создавать будильники на определенное время (мин/час)
 *  4. Реализация консольного интерфейса
 *  5. Сохранение в csv или json
*/


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String input = "init";
        BudilnikRepository budilnikRepository = new BudilnikRepository();
        while (!"0".equals(input)){

            showMainMenu();
            input = in.next();
            clearConsole();
            switch (input)
            {
                case "1" -> {
                    clearConsole();
                    if (budilnikRepository.addBudilnik(showCreateMenu())) {
                        System.out.println("Успешно создан");
                    }
                }
                case "2" -> {
                    System.out.println("Будильники:");
                    budilnikRepository.showBudilniks();

                }
                case "3" -> {
                    System.out.println("Удаление будильника:");
                    budilnikRepository.showBudilniks();
                    if (!budilnikRepository.budilniks.isEmpty()) {
                        showDeleteMenu(budilnikRepository);
                    }
                    else
                    {System.out.println("В данный момент нет будильников для удаления");}

                }
                case null, default -> System.out.printf("%s отсутствует как функция \n", input);
            }
        }
    }

    public static void showMainMenu (){
        System.out.println("1. Создать будильник");
        System.out.println("2. Просмотреть будильники");
        System.out.println("3. Удалить будильник");
    }

    public static Budilnik showCreateMenu(){
        System.out.println("Создание будильника");
        int m, h;
        boolean status = false;
        Scanner in = new Scanner(System.in);
        System.out.println("Введите час");
        h = in.nextInt();
        System.out.println("Введите минуты");
        m = in.nextInt();
        System.out.println("Хотите включить будильник? (y/n)");
        if(in.next().equals("y")){ status = true;}
        return new Budilnik(m,h,status);
    }

    public static void showDeleteMenu(BudilnikRepository repo){
        Scanner in = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            System.out.println("Какой будильник вы хотите удалить, введите номер или 0 для выхода");
            int i = in.nextInt() - 1;
            if (i >= 0 && i < repo.budilniks.size()) {
                Budilnik budilnik = repo.find(i);
                budilnik.showInfo();
                System.out.println("Вы уверенны что хотите удалить данный будильник (y/n)");
                if (in.next().equals("y")) {
                    repo.deletBudilnik(budilnik);
                    clearConsole();
                    System.out.println("Операция выполнена");
                } else {
                    clearConsole();
                    System.out.println("Операция отменена");
                }
                flag = false;
            } else if (i == -1) {
                flag = false;
                clearConsole();
            } else {
                System.out.println("Введено не верное значение");
            }
        }

    }


    public static void clearConsole (){
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }


}