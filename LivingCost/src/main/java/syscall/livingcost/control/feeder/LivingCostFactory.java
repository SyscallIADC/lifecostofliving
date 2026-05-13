package syscall.livingcost.control.feeder;
import syscall.livingcost.control.webScraper.NumbeoLivingCostData;
import syscall.livingcost.model.LivingCost;

import java.time.LocalTime;
import java.time.LocalDate;

public class LivingCostFactory {

    public static LivingCost fromNumbeoData(NumbeoLivingCostData numbeoData, String countryName) {
        return new LivingCost(
                countryName,
                numbeoData.currency(),
                numbeoData.cappuccino(),
                numbeoData.milk(),
                numbeoData.rice(),
                numbeoData.bread(),
                numbeoData.eggs(),
                numbeoData.cheese(),
                numbeoData.chicken(),
                numbeoData.beef(),
                numbeoData.fruits(),
                numbeoData.vegetables(),
                numbeoData.water(),
                numbeoData.publicTransport(),
                numbeoData.gasoline(),
                numbeoData.car(),
                numbeoData.utilities(),
                numbeoData.childCare(),
                numbeoData.gymMonthly(),
                numbeoData.bedroomMonth(),
                numbeoData.apartmentMonth(),
                numbeoData.apartmentBuy(),
                numbeoData.salaryMonth(),
                numbeoData.interestRate()
        );
    }
}
