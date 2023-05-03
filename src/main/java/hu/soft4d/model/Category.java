package hu.soft4d.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Table(name = "category")
@Entity
public class Category extends PanacheEntity {

    public String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    public List<MenuItem> items = new ArrayList<>();

    @Column(name = "menu_card_pos")
    public int menuCardPos;


    public Category() {
    }
}
