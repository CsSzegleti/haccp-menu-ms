package hu.soft4d.resource.converter;

import hu.soft4d.model.Allergen;
import hu.soft4d.model.Category;
import hu.soft4d.model.MenuItem;
import hu.soft4d.model.StoringCondition;
import hu.soft4d.resource.dto.MenuItemDto;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.stream.Collectors;

@ApplicationScoped
public class MenuItemConverter {

    public MenuItemDto toExternal(MenuItem entity) {
        MenuItemDto dto = new MenuItemDto();

        dto.id = entity.id;
        dto.imgUrls = entity.imgUrls;
        dto.price = entity.price;
        dto.currency = entity.currency;
        dto.description = entity.description;
        dto.isPreparable = entity.isPreparable;
        dto.name = entity.name;
        dto.storingCondition = entity.storingCondition;
        dto.allergenIds = entity.allergens.stream().map(allergen -> allergen.id).collect(Collectors.toList());
        dto.categoryId = entity.category.id;

        return dto;
    }

    @Transactional
    public MenuItem toBusiness(MenuItemDto dto) {
        MenuItem entity = new MenuItem();
        entity.id = dto.id;
        entity.imgUrls = dto.imgUrls;
        entity.price = dto.price;
        entity.currency = dto.currency;
        entity.description = dto.description;
        entity.isPreparable = dto.isPreparable;
        entity.name = dto.name;
        entity.storingCondition = (StoringCondition) StoringCondition.findByIdOptional(dto.storingCondition.id).orElseThrow(EntityNotFoundException::new);
        entity.storingCondition.temperature = dto.storingCondition.temperature;
        entity.storingCondition.humidity = dto.storingCondition.humidity;
        entity.storingCondition.maxStoringHours = dto.storingCondition.maxStoringHours;

        entity.allergens = dto.allergenIds.stream().map(allergenId -> (Allergen) Allergen.findByIdOptional(allergenId).orElseThrow(EntityNotFoundException::new)).collect(Collectors.toList());
        entity.category = (Category) Category.findByIdOptional(dto.categoryId).orElseThrow(EntityNotFoundException::new);

        return entity;
    }
}
