package edu.rlperez.java_general_web_demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(value = "BREWERY_TYPES")
@NoArgsConstructor
@AllArgsConstructor
public class BreweryType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    @OneToMany
    private List<Brewery> breweries;

    public BreweryType(String type) {
        this.type = type;
    }
}