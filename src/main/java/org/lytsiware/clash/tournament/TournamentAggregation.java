package org.lytsiware.clash.tournament;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class TournamentAggregation {

    public static final String IN_PROGRESS = "inProgress";
    public static final String IN_PREPARATION = "inPreparation";
    private TournamentFinder tournamentFinder;

    public TournamentAggregation(TournamentFinder tournamentFinder) {
        this.tournamentFinder = tournamentFinder;
    }


    public Map<String, List<Tournament>> filterTournaments(Set<Tournament> tournaments) {
        Map<String, List<Tournament>> result = tournaments.stream().parallel().filter(tournament -> "open".equals(tournament.getType()))
                .filter(tournament -> tournament.getMaxCapacity() != 1000)
                .filter(tournament -> tournament.getMaxCapacity() != tournament.getCapacity())
                .collect(Collectors.groupingBy(Tournament::getStatus));
        result.putIfAbsent(IN_PROGRESS, new ArrayList<>());
        result.putIfAbsent(IN_PREPARATION, new ArrayList<>());
        System.out.println("Tournaments filtered out: " + (tournaments.size() - result.values().stream().mapToInt(List::size).sum()));
        return result;
    }


    public void sortTournaments(Map<String, List<Tournament>> tournamentsPerType) {
        Comparator<Tournament> inProgressComparator = Comparator.comparing(Tournament::getMaxCapacity).reversed()
                .thenComparing(Comparator.comparing(Tournament::getCapacity))
                .thenComparing(Comparator.comparing(Tournament::getCreatedTime).reversed());

        Comparator<Tournament> inPreparationComparator = Comparator.comparing(Tournament::getMaxCapacity).reversed()
                .thenComparing(Comparator.comparing(Tournament::getCapacity))
                .thenComparing(Comparator.comparing(Tournament::getCreatedTime));

        tournamentsPerType.get(IN_PROGRESS).sort(inProgressComparator);
        tournamentsPerType.get(IN_PREPARATION).sort(inPreparationComparator);

    }

    public Map<String, List<Tournament>> filterAndSortTournaments(Set<Tournament> tournaments) {
        Map<String, List<Tournament>> result = filterTournaments(tournaments);
        sortTournaments(result);
        return result;
    }

    public Map<String, List<Tournament>> findTournamentsAndFilterSort() {
        Set<Tournament> tournaments = tournamentFinder.getTournamentsRecursivelly("");
        System.out.println("Tournaments found : " + tournaments.size());
        return filterAndSortTournaments(tournaments);
    }

    public static void main(String[] args) throws IOException {
        Function<List, String> customListToString = (List l) -> (String) l.stream().map(Object::toString).collect(Collectors.joining("\r\n"));
        Map<String, List<Tournament>> result = new TournamentAggregation(new TournamentFinder()).findTournamentsAndFilterSort();
        System.out.println("\r\n");
        System.out.println("STARTED \r\n" + customListToString.apply(result.get(IN_PROGRESS)));
        System.out.println("PREPARATION \r\n" + customListToString.apply(result.get(IN_PREPARATION)));
    }


}
