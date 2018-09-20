package org.lytsiware.clash.tournament;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class TournamentAggregation {

    private TournamentFinder tournamentFinder;

    public TournamentAggregation(TournamentFinder tournamentFinder) {
        this.tournamentFinder = tournamentFinder;
    }


    public Map<String, List<Tournament>> filterTournaments(Set<Tournament> tournaments) {
        return tournaments.stream().parallel().filter(tournament -> "open".equals(tournament.getType()))
                .filter(tournament -> tournament.getMaxCapacity() != 1000)
                .filter(tournament -> tournament.getMaxCapacity() != tournament.getCapacity())
                .collect(Collectors.groupingBy(Tournament::getStatus));
    }


    public void sortTournaments(Map<String, List<Tournament>> tournamentsPerType) {
        Comparator<Tournament> inProgressComparator = Comparator.comparing(Tournament::getMaxCapacity).reversed()
                .thenComparing(Comparator.comparing(Tournament::getCapacity))
                .thenComparing(Comparator.comparing(Tournament::getCreatedTime).reversed());

        Comparator<Tournament> inPreparationComparator = Comparator.comparing(Tournament::getMaxCapacity).reversed()
                .thenComparing(Comparator.comparing(Tournament::getCapacity))
                .thenComparing(Comparator.comparing(Tournament::getCreatedTime));

        tournamentsPerType.get("inProgress").sort(inProgressComparator);
        tournamentsPerType.get("inPreparation").sort(inPreparationComparator);

    }

    public Map<String, List<Tournament>> filterAndSortTournaments(Set<Tournament> tournaments) {
        Map<String, List<Tournament>> result = filterTournaments(tournaments);
        sortTournaments(result);
        return result;
    }

    public Map<String, List<Tournament>> findTournamentsAndFilterSort() {
        return filterAndSortTournaments(tournamentFinder.getTournaments(TournamentFinder.SEARCH_TOKENS));
    }

    public static void main(String[] args) throws IOException {
        Function<List, String> customListToString = (List l) -> (String) l.stream().map(Object::toString).collect(Collectors.joining("\r\n"));
        Map<String, List<Tournament>> result = new TournamentAggregation(new TournamentFinder()).findTournamentsAndFilterSort();

        System.out.println("STARTED \r\n" + customListToString.apply(result.get("inProgress")));
        System.out.println("PREPARATION \r\n" + customListToString.apply(result.get("inPreparation")));
    }
}
