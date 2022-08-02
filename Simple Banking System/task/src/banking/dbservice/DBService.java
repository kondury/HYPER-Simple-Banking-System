package banking.dbservice;

import banking.Card;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBService {

    public Connection getConnection() {
        return connection;
    }

    private final Connection connection;
    public DBService(String dbFileName) {
        connection = getSQLiteConnection(dbFileName);
        createCardTable();
    }

    private void createCardTable() {
        try (Statement stmt = connection.createStatement();) {
            stmt.execute("create table if not exists card (\n" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "number TEXT,\n" +
                    "pin TEXT,\n" +
                    "balance INTEGER DEFAULT 0\n" +
                    ")\n"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Connection getSQLiteConnection(String dbFileName) {
        try {
            String url = "jdbc:sqlite:" + dbFileName;
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Card getCard(long cardNumber) throws DBException {
        try {
            return (new CardDao(connection).getByNumberOrNull(cardNumber));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public long getLastCardNumberOrDefault(long defaultValue) throws DBException {
        try {
            return (new CardDao(connection).getLastCardNumberOrDefault(defaultValue));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void addCard(Card card) throws DBException {
        try {
            connection.setAutoCommit(false);
            CardDao dao = new CardDao(connection);
            dao.insertCard(card.getCardNumber(), card.getPin());
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public void addIncome(Card account, long income) throws DBException {
        changeBalance(account, income);
    }

    public void subExpense(Card account, long expense) throws DBException {
        changeBalance(account, -expense);
    }

    private void changeBalance(Card account, long change) throws DBException {
        try {
            new CardDao(connection).changeBalance(account, change);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void close() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DBException(e);
        }

    }

    public void removeAccount(Card account) throws DBException {
        try {
            new CardDao(connection).removeCard(account);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void transfer(Card fromAccount, Card toAccount, long sum) throws DBException {
        try {
            connection.setAutoCommit(false);
            new CardDao(connection).changeBalance(fromAccount, -sum);
            new CardDao(connection).changeBalance(toAccount, sum);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }
}
