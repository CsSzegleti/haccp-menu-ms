package hu.soft4d.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "allergen")
@Entity
public class Allergen extends PanacheEntity {

    @Column(name = "long_name")
    public String longName;

    @Column(name = "short_name")
    public String shortName;

}
