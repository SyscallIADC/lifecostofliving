package models;
import java.util.Map;

public interface ModelParser<Model> {
    Map<String, Object> toMap(Model model);
    Model fromMap(Map<String, Object> map);
}