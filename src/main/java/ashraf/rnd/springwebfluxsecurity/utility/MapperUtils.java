package ashraf.rnd.springwebfluxsecurity.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class MapperUtils {

    private static final String OBJECT_TO_STRING_MAPPING_FAILED = "Could not convert object to string {}";
    private static final String OBJECT_TO_BYTES_MAPPING_FAILED = "Could not convert object to bytes {}";
    private static final String OBJECT_TO_CLASS_MAPPING_FAILED = "Could not convert object {} to class {}";
    private static final String BYTES_TO_CLASS_MAPPING_FAILED = "Could not convert bytes to class {}";

    private final ObjectMapper objectMapper;

    public String serialize(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            log.error(OBJECT_TO_STRING_MAPPING_FAILED, data, ex);
            return null;
        }
    }

    public byte[] serializeToBytes(Object data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException ex) {
            log.error(OBJECT_TO_BYTES_MAPPING_FAILED, data, ex);
            return new byte[0];
        }
    }

    public <T> T deserialize(Object data, Class<T> tClass) {
        try {
            return objectMapper.readValue(String.valueOf(data), tClass);
        } catch (Exception ex) {
            log.error(OBJECT_TO_CLASS_MAPPING_FAILED, data, tClass, ex);
            return null;
        }
    }

    public <T> T deserialize(byte[] bytes, Class<T> tClass) {
        try {
            return objectMapper.readValue(bytes, tClass);
        } catch (Exception ex) {
            log.error(BYTES_TO_CLASS_MAPPING_FAILED, tClass, ex);
            return null;
        }
    }

    public <T> T convert(Object data, Class<T> tClass) {
        return deserialize(serialize(data), tClass);
    }

    public <T> T deserialize(String data, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(data, valueTypeRef);
        } catch (Exception ex) {
            log.error(OBJECT_TO_CLASS_MAPPING_FAILED, data, valueTypeRef, ex);
            return null;
        }
    }

    public <T> T convertValue(Object fromValue, Class<T> toValueType) {
        try {
            return objectMapper.convertValue(fromValue, toValueType);
        } catch (Exception ex) {
            log.error(OBJECT_TO_CLASS_MAPPING_FAILED, fromValue, toValueType, ex);
            return null;
        }
    }
}
