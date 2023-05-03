package hu.soft4d.resource.dto;

import hu.soft4d.model.StoringCondition;

import java.util.List;

public class MenuItemDto {

    public Long id;

    public String name;

    public double price;

    public String currency;

    public Long categoryId;

    public String description;

    public List<String> imgUrls;

    public List<Long> allergenIds;

    public boolean isPreparable;

    public StoringCondition storingCondition;
}
