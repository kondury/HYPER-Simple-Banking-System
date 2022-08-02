package banking.dbservice;

import banking.Card;
import banking.dbservice.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class CardDao {

    private Executor executor;

    public CardDao(Connection connection) {
        this.executor = new Executor(connection);
    }

    public Card getByNumberOrNull(long cardNumber) throws SQLException {
        return executor.execQuery("select * from card where number='" + cardNumber + "'", result -> {
            if (result.next()) {
                return new Card(result.getLong(2), result.getInt(3), result.getLong(4));
            }
            return null;
        });
    }

    public long getLastCardNumberOrDefault(long defaultValue) throws SQLException {
        return executor.execQuery("select max(number) from card", result -> {
            result.next();
            long value = result.getLong(1);
            return value != 0 ? value : defaultValue;
        });
    }
    public void insertCard(long number, int pin) throws SQLException {
        executor.execUpdate("insert into card (number, pin) values ('" + number + "'," + "'" + pin + "')");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("drop table card");
    }

    public void changeBalance(Card card, long change) throws SQLException {
        executor.execUpdate("update card \n"
                + "set balance = balance + (" + change + ")\n"
                + "where number = " + card.getCardNumber());
    }

    public void removeCard(Card account) throws SQLException {
        executor.execUpdate("delete from card where number = " + account.getCardNumber());
    }
}
