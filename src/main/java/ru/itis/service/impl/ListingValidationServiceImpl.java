package ru.itis.service.impl;

import ru.itis.model.ListingEntity;
import ru.itis.validation.ListingValidationService;

import java.util.HashMap;
import java.util.Map;

public class ListingValidationServiceImpl implements ListingValidationService {
    @Override
    public Map<String, String> validate(ListingEntity e) {
        var errors = new HashMap<String, String>();
        if (e.getTitle() == null || e.getTitle().isBlank()) errors.put("title", "Название обязательно");
        if (e.getType() == null  || e.getType().isBlank())  errors.put("type",  "Тип обязателен");
        if (e.getStatus()== null || e.getStatus().isBlank())errors.put("status","Статус обязателен");
        return errors;
    }
}
