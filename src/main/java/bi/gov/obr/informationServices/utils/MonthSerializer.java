package bi.gov.obr.informationServices.utils;

import java.io.IOException;
import java.time.Month;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MonthSerializer extends JsonSerializer<Month> {
    @Override
    public void serialize(Month month, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(StringUtils.capitalize(month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRANCE)));
    }
}
