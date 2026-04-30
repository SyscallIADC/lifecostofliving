package org.syscall.exchangerate;

import org.syscall.exchangerate.control.database.DatabaseHelper;
import org.syscall.exchangerate.control.database.ExchangeRateStore;
import org.syscall.exchangerate.control.database.SQLiteExchangeRateStore;
import org.syscall.exchangerate.control.feeder.APIExchangeRateFeeder;
import org.syscall.exchangerate.control.feeder.ExchangeRateFeeder;
import org.syscall.exchangerate.control.Controller;


public class Main {
    public static void main(String[] args) {
        DatabaseHelper.createTables();

        ExchangeRateFeeder feeder = new APIExchangeRateFeeder();
        ExchangeRateStore store = new SQLiteExchangeRateStore();
        Controller controller = new Controller(feeder, store);

        Runtime.getRuntime().addShutdownHook(new Thread(controller::stop));

        controller.start();
    }
}