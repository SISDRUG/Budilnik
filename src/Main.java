import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "alarm.json";
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String input = "init";
        AlarmRepository alarmRepository = new AlarmRepository();
        ObjectMapper objectMapper = new ObjectMapper();
        clearConsole();
        TimeThread task = new TimeThread();
        Thread childThread = new Thread(task);
        childThread.start();

        try {
            File file = new File(FILE_NAME);
            alarmRepository = objectMapper.readValue(file, AlarmRepository.class);
            System.out.printf("Найдено %d сохраненных будильников \n", alarmRepository.alarms.size());
        } catch (IOException e) {
            System.out.println("Сохраненных будильников нет\n");
        }

        while (true) {
            showMainMenu();
            input = in.next();
            clearConsole();
            switch (input) {
                case "1" -> {
                    clearConsole();
                    List<Integer> params = showCreateMenu();
                    if (params.isEmpty()) {
                        System.out.println("Операция отменена");
                    } else if (alarmRepository.addAlarm(params)) {
                        System.out.println("Будильник успешно создан");
                        saveAlarms(alarmRepository);
                    } else {
                        System.out.println("Такой будильник уже есть");
                    }
                }
                case "2" -> {
                    clearConsole();
                    System.out.println("Будильники:");
                    alarmRepository.showAlarms();
                    showAlarmsMenu(alarmRepository, task);

                }
                case "3" -> {
                    System.out.println("Удаление будильника:");
                    alarmRepository.showAlarms();
                    if (!alarmRepository.alarms.isEmpty()) {
                        showDeleteMenu(alarmRepository);
                        saveAlarms(alarmRepository);
                    } else {
                        System.out.println("В данный момент нет будильников для удаления");
                    }

                }
                case "4" -> {
                    System.out.println("Сохранение начато дождитесь окончания");
                    System.out.printf("Сохранение законченно: %s \n", saveAlarms(alarmRepository));
                }

                case "5" -> {
                    alarmRepository.clear();
                    System.out.println("Будильники очищены");
                    saveAlarms(alarmRepository);
                }

                case "6" -> {
                    alarmRepository.offAll();
                    System.out.println("Будильники выключены");
                    saveAlarms(alarmRepository);
                }

                case "stop" -> task.pause();

                case "0" -> {
                    childThread.interrupt();
                    System.exit(0);

                }

                case null, default -> System.out.printf("%s отсутствует как функция \n", input);
            }
        }
    }

    public static void showMainMenu() {
        System.out.println("1. Создать будильник");
        System.out.println("2. Просмотреть будильники");
        System.out.println("3. Удалить будильник");
        System.out.println("4. Сохранить будильники");
        System.out.println("5. Удалить все будильники");
        System.out.println("6. Выключить все будильники");
        System.out.println("0. Закрыть приложение");
    }

    public static List<Integer> showCreateMenu() {
        System.out.println("Создание будильника");
        int m, h;
        int status = 0;
        Scanner in = new Scanner(System.in);
        h = getHour();
        if (h < 0) {
            return List.of();
        }
        m = getMinutes();
        if (m < 0) {
            return List.of();
        }
        System.out.println("Хотите включить будильник? (y/any key)");
        if (in.next().equals("y")) {
            status = 1;
        }
        return List.of(m, h, status);
    }

    public static int getHour() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите час (0-23) или exit для отмены:");

        try {
            String input = scanner.nextLine();

            if (!input.equalsIgnoreCase("exit")) {
                int hour = Integer.parseInt(input);
                if (hour < 0 || hour > 23) {
                    System.out.println("Ошибка: час должен быть в диапазоне от 0 до 23.");
                    return getHour();
                } else {
                    return hour;
                }
            } else {
                return -1;
            }

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

            if (!input.equalsIgnoreCase("exit")) {
                int minutes = Integer.parseInt(input);
                if (minutes < 0 || minutes > 59) {
                    System.out.println("Ошибка: час должен быть в диапазоне от 0 до 59.");
                    return getMinutes();
                } else {
                    return minutes;
                }
            } else {
                return -1;
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное целое число.");
            return getMinutes();
        }
    }


    public static void showDeleteMenu(AlarmRepository repo) {
        Scanner in = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            System.out.println("Какой будильник вы хотите удалить, введите номер или 0 для выхода");
            int i = in.nextInt() - 1;
            if (i >= 0 && i < repo.alarms.size()) {
                Alarm alarm = repo.find(i);
                alarm.showInfo();
                System.out.println("Вы уверенны что хотите удалить данный будильник (y/any key)");
                if (in.next().equals("y")) {
                    repo.deleteAlarm(alarm);
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

    public static String saveAlarms(AlarmRepository alarmRepository) {
        try (FileWriter writer = new FileWriter(FILE_NAME, false)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(alarmRepository);
            writer.write(json);
            writer.flush();
            return "Успешно";
        } catch (IOException ex) {
            return (ex.getMessage());
        }
    }


    public static void clearConsole() {
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }

    public static void showAlarmsMenu(AlarmRepository alarmRepository, TimeThread task) {
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

        while (!"0".equals(input)) {
            input = in.next();

            switch (input) {

                case "1" -> {
                    clearConsole();
                    List<Integer> params = showCreateMenu();
                    if (params.isEmpty()) {
                        System.out.println("Операция отменена");
                    } else if (alarmRepository.addAlarm(params)) {
                        System.out.println("Будильник успешно создан");
                        saveAlarms(alarmRepository);
                    } else {
                        System.out.println("Такой будильник уже есть");
                    }
                    input = "0";
                }

                case "2" -> {
                    AlarmRepository activeAlarmRepository = alarmRepository.showActive();
                    boolean flag = true;
                    while (flag) {
                        System.out.println("Какой будильник вы хотите выключить, введите номер или 0 для выхода");
                        int i = in.nextInt() - 1;
                        if (i >= 0 && i < alarmRepository.alarms.size()) {
                            Alarm alarm = activeAlarmRepository.find(i);
                            alarm.showInfo();
                            System.out.println("Вы уверенны что хотите выключить данный будильник (y/any key)");
                            if (in.next().equals("y")) {
                                alarm.setStatus(false);
                                clearConsole();
                                System.out.println("Операция выполнена");
                                saveAlarms(alarmRepository);
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

                case "3" -> {
                    AlarmRepository inActiveAlarmRepository = alarmRepository.showInActive();
                    boolean flag = true;
                    while (flag) {
                        System.out.println("Какой будильник вы хотите включить, введите номер или 0 для выхода");
                        int i = in.nextInt() - 1;
                        if (i >= 0 && i < alarmRepository.alarms.size()) {
                            Alarm alarm = inActiveAlarmRepository.find(i);
                            alarm.showInfo();
                            System.out.println("Вы уверенны что хотите включить данный будильник (y/any key)");
                            if (in.next().equals("y")) {
                                alarm.setStatus(true);
                                clearConsole();
                                System.out.println("Операция выполнена");
                                saveAlarms(alarmRepository);
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

                case "4" -> {
                    System.out.println("Удаление будильника:");
                    alarmRepository.showAlarms();
                    if (!alarmRepository.alarms.isEmpty()) {
                        showDeleteMenu(alarmRepository);
                        saveAlarms(alarmRepository);
                    } else {
                        System.out.println("В данный момент нет будильников для удаления");
                    }
                    input = "0";
                }

                case "5" -> {
                    alarmRepository.clear();
                    System.out.println("Будильники очищены");
                    saveAlarms(alarmRepository);
                    showAlarmsMenu(alarmRepository, task);
                    input = "0";
                }

                case "6" -> {
                    alarmRepository.alarms.sort(new AlarmComparator());
                    input = "0";
                }

                case "7" -> {
                    alarmRepository.alarms.sort(new AlarmReverseComparator());
                    input = "0";
                }

                case "stop" -> {
                    task.pause();
                    input = "0";
                }

                case null, default -> System.out.printf("%s отсутствует как функция \n", input);
            }
            clearConsole();
        }
    }

}