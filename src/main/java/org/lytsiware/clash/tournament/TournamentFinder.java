package org.lytsiware.clash.tournament;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class TournamentFinder {


    public static final String[] SEARCH_TOKENS = {":", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    private ObjectMapper objectMapper;
    private final String url;
    private String token;

    public TournamentFinder() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("configuration.properties"));
        url = properties.getProperty("url");
        token = properties.getProperty("token");
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }


    public TournamentsResource getTournaments(String name) {
        try {
            URL url = new URL(this.url + "?name=" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("authorization", token);
            connection.setRequestProperty("accept", "application/json");
            TournamentsResource resource = objectMapper.readValue(connection.getInputStream(), TournamentsResource.class);
            if (resource.getTournaments().size() == 60) {
                System.out.println("Max limit reached for search token : " + name);
            }
            return resource;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Tournament> getTournamentsRecursivelly(String word) {
        return Arrays.stream(SEARCH_TOKENS).parallel().map(letter -> word + letter)
                .peek(searchToken -> System.out.println("Search token : " + searchToken))
                .flatMap(searchToken -> {
                    Set<Tournament> tournaments = new HashSet<>(getTournaments(searchToken).getTournaments());
                    if (tournaments.size() == 60) {
                        tournaments.addAll(getTournamentsRecursivelly(searchToken));
                    }
                    return tournaments.stream();
                }).collect(Collectors.toSet());
    }


    public Set<Tournament> getTournaments(String... names) {
        return Arrays.stream(names).parallel().map(this::getTournaments)
                .map(TournamentsResource::getTournaments)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }


    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }


}
