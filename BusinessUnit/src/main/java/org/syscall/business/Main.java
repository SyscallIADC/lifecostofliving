package org.syscall.business;

import org.syscall.business.control.BusinessController;
import org.syscall.business.control.analysis.TrendAnalyzer;
import org.syscall.business.view.CLIView;
import org.syscall.business.control.data_loader.HistorialDataLoader;
import org.syscall.business.control.datamart.DatabaseHelper;
import org.syscall.business.control.datamart.DatamartDAO;
import org.syscall.business.control.datamart.DatamartRepository;
import org.syscall.business.control.network.BusinessSubscriber;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String eventStorePath = args.length > 0 ? args[0] : "eventstore";
        DatabaseHelper.initializeDatabase();

        DatamartRepository datamart = new DatamartDAO();
        TrendAnalyzer trendAnalyzer = new TrendAnalyzer(datamart);

        HistorialDataLoader.load(datamart, eventStorePath);

        BusinessSubscriber subscriber = new BusinessSubscriber(datamart);
        subscriber.start(List.of("LivingCost", "ExchangeRate"));

        BusinessController controller = new BusinessController(datamart, trendAnalyzer);

        CLIView cli = new CLIView(controller);
        cli.startMenu();

        subscriber.stop();
    }
}