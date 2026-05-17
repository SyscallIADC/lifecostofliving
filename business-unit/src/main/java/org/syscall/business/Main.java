package org.syscall.business;

import org.syscall.business.control.datamart.DatamartDAO;
import org.syscall.business.control.datamart.DatamartRepository;
import org.syscall.business.control.data_loader.HistorialDataLoader;
import org.syscall.business.control.network.BusinessSubscriber;
import org.syscall.business.control.cli.CLIController;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Arrancando módulo Business Unit...");

        DatamartRepository datamart = new DatamartDAO();

        HistorialDataLoader.load(datamart);

        BusinessSubscriber subscriber = new BusinessSubscriber(datamart);
        subscriber.start(List.of("LivingCost", "ExchangeRate"));

        CLIController cli = new CLIController(datamart);
        cli.startMenu();

        subscriber.stop();
        System.out.println("Ejecución finalizada.");
    }
}