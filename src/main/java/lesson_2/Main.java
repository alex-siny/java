package lesson_2;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static Connection connection;
    private static Statement statement;
    private static final Scanner sc = new Scanner(System.in);

    /**
     * С формулировкой заданий можно ознакомится в файле README.md
     */
    public static void main(String[] args)  {

        try {
            connect();
            createProductsTable();
            clearProductsTable();
            generateProducts(10000);

            while (true) {
                String command = sc.nextLine();
                String[] tokens = command.split(" ");
                if (command.isEmpty()) continue;
                if (command.equals("выход")) break;
                if (command.startsWith("цена ")) {
                    if (tokens.length != 2) {
                        System.out.println("Формат команды не верный.");
                        continue;
                    }
                    showProductPrice(tokens[1]);
                } else if (command.startsWith("сменитьцену ")) {
                    if (tokens.length != 3) {
                        System.out.println("Формат команды не верный.");
                        continue;
                    }
                    changeProductPrice(tokens[1], Integer.parseInt(tokens[2]));
                } else if (command.startsWith("товарыпоцене ")) {
                    if (tokens.length != 3) {
                        System.out.println("Формат команды не верный.");
                        continue;
                    }
                    showProductsByPrice(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                } else {
                    System.out.println("Не удалось распознать команду.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                sc.close();
                disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void showProductPrice(String name) throws SQLException {
        Product product = getProductByName(name);
        if (product == null) {
            System.out.println("Такого товара нет.");
            return;
        }
        product.showInfo();
    }

    private static void changeProductPrice(String name, int price) throws SQLException {
        Product product = getProductByName(name);
        if (product == null) {
            System.out.println("Такого товара нет.");
            return;
        }
        if (price <= 0) {
            System.out.println("Цена на товар должна быть больше нуля.");
            return;
        }
        if (product.getPrice() == price) {
            System.out.println("Цена на товар '" + product.getName() + "' уже равна " + price);
            return;
        }
        statement.executeUpdate(
            String.format("UPDATE `products` SET `price` = '%d' WHERE `name` = '%s'", price, name)
        );
        System.out.println("Цена для товара '" + name + "' изменена с " + product.getPrice() + " на " + price);
    }

    private static void showProductsByPrice(int priceFrom, int priceTo) throws SQLException {
        ResultSet result = statement.executeQuery(
            String.format("SELECT * FROM `products` WHERE `price` BETWEEN %d AND %d", priceFrom, priceTo)
        );
        while (result.next()) {
            Product product  = new Product(result.getString(2), result.getInt(3));
            product.showInfo();
        }
        if (result.getFetchSize() == 0) System.out.println("Список товаров с указанным диапазоном цен пуст.");
    }

    private static void createProductsTable() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS `products` (" +
                "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "`name` TEXT NOT NULL UNIQUE, " +
                "`price` INTEGER NOT NULL" +
                ");");
    }

    private static void clearProductsTable() throws SQLException {
        statement.execute("DELETE FROM `products`;");
    }

    private static void generateProducts(int count) throws SQLException {
        if (count == 0 || count < 0) return;
        connection.setAutoCommit(false);
        final PreparedStatement prepareStatement = connection.prepareStatement(
                "INSERT INTO `products` (`name`, `price`) VALUES (?,?)"
        );
        for (int i = 0; i < count; i++) {
            prepareStatement.setString(1, "товар" + i);
            prepareStatement.setInt(2, i + 10);
            prepareStatement.addBatch();
        }
        prepareStatement.executeBatch();
        connection.setAutoCommit(true);
    }

    private static Product getProductByName(String name) throws SQLException {
        ResultSet result = statement.executeQuery(String.format("SELECT * FROM `products` WHERE `name` = '%s'", name));
        if (result.next()) {
            return new Product(result.getString(2), result.getInt(3));
        }
        return null;
    }

    private static void disconnect() throws SQLException {
        connection.close();
    }

    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:main.db");
        statement = connection.createStatement();
    }
}