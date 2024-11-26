package johnoliveira.eventTracker_capstoneProject.converters;

import johnoliveira.eventTracker_capstoneProject.enums.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

// classe per convertire in maiuscolo le category ricercate dall'utente
@Component
public class StringToCategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String source) {
        try {
            return Category.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category value: " + source);
        }
    }
}
