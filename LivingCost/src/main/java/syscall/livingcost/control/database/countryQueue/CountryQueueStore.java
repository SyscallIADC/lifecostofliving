package syscall.livingcost.control.database.countryQueue;

public interface CountryQueueStore {
    String getNextCountry();
    void setCountryAsScrapped(String country);
}
