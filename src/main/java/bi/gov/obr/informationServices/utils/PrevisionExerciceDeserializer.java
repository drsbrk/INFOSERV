package bi.gov.obr.informationServices.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import bi.gov.obr.informationServices.entity.Exercice;

import java.io.IOException;

public class PrevisionExerciceDeserializer extends JsonDeserializer<Exercice> {
    @Override
    public Exercice deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        final ObjectCodec objectCodec = jsonParser.getCodec();
        final JsonNode node = objectCodec.readTree(jsonParser);
        System.out.println(" node ====> prettystring " + node.toPrettyString());
        node.elements().forEachRemaining(element -> {
            System.out.println(" element ===>> ===> || ==> " + element.toPrettyString());
        });
        node.fieldNames().forEachRemaining(field -> {
            System.out.println(" field ===>> ===> || ==> " + field);
        });
        node.findParents("id").forEach(parent -> {
            System.out.println(" parent ===>> ===> || ==> " + parent.toPrettyString());
        });
        System.out.println(" ===>> ===> || ==> " + node.asText());
        final Long exerciceId = node.asLong();
        Exercice exercice = new Exercice();
        exercice.setId(exerciceId);
        return exercice;
    }
}
