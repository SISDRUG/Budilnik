import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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

        CommandInvoker invoker = new CommandInvoker();
        invoker.setCommand("1", new CreateAlarmCommand(alarmRepository));
        invoker.setCommand("2", new ShowAlarmsCommand(alarmRepository, task));
        invoker.setCommand("3", new DeleteAlarmCommand(alarmRepository));
        invoker.setCommand("4", new SaveAlarmsCommand(alarmRepository));
        invoker.setCommand("5", new ClearAlarmsCommand(alarmRepository));
        invoker.setCommand("6", new TurnOffAlarmsCommand(alarmRepository));
        invoker.setCommand("stop", new PauseTaskCommand(task));
        invoker.setCommand("0", new ExitCommand(childThread));

        while (true) {
            showMainMenu();
            input = in.next();
            clearConsole();



            invoker.executeCommand(input);
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

        Scanner in = new Scanner(System.in);
        String input = "init";

        CommandInvoker invoker = new CommandInvoker();
        invoker.setCommand("1", new CreateAlarmCommand(alarmRepository));
        invoker.setCommand("2", new TurnOffAlarmCommand(alarmRepository, in));
        invoker.setCommand("3", new TurnOnAlarmCommand(alarmRepository, in));
        invoker.setCommand("4", new DeleteAlarmCommand(alarmRepository));
        invoker.setCommand("5", new ClearAlarmsCommand(alarmRepository));
        invoker.setCommand("6", new SortAlarmsCommand(alarmRepository));
        invoker.setCommand("7", new SortReverseAlarmsCommand(alarmRepository));
        invoker.setCommand("stop", new PauseTaskCommand(task));

        while (!"0".equals(input)) {
            System.out.println("Будильники:");
            alarmRepository.showAlarms();
            System.out.println("1. Создать будильник");
            System.out.println("2. Выключить будильник");
            System.out.println("3. Включить будильник");
            System.out.println("4. Удалить будильник");
            System.out.println("5. Удалить все будильники");
            System.out.println("6. Отсортировать по возрастанию");
            System.out.println("7. Отсортировать по убыванию");
            System.out.println("0. Выход");
            input = in.next();
            clearConsole();




            invoker.executeCommand(input);
        }
    }

    //region Commands for alarms menu

    static class TurnOffAlarmCommand implements Command {
        private final AlarmRepository alarmRepository;
        private final Scanner in;

        public TurnOffAlarmCommand(AlarmRepository alarmRepository, Scanner in) {
            this.alarmRepository = alarmRepository;
            this.in = in;
        }

        @Override
        public void execute() {
            AlarmRepository activeAlarmRepository = alarmRepository.showActive();
            boolean flag = true;
            while (flag) {
                System.out.println("Какой будильник вы хотите выключить, введите номер или 0 для выхода");
                int i = in.nextInt() - 1;
                if (i >= 0 && i < alarmRepository.alarms.size()) {
                    Alarm alarm = activeAlarmRepository.find(i);
                    alarm.showInfo();
                    System.out.println("Вы уверены что хотите выключить данный будильник (y/any key)");
                    if (in.next().equals("y")) {
                        alarm.setStatus(false);
                        System.out.println("Операция выполнена");
                        saveAlarms(alarmRepository);
                    } else {
                        System.out.println("Операция отменена");
                    }
                    flag = false;
                } else if (i == -1) {
                    flag = false;
                } else {
                    System.out.println("Введено не верное значение");
                }
            }
        }
    }

    static class TurnOnAlarmCommand implements Command {
        private final AlarmRepository alarmRepository;
        private final Scanner in;

        public TurnOnAlarmCommand(AlarmRepository alarmRepository, Scanner in) {
            this.alarmRepository = alarmRepository;
            this.in = in;
        }

        @Override
        public void execute() {
            AlarmRepository inActiveAlarmRepository = alarmRepository.showInActive();
            boolean flag = true;
            while (flag) {
                System.out.println("Какой будильник вы хотите включить, введите номер или 0 для выхода");
                int i = in.nextInt() - 1;
                if (i >= 0 && i < alarmRepository.alarms.size()) {
                    Alarm alarm = inActiveAlarmRepository.find(i);
                    alarm.showInfo();
                    System.out.println("Вы уверены что хотите включить данный будильник (y/any key)");
                    if (in.next().equals("y")) {
                        alarm.setStatus(true);
                        System.out.println("Операция выполнена");
                        saveAlarms(alarmRepository);
                    } else {
                        System.out.println("Операция отменена");
                    }
                    flag = false;
                } else if (i == -1) {
                    flag = false;
                } else {
                    System.out.println("Введено не верное значение");
                }
            }
        }
    }

    static class SortAlarmsCommand implements Command {
        private final AlarmRepository alarmRepository;

        public SortAlarmsCommand(AlarmRepository alarmRepository) {
            this.alarmRepository = alarmRepository;
        }

        @Override
        public void execute() {
            alarmRepository.alarms.sort(new AlarmComparator());
        }
    }

    static class SortReverseAlarmsCommand implements Command {
        private final AlarmRepository alarmRepository;

        public SortReverseAlarmsCommand(AlarmRepository alarmRepository) {
            this.alarmRepository = alarmRepository;
        }

        @Override
        public void execute() {
            alarmRepository.alarms.sort(new AlarmReverseComparator());
        }
    }
    //endregion
    //region Main menu commands

    static class CreateAlarmCommand implements Command {
        private final AlarmRepository alarmRepository;

        public CreateAlarmCommand(AlarmRepository alarmRepository) {
            this.alarmRepository = alarmRepository;
        }

        @Override
        public void execute() {
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
    }

    static class ShowAlarmsCommand implements Command {
        private final AlarmRepository alarmRepository;
        private final TimeThread task;

        public ShowAlarmsCommand(AlarmRepository alarmRepository, TimeThread task) {
            this.alarmRepository = alarmRepository;
            this.task = task;
        }

        @Override
        public void execute() {
            showAlarmsMenu(alarmRepository, task);
        }

    }

    static class DeleteAlarmCommand implements Command {
        private final AlarmRepository alarmRepository;

        public DeleteAlarmCommand(AlarmRepository alarmRepository) {
            this.alarmRepository = alarmRepository;
        }

        @Override
        public void execute() {
            System.out.println("Удаление будильника:");
            alarmRepository.showAlarms();
            if (!alarmRepository.alarms.isEmpty()) {
                showDeleteMenu(alarmRepository);
                saveAlarms(alarmRepository);
            } else {
                System.out.println("В данный момент нет будильников для удаления");
            }
        }
    }

    static class SaveAlarmsCommand implements Command {
        private final AlarmRepository alarmRepository;

        public SaveAlarmsCommand(AlarmRepository alarmRepository) {
            this.alarmRepository = alarmRepository;
        }

        @Override
        public void execute() {
            System.out.println("Сохранение начато, дождитесь окончания");
            System.out.printf("Сохранение закончено: %s \n", saveAlarms(alarmRepository));
        }
    }

    static class ClearAlarmsCommand implements Command {
        private final AlarmRepository alarmRepository;

        public ClearAlarmsCommand(AlarmRepository alarmRepository) {
            this.alarmRepository = alarmRepository;
        }

        @Override
        public void execute() {
            alarmRepository.clear();
            System.out.println("Будильники очищены");
            saveAlarms(alarmRepository);
        }
    }

    static class TurnOffAlarmsCommand implements Command {
        private final AlarmRepository alarmRepository;

        public TurnOffAlarmsCommand(AlarmRepository alarmRepository) {
            this.alarmRepository = alarmRepository;
        }

        @Override
        public void execute() {
            alarmRepository.offAll();
            System.out.println("Будильники выключены");
            saveAlarms(alarmRepository);
        }
    }

    static class PauseTaskCommand implements Command {
        private final TimeThread task;

        public PauseTaskCommand(TimeThread task) {
            this.task = task;
        }

        @Override
        public void execute() {
            task.pause();
        }
    }

    static class ExitCommand implements Command {
        private final Thread childThread;

        public ExitCommand(Thread childThread) {
            this.childThread = childThread;
        }

        @Override
        public void execute() {
            childThread.interrupt();
            System.exit(0);
        }
    }

    static class CommandInvoker {
        private final Map<String, Command> commandMap = new HashMap<>();

        public void setCommand(String input, Command command) {
            commandMap.put(input, command);
        }

        public void executeCommand(String input) {
            Command command = commandMap.get(input);
            if (command != null) {
                command.execute();
            } else if (Objects.equals(input, "0")) {
                System.out.println("Возврат в главное меню");
            } else {
                System.out.printf("%s отсутствует как функция \n", input);
            }
        }
    }

    //endregion
}