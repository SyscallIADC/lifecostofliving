package syscall.livingcost.control.feeder;

import com.google.gson.Gson;

public class Serializer {
    private static final Gson gson = new Gson();

    public static String serialize(Object event){
        return gson.toJson(event);
    }
}
