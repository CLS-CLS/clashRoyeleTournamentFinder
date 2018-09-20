package org.lytsiware.clash.tournament;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TournamentsResource {
    Map<String, Object> paging;

    @JsonAlias("items")
    List<Tournament> tournaments;
}
