package hu.soft4d.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.List;

@Table(name = "menu_item")
@Entity
public class MenuItem extends PanacheEntity {

    @Column(length = 100)
    public String name;

    public double price;

    @ManyToOne
    public Category category;

    public String currency;

    public String description;

    @ElementCollection(targetClass = String.class)
    @JoinTable(name = "menu_item_img_urls")
    @CollectionTable(name = "menu_item_img_urls", joinColumns = @JoinColumn(name = "menu_item_id"))
    @Column(name = "img_url")
    public List<String> imgUrls;

    @ManyToMany
    @JoinTable(name = "menu_item_allergen",
        joinColumns = @JoinColumn(name = "menu_item_id"),
        inverseJoinColumns = @JoinColumn(name = "allergens_id"))
    public List<Allergen> allergens;

    @Column(name = "is_preparable")
    public boolean isPreparable;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "storing_condition_id", referencedColumnName = "id")
    public StoringCondition storingCondition;
}
