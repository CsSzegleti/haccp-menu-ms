package hu.soft4d.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "allergen")
@Entity
public class Allergen extends PanacheEntity {

    @Schema(required = true)
    @Column(name = "long_name")
    public String longName;

    @Schema(required = true)
    @Column(name = "short_name")
    public String shortName;

}
