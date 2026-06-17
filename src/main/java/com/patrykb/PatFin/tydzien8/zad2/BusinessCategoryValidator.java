package com.patrykb.PatFin.tydzien8.zad2;
import com.patrykb.PatFin.model.Category;
import org.springframework.stereotype.Component;

@Component
public class BusinessCategoryValidator extends AbstractCategoryValidator {
    @Override
    public void validate(Category category) {
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            throwValidationError("Nazwa kategorii nie może być pusta.");
        }
        if (category.getName().length() < 3) {
            throwValidationError("Nazwa musi mieć co najmniej 3 znaki.");
        }
    }
}