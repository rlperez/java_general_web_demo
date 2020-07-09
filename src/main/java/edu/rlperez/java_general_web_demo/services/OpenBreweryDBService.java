package edu.rlperez.java_general_web_demo.services;

import com.google.gson.annotations.SerializedName;
import edu.rlperez.java_general_web_demo.models.BreweryType;
import edu.rlperez.java_general_web_demo.repositories.BreweryRepository;
import edu.rlperez.java_general_web_demo.repositories.BreweryTypeRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OpenBreweryDBService {

    private final BreweryRepository breweryRepository;

    private final BreweryTypeRepository breweryTypeRepository;

    @Autowired
    public OpenBreweryDBService(BreweryRepository breweryRepository, BreweryTypeRepository breweryTypeRepository) {
        this.breweryRepository = breweryRepository;
        this.breweryTypeRepository = breweryTypeRepository;
    }

    // Run every day from the time the service starts.
    @Scheduled(initialDelay = 0, fixedDelay = 86400000)
    public void perform() throws Exception {

    }


    @Getter
    @Setter
    static class BreweryResponse {
        private long id;
        private String name;
        private String street;
        private String city;
        private String state;
        @SerializedName("postal_code") private String postalCode;
        private String country;
        private String phone;
        @SerializedName("website_url") private String websiteUrl;
        @SerializedName("brewery_type") private String breweryType;
        @SerializedName("updated_at") private String updatedAt;
    }
}
