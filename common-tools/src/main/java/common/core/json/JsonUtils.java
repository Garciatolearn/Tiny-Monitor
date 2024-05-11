package common.core.json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonUtils {

    ObjectMapper mapper = new ObjectMapper();

    public <T> String toJson(T data) throws JsonProcessingException {
        return mapper.writeValueAsString(data);
    }

    public <T> T toObject(String data,Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(data,clazz);
    }
}
