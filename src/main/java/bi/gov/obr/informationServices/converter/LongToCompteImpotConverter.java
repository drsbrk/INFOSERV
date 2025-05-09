package bi.gov.obr.informationServices.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import bi.gov.obr.informationServices.config.LongToCompteImpot;
import bi.gov.obr.informationServices.entity.CompteImpot;

import java.util.Collections;
import java.util.Set;

public class LongToCompteImpotConverter implements ConditionalGenericConverter {
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.getAnnotation(LongToCompteImpot.class) != null;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Long.class, CompteImpot.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        CompteImpot compteImpot = new CompteImpot();
//        compteImpot.setId((Long) source);
        return compteImpot;
    }
}
