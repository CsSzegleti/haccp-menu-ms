package hu.soft4d.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;

@Table(name = "storing_condition")
@Entity
public class StoringCondition extends PanacheEntity {

    public float temperature;

    public float humidity;

    @Column(name = "max_storing_hours")
    public int maxStoringHours;
}
