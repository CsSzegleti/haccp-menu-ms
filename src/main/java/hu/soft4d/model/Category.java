package hu.soft4d.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Table(name = "category")
@Entity
public class Category extends PanacheEntity {

    @Schema(required = true)
    public String name;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    public List<MenuItem> items = new ArrayList<>();

    @Column(name = "menu_card_pos")
    @Schema(required = true)
    public int menuCardPos;


    public Category() {
    }
}
