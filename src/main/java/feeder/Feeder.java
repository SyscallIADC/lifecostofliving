package feeder;

public interface Feeder<Model> {
    Model extractData(String fromCurrency, String toCurrency) throws Exception;

}
