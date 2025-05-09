package bi.gov.obr.informationServices.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import bi.gov.obr.informationServices.enums.PrevisionType;

import java.io.IOException;

public class PrevisionTypeSerializer extends JsonSerializer<PrevisionType> {
    @Override
    public void serialize(PrevisionType previsionType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(previsionType.name());
        System.out.println(" ===>> ===> || ==> " + previsionType.name());
    }
}
