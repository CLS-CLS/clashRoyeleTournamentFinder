package org.lytsiware.clash.tournament;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class TournamentFinderTest {


    @Test
    public void testMapping() throws IOException {
        String tournanent = "{\"tag\":\"#URL0J99\",\"type\":\"passwordProtected\",\"status\":\"inPreparation\",\"creatorTag\":\"#9YRQCJGYG\",\"name\":\"atropaa\"," +
                "\"description\":\"vemnabota\",\"capacity\":3,\"maxCapacity\":50,\"preparationDuration\":7200,\"duration\":3600,\"createdTime\":\"20180919T200037.000Z\"}";

        Tournament tournament = new TournamentFinder().getObjectMapper().readValue(tournanent, Tournament.class);

        assertEquals(LocalDateTime.of(2018, 9, 19, 20, 0, 37), tournament.getCreatedTime());
    }
}