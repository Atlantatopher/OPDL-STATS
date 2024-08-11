package com.opdl.api.stats.comparator;

import com.opdl.api.stats.model.Match;
import com.opdl.api.stats.model.TeamStandings;
import com.opdl.api.stats.service.MatchService;
import com.opdl.api.stats.service.QualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@Component
public class StandingsComparator implements Comparator <TeamStandings> {

    @Autowired
    private MatchService matchService;

    @Autowired
    private QualityService qualityService;


    public StandingsComparator() {
    }

    /**
     * @param team the first object to be compared.
     * @param otherTeam the second object to be compared.
     * @return
     */
    @Override
    public int compare(TeamStandings team, TeamStandings otherTeam) {
        int result = Double.compare(otherTeam.getWinPercentage(), team.getWinPercentage());
        if ( result == 0 ) {
            result = Integer.compare(otherTeam.getScore(), team.getScore());
        } if ( result == 0 ) {
            Match headToHeadMatch = matchService.getHeadToHeadMatch(team.getTeam().getSeasonId(), team.getTeam(), otherTeam.getTeam());
            if(team.getTeam() == headToHeadMatch.getHomeTeam()){
                result = Integer.compare(headToHeadMatch.getAwayScore(), headToHeadMatch.getHomeScore());
            } else {
                result = Integer.compare(headToHeadMatch.getHomeScore(), headToHeadMatch.getAwayScore());
            }
        } if ( result == 0 ) {
            // compare the two teams total quality points
            HashMap<String, String> teamQuality = qualityService.calcutlateTeamQuality(team.getTeam().getId());
            HashMap<String, String> otherTeamQuality = qualityService.calcutlateTeamQuality(otherTeam.getTeam().getId());
            int teamQualityTotal = teamQuality.get("teamTotal") == null ? 0 : Integer.parseInt(teamQuality.get("teamTotal"));
            int otherTeamQualityTotal = otherTeamQuality.get("teamTotal") == null ? 0 : Integer.parseInt(otherTeamQuality.get("teamTotal"));
            result = Integer.compare(otherTeamQualityTotal, teamQualityTotal);
        }

        return result;
    }

    /**
     * @return
     */
    @Override
    public Comparator<TeamStandings> reversed() {
        return Comparator.super.reversed();
    }

    /**
     * @param other the other comparator to be used when this comparator
     *              compares two objects that are equal.
     * @return
     */
    @Override
    public Comparator<TeamStandings> thenComparing(Comparator<? super TeamStandings> other) {
        return Comparator.super.thenComparing(other);
    }

    /**
     * @param keyExtractor  the function used to extract the sort key
     * @param keyComparator the {@code Comparator} used to compare the sort key
     * @param <U>
     * @return
     */
    @Override
    public <U> Comparator<TeamStandings> thenComparing(Function<? super TeamStandings, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return Comparator.super.thenComparing(keyExtractor, keyComparator);
    }

    /**
     * @param keyExtractor the function used to extract the {@link
     *                     Comparable} sort key
     * @param <U>
     * @return
     */
    @Override
    public <U extends Comparable<? super U>> Comparator<TeamStandings> thenComparing(Function<? super TeamStandings, ? extends U> keyExtractor) {
        return Comparator.super.thenComparing(keyExtractor);
    }

    /**
     * @param keyExtractor the function used to extract the integer sort key
     * @return
     */
    @Override
    public Comparator<TeamStandings> thenComparingInt(ToIntFunction<? super TeamStandings> keyExtractor) {
        return Comparator.super.thenComparingInt(keyExtractor);
    }

    /**
     * @param keyExtractor the function used to extract the long sort key
     * @return
     */
    @Override
    public Comparator<TeamStandings> thenComparingLong(ToLongFunction<? super TeamStandings> keyExtractor) {
        return Comparator.super.thenComparingLong(keyExtractor);
    }

    /**
     * @param keyExtractor the function used to extract the double sort key
     * @return
     */
    @Override
    public Comparator<TeamStandings> thenComparingDouble(ToDoubleFunction<? super TeamStandings> keyExtractor) {
        return Comparator.super.thenComparingDouble(keyExtractor);
    }
}
