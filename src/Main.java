/*
 ToDO
 *  1. Разобраться с потоками
 *  2. Добавить проверку на существования будильника с такими же параметрами
 *  3. Добавить сортировку и быстрые действия в меню просмотра будильников
 *  4. Посмотреть ошибки реализаций и посмотреть аналоги
 *  5. Добавить действие переключение статуса будильника после выключения
 *  6. Добавить проверку включен ли будильник
 *  7. Проверить Warn
*/


import org.codehaus.jackson.map.ObjectMapper;

import java.awt.desktop.AppEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner in = new Scanner(System.in);

        String input = "init";
        BudilnikRepository budilnikRepository = new BudilnikRepository();
        ObjectMapper objectMapper = new ObjectMapper();
        clearConsole();
        TimeThread task = new TimeThread();
        Thread childThread = new Thread(task);
        childThread.start();

        try {
            File file = new File("budilnik.json");
            budilnikRepository = objectMapper.readValue(file, BudilnikRepository.class);
            System.out.printf("Найдено %d сохраненных будильников \n", budilnikRepository.budilniks.size());
        } catch (IOException e) {
            System.out.println("Сохраненных будильников нет\n");
        }

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
                        saveBudilniks(budilnikRepository);
                    }else {
                        System.out.println("Такой будильник уже есть");
                    }
                }
                case "2" -> {
                    clearConsole();
                    System.out.println("Будильники:");
                    budilnikRepository.showBudilniks();
                    showBudilniksMenu(budilnikRepository);

                }
                case "3" -> {
                    System.out.println("Удаление будильника:");
                    budilnikRepository.showBudilniks();
                    if (!budilnikRepository.budilniks.isEmpty()) {
                        showDeleteMenu(budilnikRepository);
                        saveBudilniks(budilnikRepository);
                    }
                    else
                    {System.out.println("В данный момент нет будильников для удаления");}

                }
                case "4"->{
                    System.out.println("Сохранение начато дождитесь окончания");
                    System.out.printf("Сохранение законченно: %s \n",saveBudilniks(budilnikRepository));
                }

                case "5" ->{
                    budilnikRepository.clear();
                    System.out.println("Будильники очищены");
                    saveBudilniks(budilnikRepository);
                }

                case "6" ->{
                    budilnikRepository.offAll();
                    System.out.println("Будильники выключены");
                    saveBudilniks(budilnikRepository);
                }

                case "stop"->{
                    task.pause();
                }

                case "0" ->{
                    childThread.interrupt();
                    System.exit(0);

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
        System.out.println("5. Удалить все будильники");
        System.out.println("6. Выключить все будильники");
        System.out.println("0. Закрыть приложение");
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

    public static String saveBudilniks(BudilnikRepository budilnikRepository){
        try(FileWriter writer = new FileWriter("budilnik.json", false))
        {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(budilnikRepository);
            writer.write(json);
            writer.flush();
            return "Успешно";
        }
        catch(IOException ex){
            return (ex.getMessage());
        }
    }


    public static void clearConsole (){
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }

    public static void showBudilniksMenu(BudilnikRepository budilnikRepository){
        System.out.println("1. Создать будильник");
        System.out.println("2. Выключить будильник");
        System.out.println("3. Включить будильник");
        System.out.println("4. Удалить будильник");
        System.out.println("5. Удалить все будильники");
        System.out.println("6. Отсортировать по возрастанию");
        System.out.println("7. Отсортировать по убыванию");
        System.out.println("0. Выход");
        Scanner in = new Scanner(System.in);
        String input = "init";

        while (!"0".equals(input)){
            input = in.next();

            switch (input){

                case "1" ->{
                    clearConsole();
                    List<Integer> params = showCreateMenu();
                    if (params.isEmpty()) {
                        System.out.println("Операция отменена");
                    }
                    else if (budilnikRepository.addBudilnik(params)){
                        System.out.println("Будильник успешно создан");
                        saveBudilniks(budilnikRepository);
                    }else {
                        System.out.println("Такой будильник уже есть");
                    }
                    input = "0";
                }

                case "2" ->{
                    BudilnikRepository activeBudilnikRepository = budilnikRepository.showActive();
                    boolean flag = true;
                    while (flag) {
                        System.out.println("Какой будильник вы хотите выключить, введите номер или 0 для выхода");
                        int i = in.nextInt() - 1;
                        if (i >= 0 && i < budilnikRepository.budilniks.size()) {
                            Budilnik budilnik = activeBudilnikRepository.find(i);
                            budilnik.showInfo();
                            System.out.println("Вы уверенны что хотите выключить данный будильник (y/any key)");
                            if (in.next().equals("y")) {
                                budilnik.setStatus(false);
                                clearConsole();
                                System.out.println("Операция выполнена");
                                saveBudilniks(budilnikRepository);
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
                    input = "0";
                }

                case "3" ->{
                    BudilnikRepository inActiveBudilnikRepository = budilnikRepository.showInActive();
                    boolean flag = true;
                    while (flag) {
                        System.out.println("Какой будильник вы хотите включить, введите номер или 0 для выхода");
                        int i = in.nextInt() - 1;
                        if (i >= 0 && i < budilnikRepository.budilniks.size()) {
                            Budilnik budilnik = inActiveBudilnikRepository.find(i);
                            budilnik.showInfo();
                            System.out.println("Вы уверенны что хотите включить данный будильник (y/any key)");
                            if (in.next().equals("y")) {
                                budilnik.setStatus(true);
                                clearConsole();
                                System.out.println("Операция выполнена");
                                saveBudilniks(budilnikRepository);
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
                    input = "0";
                }

                case "4" ->{
                    System.out.println("Удаление будильника:");
                    budilnikRepository.showBudilniks();
                    if (!budilnikRepository.budilniks.isEmpty()) {
                        showDeleteMenu(budilnikRepository);
                        saveBudilniks(budilnikRepository);
                    }
                    else
                    {System.out.println("В данный момент нет будильников для удаления");}
                    input = "0";
                }

                case "5" ->{
                    budilnikRepository.clear();
                    System.out.println("Будильники очищены");
                    saveBudilniks(budilnikRepository);
                    showBudilniksMenu(budilnikRepository);
                    input = "0";
                }

                case "6" ->{
                    Collections.sort(budilnikRepository.budilniks, new BudilnikComparator());
                    input = "0";
                }

                case "7" ->{
                    Collections.sort(budilnikRepository.budilniks, new BudilnikReverseComparator());
                    input = "0";
                }

                case null, default -> System.out.printf("%s отсутствует как функция \n", input);
            }
            clearConsole();
        }
    }

}