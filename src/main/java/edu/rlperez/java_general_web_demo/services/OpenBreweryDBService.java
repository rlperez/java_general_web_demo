package edu.rlperez.java_general_web_demo.services;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import edu.rlperez.java_general_web_demo.models.Brewery;
import edu.rlperez.java_general_web_demo.models.BreweryType;
import edu.rlperez.java_general_web_demo.repositories.BreweryRepository;
import edu.rlperez.java_general_web_demo.repositories.BreweryTypeRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.net.http.HttpClient.*;

@Service
public class OpenBreweryDBService {

    private final BreweryRepository breweryRepository;

    private final BreweryTypeRepository breweryTypeRepository;

    private final String OPENBREWERYDB_URL = "https://api.openbrewerydb.org/breweries?page=%d&per_page=50";

    private final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
            .optionalStart()
            .appendOffset("+HHMM", "+0000").optionalEnd()
            .optionalStart()
            .appendZoneId()
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .optionalEnd()
            .toFormatter()
            .withZone(ZoneOffset.UTC);

    @Autowired
    public OpenBreweryDBService(BreweryRepository breweryRepository, BreweryTypeRepository breweryTypeRepository) {
        this.breweryRepository = breweryRepository;
        this.breweryTypeRepository = breweryTypeRepository;
    }

    // Run every week from the time the service starts.
    @Scheduled(initialDelay = 0, fixedRate = 86400000L * 7)
    public void perform() {
        HttpClient client = getClient();
        int exceptionCount = 0;
        var requests = new LinkedList<CompletableFuture<Boolean>>();

        for (int i = 1; ; i++) {
            HttpRequest request = getRequest(i);

            requests.add(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::handleBody));

            if (requests.size() >= 10) {
                try {
                    if (reachedLastPage(requests)) break;
                    else requests.clear();
                } catch (InterruptedException | ExecutionException e) {
                    exceptionCount++;
                    e.printStackTrace();
                }
            } else if (exceptionCount > 10) {
                System.err.println("OpenBreweryDB exception count exceeded.");
                break;
            }
        }
    }

    private boolean handleBody(String requestBody) {
        BreweryResponse[] breweryResponses = new Gson().fromJson(requestBody, BreweryResponse[].class);
        List<Brewery> breweries = Arrays.stream(breweryResponses).map(breweryResponse -> {
            Brewery brewery = breweryRepository.findByExternalId(breweryResponse.getId()).orElse(new Brewery());
            LocalDateTime responseUpdatedAt = LocalDateTime.parse(breweryResponse.getUpdatedAt(), DATE_TIME_FORMATTER);
            if (brewery.getUpdatedAt() == null || brewery.getUpdatedAt().toEpochSecond(ZoneOffset.UTC) < responseUpdatedAt.toEpochSecond(ZoneOffset.UTC)) {
                // Source data updated so use that. A brewery likely updated information.
                brewery.setExternalId(breweryResponse.getId());
                brewery.setName(breweryResponse.getName());
                brewery.setStreet(Optional.ofNullable(breweryResponse.getStreet()).orElse(""));
                brewery.setCity(Optional.ofNullable(breweryResponse.getCity()).orElse(""));
                brewery.setState(Optional.ofNullable(breweryResponse.getState()).orElse(""));
                brewery.setPostalCode(Optional.ofNullable(breweryResponse.getPostalCode()).orElse(""));
                brewery.setCountry(Optional.ofNullable(breweryResponse.getCountry()).orElse(""));
                brewery.setPhone(Optional.ofNullable(breweryResponse.getPhone()).orElse(""));
                brewery.setWebsiteUrl(Optional.ofNullable(breweryResponse.getWebsiteUrl()).orElse(""));
                brewery.setUpdatedAt(LocalDateTime.now());

                Optional<BreweryType> maybeType = breweryTypeRepository
                        .findByType(breweryResponse.getBreweryType().toLowerCase());

                if (maybeType.isEmpty()) {
                    BreweryType breweryType = new BreweryType();
                    breweryType.setType(breweryResponse.getBreweryType().toLowerCase());
                    brewery.setBreweryType(breweryTypeRepository.save(breweryType));
                } else {
                    brewery.setBreweryType(maybeType.get());
                }

                return brewery;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        breweries = StreamSupport.stream(breweryRepository.saveAll(breweries).spliterator(), false)
                .collect(Collectors.toList());

        return breweries.size() >= 1;
    }

    private boolean reachedLastPage(List<CompletableFuture<Boolean>> requests) throws InterruptedException, ExecutionException {
        for (CompletableFuture<Boolean> r : requests) {
            if (!r.join()) {
                return true;
            }
        }

        return false;
    }

    private HttpClient getClient() {
        return newBuilder()
                .version(Version.HTTP_1_1)
                .followRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    private HttpRequest getRequest(int i) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format(OPENBREWERYDB_URL, i)))
                .timeout(Duration.ofMinutes(1))
                .header("Accept", "application/json")
                .GET()
                .build();
    }

    @Getter
    @Setter
    static class BreweryResponse {
        private long id;
        private String name;
        private String street;
        private String city;
        private String state;
        @SerializedName("postal_code")
        private String postalCode;
        private String country;
        private String phone;
        @SerializedName("website_url")
        private String websiteUrl;
        @SerializedName("brewery_type")
        private String breweryType;
        @SerializedName("updated_at")
        private String updatedAt;
    }
}
