/*
 ToDO
 *  1. Создать класс и обьект Будильник
 *  2. Создать репозиторий для хранения обьектов типа Будильник
 *  3. Можно создавать будильники на определенное время (мин/час)
 *  4. Реализация консольного интерфейса
 *  5. Сохранение в csv или json
*/


import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;
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
                    List<Integer> params = showCreateMenu();
                    if (params.isEmpty()) {
                        System.out.println("Операция отменена");
                    }
                    else if (budilnikRepository.addBudilnik(params)){
                        System.out.println("Будильник успешно создан");
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
                case "4"->{

                }

                case null, default -> System.out.printf("%s отсутствует как функция \n", input);
            }
        }
    }

    public static void showMainMenu (){
        System.out.println("1. Создать будильник");
        System.out.println("2. Просмотреть будильники");
        System.out.println("3. Удалить будильник");
        System.out.println("4. Сохранить будильники");
    }

    public static List<Integer> showCreateMenu(){
        System.out.println("Создание будильника");
        int m, h;
        int status = 0;
        Scanner in = new Scanner(System.in);
        h = getHour();
        if (h < 0){
            return List.of();
        }
        m = getMinutes();
        if (m < 0){
            return List.of();
        }
        System.out.println("Хотите включить будильник? (y/any key)");
        if(in.next().equals("y")){ status = 1;}
        return List.of(m,h,status);
    }

    public static int getHour() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите час (0-23) или exit для отмены:");

        try {
            String input = scanner.nextLine();

            if (!input.equalsIgnoreCase("exit")){
                int hour = Integer.parseInt(input);
                if (hour < 0 || hour > 23) {
                    System.out.println("Ошибка: час должен быть в диапазоне от 0 до 23.");
                    return getHour();
                }
                else {
                    return hour;
                }
            }
            else {return -1;}

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное целое число.");
            return getHour();
        }
    }

    public static int getMinutes() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите минуты (0-59) или exit для отмены: ");

        try {
            String input = scanner.nextLine();

            if (!input.equalsIgnoreCase("exit")){
                int minutes = Integer.parseInt(input);
                if (minutes < 0 || minutes > 59) {
                    System.out.println("Ошибка: час должен быть в диапазоне от 0 до 59.");
                    return getMinutes();
                }
                else {
                    return minutes;
                }
            }
            else {return -1;}

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное целое число.");
            return getMinutes();
        }
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
                System.out.println("Вы уверенны что хотите удалить данный будильник (y/any key)");
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