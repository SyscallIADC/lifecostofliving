package consumer;

public interface Consumer<Model> {
    Model extractData(String fromCurrency, String toCurrency) throws Exception;

}
