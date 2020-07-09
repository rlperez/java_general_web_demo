package edu.rlperez.java_general_web_demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(value = "BREWERIES")
@NoArgsConstructor
@AllArgsConstructor
public class Brewery {

    @Id
    private long id;
    private long externalId;
    private String breweryName;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phone;
    private String websiteUrl;
    @ManyToOne
    private BreweryType breweryType;
    @OneToMany
    private List<Beer> beers;
    private LocalDateTime updatedAt;
}
