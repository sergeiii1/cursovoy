package com.example.taskcontrol;

import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Класс, представляющий адаптер базы данных для работы с компьютерами и ошибками.
 */
public class DatabaseAdapter {
    /**
     * URL базы данных.
     */
    public static final String DB_URL = "jdbc:h2:/db/computersError";
    /**
     * Драйвер базы данных.
     */
    public static final String DB_Driver = "org.h2.Driver";
    /**
     * Название таблицы ошибок.
     */
    public static final String TableError="Error";
    /**
     * Название таблицы компьютеров.
     */
    public static final String TablePC="Computer";

    /**
     * Метод для получения соединения с базой данных.
     */
    public static void getDBConnection() {
        try {
            Class.forName(DB_Driver);
            Connection connection = DriverManager.getConnection(DB_URL);
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, TableError, null);
            //deleteTable(TableError);
            //deleteTable(TablePC);
            if (!rs.next()) {
                createTableComputer();
                createTableError();
            }
            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка SQL!");
        }
    }

    /**
     * Метод для удаления таблицы из базы данных.
     * @param Table - название таблицы
     */
    private static void deleteTable(String Table)
    {
        String deleteTableSQL = "DROP TABLE "+Table;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(deleteTableSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для создания таблицы ошибок в базе данных.
     */
    private static void createTableError() {
        String createTableSQL = "CREATE TABLE "+ TableError+ " ("
                + "ID INT NOT NULL auto_increment, "
                + "Date DATETIME NOT NULL, "
                + "Task varchar(100) NOT NULL, "
                + "Type INT NOT NULL, "
                + "ComputerID int NOT NULL, "
                + "Fixed bit NOT NULL, "
                + "PRIMARY KEY (ID),"
                + "FOREIGN KEY (ComputerID) REFERENCES "+TablePC +" (ID)"
                + ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * Метод для создания таблицы компьютеров в базе данных.
     */
    private static void createTableComputer() {
        String createTableSQL = "CREATE TABLE "+ TablePC+ " ("
                + "ID INT NOT NULL auto_increment, "
                + "Name varchar(20) NOT NULL UNIQUE, "
                + "PRIMARY KEY (ID) "
                + ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * Метод для получения списка компьютеров из базы данных.
     * @param elementsDAO - список компьютеров
     */
    public static void getComputers(ObservableList<Computer> elementsDAO) {
        String selection = "select * from "+ TablePC;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    Computer computer = new Computer.ComputerBuilder(rs.getString("Name")).setId(rs.getInt("ID")).build();
                    elementsDAO.add(computer);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для получения компьютерa из базы данных.
     * @param id - id компьютерf
     */
    public static Computer getComputer(int id) {
        String selection = "select * from "+ TablePC+" where ID="+id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                if (rs.next()) {
                    return new Computer.ComputerBuilder(rs.getString("Name")).setId(rs.getInt("ID")).build();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static void getTasks(ObservableList<ErrorTask> elementsDAO) {
        String selection = "select * from "+ TableError;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    ErrorTask element = new ErrorTask(rs.getString("Task"),LocalDateTime.of(rs.getDate("Date").toLocalDate(),rs.getTime("Date").toLocalTime()),rs.getBoolean("Fixed"),rs.getInt("ComputerID"),TypeError.fromInt(rs.getInt("Type")), rs.getInt("ID"));
                    elementsDAO.add(element);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void getTasksNonFixed(ObservableList<ErrorTask> elementsDAO,int idPC) {
        String selection = "select * from "+ TableError+" where Fixed='"+false +"' and ComputerID="+idPC+" order by Type DESC";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    ErrorTask element = new ErrorTask(rs.getString("Task"),LocalDateTime.of(rs.getDate("Date").toLocalDate(),rs.getTime("Date").toLocalTime()),rs.getBoolean("Fixed"),rs.getInt("ComputerID"),TypeError.fromInt(rs.getInt("Type")), rs.getInt("ID"));
                    elementsDAO.add(element);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод добавляет новый компьютер в базу данных.
     * @param computer Объект компьютера, который необходимо добавить
     * @return ID добавленного компьютера или -1, если произошла ошибка
     */
    public static int addComputer(Computer computer) {
        String insertTableSQL = "INSERT INTO "+ TablePC
                + " (Name) " + "VALUES "
                + "('"+computer.title+"')";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TablePC+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * Метод добавляет новую задачу ошибки в базу данных.
     * @param element Объект задачи ошибки, который необходимо добавить
     * @return ID добавленной задачи ошибки или -1, если произошла ошибка
     */
    public static int addErrorTask(ErrorTask element) {
        String insertTableSQL = "INSERT INTO "+ TableError
                    + " (Date,Task,Fixed,Type,ComputerID) " + "VALUES "
                    + "('"+element.dateTime+"','"+element.error+"','"+element.fixed+"',"+element.type.toInt()+","+element.computerID+")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableError+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * Метод редактирует задачу ошибки в базе данных.
     * @param element Объект задачи ошибки, который необходимо изменить
     * @return true, если задача ошибки успешно отредактирована, иначе - false
     */
    public static boolean editTask(ErrorTask element) {
        String updateTableSQL = String.format("UPDATE "+ TableError
                        + " SET Date='%s',Task='%s',Fixed='%s',Type ='%s',ComputerID='%s' where ID=%s;",
                element.dateTime,element.error,element.fixed,element.type.toInt(),element.computerID,element.id);
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(updateTableSQL);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Метод удаляет задачу ошибки из базы данных.
     * @param ID ID задачи ошибки, которую необходимо удалить
     * @return true, если задача ошибки успешно удалена, иначе - false
     */
    public static boolean deleteTask(int ID) {
        String deleteTableSQL = String.format("DELETE from "+ TableError +" WHERE ID=%s;",ID);
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(deleteTableSQL);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Метод удаляет компьютер из базы данных.
     * @param ID ID компьютера, который необходимо удалить
     * @return true, если компьютер успешно удален, иначе - false
     */
    public static boolean deletePC(int ID) {
        String deleteTableSQL = String.format("DELETE from "+ TablePC +" WHERE ID=%s;",ID);
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(deleteTableSQL);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
