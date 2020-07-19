package edu.rlperez.java_general_web_demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OpenBreweryDBServiceTest {

    private static final String response1 =
            "  {\n" +
                    "    id: 299,\n" +
                    "    name: \"Almanac Beer Company\",\n" +
                    "    brewery_type: \"micro\",\n" +
                    "    street: \"651B W Tower Ave\",\n" +
                    "    city: \"Alameda\",\n" +
                    "    state: \"California\",\n" +
                    "    postal_code: \"94501-5047\",\n" +
                    "    country: \"United States\",\n" +
                    "    longitude: \"-122.306283180899\",\n" +
                    "    latitude: \"37.7834497667258\",\n" +
                    "    phone: \"4159326531\",\n" +
                    "    website_url: \"http://almanacbeer.com\",\n" +
                    "    updated_at: \"2018-08-23T23:24:11.758Z\",\n" +
                    "    tag_list: []\n" +
                    "  }";

    private static final String response2 =
            "{\n" +
                    "    \"id\": 253,\n" +
                    "    \"name\": \"Rapp's Barren Brewing Company\",\n" +
                    "    \"brewery_type\": \"micro\",\n" +
                    "    \"street\": \"1343 E 9th St\",\n" +
                    "    \"city\": \"Mountain Home\",\n" +
                    "    \"state\": \"Arkansas\",\n" +
                    "    \"postal_code\": \"72653-5050\",\n" +
                    "    \"country\": \"United States\",\n" +
                    "    \"longitude\": \"-92.3599724\",\n" +
                    "    \"latitude\": \"36.3326432\",\n" +
                    "    \"phone\": \"8704247288\",\n" +
                    "    \"website_url\": \"http://www.rappsbarrenbrewing.com\",\n" +
                    "    \"updated_at\": \"2018-08-23T23:23:29.428Z\",\n" +
                    "    \"tag_list\": []\n" +
                    "}";

    @Test
    public void testJSONMarshalling() throws JsonProcessingException {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();

        OpenBreweryDBService.BreweryResponse b =
                gson.fromJson(response1, OpenBreweryDBService.BreweryResponse.class);

        assertEquals(299, b.getId());
        assertEquals("Almanac Beer Company", b.getName());
        assertEquals("micro", b.getBreweryType());
        assertEquals("651B W Tower Ave", b.getStreet());
        assertEquals("Alameda", b.getCity());
        assertEquals("California", b.getState());
        assertEquals("United States", b.getCountry());
        assertEquals("94501-5047", b.getPostalCode());
        assertEquals("4159326531", b.getPhone());
        assertEquals("http://almanacbeer.com", b.getWebsiteUrl());
        assertEquals("2018-08-23T23:24:11.758Z", b.getUpdatedAt());
    }

    private String mockResponse(List<String> responses) {
        return "[" + String.join(",", responses) + "]";
    }
}
