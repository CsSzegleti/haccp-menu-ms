package hu.soft4d.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;

@Table(name = "storing_condition")
@Entity
public class StoringCondition extends PanacheEntity {

    @Schema(required = true)
    public float temperature;

    public float humidity;

    @Schema(required = true)
    @Column(name = "max_storing_hours")
    public int maxStoringHours;
}
