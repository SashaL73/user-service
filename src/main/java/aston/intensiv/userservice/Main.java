package aston.intensiv.userservice;

import aston.intensiv.userservice.config.HibernateConfig;
import aston.intensiv.userservice.dao.UserDaoImpl;
import aston.intensiv.userservice.model.User;
import aston.intensiv.userservice.service.UserService;
import aston.intensiv.userservice.service.UserServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl(
                new UserDaoImpl(HibernateConfig.getSessionFactory()));

        menu();
        while (true) {
            System.out.print("> ");
            String line = sc.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            Command command = Command.fromString(parts[0]);

            switch (command) {
                case CREATE -> createUser(parts, userService);
                case GET -> getUser(parts, userService);
                case GET_ALL -> getAllUsers(userService);
                case UPDATE -> updateUser(userService);
                case DELETE -> deleteUser(parts, userService);
                case HELP -> menu();
                case EXIT -> {
                    System.out.println("Выход.");
                    HibernateConfig.shutdown();
                    sc.close();
                    return;
                }
                default -> System.out.println("Неизвестная команда. Введите help.");
            }

        }
    }

    private static void getUser(String[] parts, UserService userService) {
        if (parts.length != 2) {
            System.out.println("Некорректно введен id : get <id>");
            return;
        }

        Long id = Long.parseLong(parts[1]);
        User user = userService.getUserById(id);

        if (user == null) {
            System.out.println("Пользователь не найден");
            return;
        }

        System.out.println(user);
    }

    private static void createUser(String[] parts, UserService userService) {
        if (parts.length != 4) {
            System.out.println("Некорректно введены данные: create <name> <email> <age>");
            return;
        }

        String name = parts[1];
        String email = parts[2];
        int age = Integer.parseInt(parts[3]);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        User savedUser = userService.saveUser(user);

        System.out.println("Пользователь создан: " + savedUser);
    }

    private static void deleteUser(String[] parts, UserService userService) {
        if (parts.length != 2) {
            System.out.println("Некорректно введен id: delete <id>");
            return;
        }

        Long id = Long.parseLong(parts[1]);
        userService.deleteUser(id);
    }

    private static void updateUser(UserService userService) {
        try {
            System.out.print("Введите id пользователя для обновления: ");
            Long id = Long.parseLong(sc.nextLine());

            User findUser = userService.getUserById(id);

            if (findUser == null) {
                System.out.println("Пользователь не найден");
                return;
            }

            System.out.print("Введите новое имя: ");
            String name = sc.nextLine();

            System.out.print("Введите новый email: ");
            String email = sc.nextLine();

            System.out.print("Введите новый возраст: ");
            Integer age = Integer.parseInt(sc.nextLine());

            findUser.setName(name);
            findUser.setEmail(email);
            findUser.setAge(age);

            User updatedUser = userService.updateUser(findUser);
            System.out.println("Пользователь обновлён: " + updatedUser);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getAllUsers(UserService userService) {
        List<User> users = userService.findAllUsers();

        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст");
            return;
        }

        users.forEach(System.out::println);
    }


    static void menu() {
        System.out.println("""
                create <name> <email> <age>      - создать пользователя
                get <id>                         - получить пользователя по id
                get_all                          - получить всех пользователей
                update                           - обновить пользователя
                delete <id>                      - удалить пользователя
                help                             - показать список команд
                exit                             - выйти из программы
                """);
    }
}
