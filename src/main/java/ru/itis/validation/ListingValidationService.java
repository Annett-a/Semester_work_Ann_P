package ru.itis.validation;

import ru.itis.model.ListingEntity;
import java.util.Map;

public interface ListingValidationService {
    Map<String, String> validate(ListingEntity e);
}
