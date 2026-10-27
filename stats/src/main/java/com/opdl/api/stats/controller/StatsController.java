package com.opdl.api.stats.controller;

import com.opdl.api.stats.comparator.StandingsComparator;
import com.opdl.api.stats.model.*;
import com.opdl.api.stats.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class StatsController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private StandingsComparator standingsComparator;


    @PostMapping("/stats/standings/team")
    public ResponseEntity<List<TeamStandings>> getTeamStandings(@RequestBody StandingsQuery query){
        ArrayList<TeamStandings> results = new ArrayList<TeamStandings>();
        if(query == null) {
            System.out.println("Query is null");
        } else {
            Optional<List<Schedule>> scheduleResults = getSchedules(query);
            if (scheduleResults.isPresent()) {
                scheduleResults.get().forEach(schedule -> {
                    Optional<List<Match>> matchResults = getMatchesBySchedule(query, schedule.getId());
                    if (matchResults.isPresent()) {
                        matchResults.get().forEach(match -> {
                            //System.out.println(match.toString());
                            TeamStandings homeTeamEntry = initializeTeamStandings(match, true);
                            TeamStandings awayTeamEntry = initializeTeamStandings(match, false);
                            Optional<List<Game>> gameResults = getGames(query, match);
                            if (gameResults.isPresent()) {
                                homeTeamEntry.setTotalGamesPlayed(gameResults.get().size() * 2);
                                awayTeamEntry.setTotalGamesPlayed(gameResults.get().size() * 2);
                                gameResults.get().forEach(game -> {
                                    //System.out.println(game.toString());
                                    homeTeamEntry.setScore(homeTeamEntry.getScore() + game.getHomeScore());
                                    awayTeamEntry.setScore(awayTeamEntry.getScore() + game.getAwayScore());
                                });
                                homeTeamEntry.setScoreAdjusted(homeTeamEntry.getScore());
                                if(match.getPostponedBy() > 0 && homeTeamEntry.getTeam().getId() == match.getPostponedBy()){
                                    homeTeamEntry.setScoreAdjusted(homeTeamEntry.getScoreAdjusted()-3);
                                }

                                awayTeamEntry.setScoreAdjusted(awayTeamEntry.getScore());
                                if(match.getPostponedBy() > 0 && awayTeamEntry.getTeam().getId() == match.getPostponedBy()){
                                    awayTeamEntry.setScoreAdjusted(awayTeamEntry.getScoreAdjusted()-3);
                                }
                            } else {
                                System.out.println("No GAMES found");
                            }
                            AtomicBoolean foundHomeTeam = new AtomicBoolean(false);
                            AtomicBoolean foundAwayTeam = new AtomicBoolean(false);
                            results.stream().forEach(result -> {
                                if(result.getTeam().getId() == homeTeamEntry.getTeam().getId()){
                                    result.setScore(result.getScore() + homeTeamEntry.getScore());
                                    result.setTotalGamesPlayed(result.getTotalGamesPlayed() + homeTeamEntry.getTotalGamesPlayed());
                                    result.setScoreAdjusted(result.getScoreAdjusted()+ homeTeamEntry.getScoreAdjusted());
                                    foundHomeTeam.set(true);
                                }
                                else if(result.getTeam().getId() == awayTeamEntry.getTeam().getId()){
                                    result.setScore(result.getScore() + awayTeamEntry.getScore());
                                    result.setTotalGamesPlayed(result.getTotalGamesPlayed() + awayTeamEntry.getTotalGamesPlayed());
                                    result.setScoreAdjusted(result.getScoreAdjusted()+ awayTeamEntry.getScoreAdjusted());
                                    foundAwayTeam.set(true);
                                }
                            });
                            if(!foundHomeTeam.get()){
                                results.add(homeTeamEntry);
                            }
                            if(!foundAwayTeam.get()){
                                results.add(awayTeamEntry);
                            }
                        });
                    } else {
                        System.out.println("No MATCHES found");
                    }
                });
            } else {
                System.out.println("No SCHEDULES found");
            }
        }

        ArrayList<TeamStandings> finalResults = new ArrayList<TeamStandings>();
        if(!results.isEmpty()){
            Collections.sort(results, standingsComparator);

            int topScore = results.get(0).getScore();
            finalResults = (ArrayList<TeamStandings>) results.stream().map(teamStanding -> {
                int curScore = teamStanding.getScore();
                int gamesBehind = topScore - curScore;
                double curPecentage = (curScore == 0 )? 0.0: (double) curScore / teamStanding.getTotalGamesPlayed();
                teamStanding.setNumGamesBehind(gamesBehind);
                teamStanding.setWinPercentage(curPecentage);
                return teamStanding;
            }).collect(Collectors.toList());
        }
        Collections.sort(finalResults, standingsComparator);
        return (!finalResults.isEmpty())? ResponseEntity.ok(finalResults): ResponseEntity.badRequest().build();
    }

    @PostMapping("/stats/standings/player")
    public ResponseEntity<List<PlayerStats>> getPlayerStats(@RequestBody StandingsQuery query){
        ArrayList<PlayerStats> results = new ArrayList<PlayerStats>();
        if(query == null) {
            System.out.println("Query is null");
        } else {
            Optional<List<Schedule>> scheduleResults = getSchedules(query);
            scheduleResults.ifPresent(schedules -> schedules.forEach(schedule -> {
                ArrayList<PlayerStats> weeklyResults = new ArrayList<PlayerStats>();
                        Optional<List<Match>> matchResults = getMatchesBySchedule(query, schedule.getId());
                matchResults.ifPresent(matches -> matches.forEach(match -> {
                    ArrayList<PlayerStats> matchStats = (ArrayList<PlayerStats>) Optional.of(results.stream()
                            .filter(stats -> (stats.getTeam() == match.getHomeTeam() || stats.getTeam() == match.getAwayTeam()))
                            .collect(Collectors.toList())).orElse(new ArrayList<PlayerStats>());
                            Optional<List<Game>> gameResults = getGames(query, match);
                    gameResults.ifPresent(games -> games.forEach(game -> {
                        AtomicBoolean homePlayerFound = new AtomicBoolean(false);
                        AtomicBoolean homePlayerDblFound = new AtomicBoolean(false);
                        AtomicBoolean awayPlayerFound = new AtomicBoolean(false);
                        AtomicBoolean awayPlayerDblFound = new AtomicBoolean(false);
                        PlayerStats homePlayer = initializePlayerStats(match, game, true, false);
                        PlayerStats homePlayerDBL = (game.isDoubles())?initializePlayerStats(match, game, true, true):new PlayerStats();
                        PlayerStats awayPlayer = initializePlayerStats(match, game, false, false);
                        PlayerStats awayPlayerDBL = (game.isDoubles())?initializePlayerStats(match, game, false, true):new PlayerStats();
                        matchStats.stream().forEach(player -> {
                            if(player.getPlayer() != null) {
                                if (homePlayer.getPlayer() != null && homePlayer.getPlayer().getId() == player.getPlayer().getId()) {
                                    updatePlayerStats(game, player, true);
                                    homePlayerFound.set(true);
                                } else if (awayPlayer.getPlayer() != null && awayPlayer.getPlayer().getId() == player.getPlayer().getId()) {
                                    updatePlayerStats(game, player, false);
                                    awayPlayerFound.set(true);
                                } else if (game.isDoubles()) {
                                    if (homePlayerDBL.getPlayer() != null && homePlayerDBL.getPlayer().getId() == player.getPlayer().getId()) {
                                        updatePlayerStats(game, player, true);
                                        homePlayerDblFound.set(true);
                                    } else if (awayPlayerDBL.getPlayer() != null && awayPlayerDBL.getPlayer().getId() == player.getPlayer().getId()) {
                                        updatePlayerStats(game, player, false);
                                        awayPlayerDblFound.set(true);
                                    }
                                }
                            }
                        });
                        if(!homePlayerFound.get()) matchStats.add(updatePlayerStats(game, homePlayer, true));
                        if(game.isDoubles() && !homePlayerDblFound.get()) matchStats.add(updatePlayerStats(game, homePlayerDBL, true));
                        if(!awayPlayerFound.get()) matchStats.add(updatePlayerStats(game, awayPlayer, false));
                        if(game.isDoubles() && !awayPlayerDblFound.get()) matchStats.add(updatePlayerStats(game, awayPlayerDBL, false));
                    }));
                    weeklyResults.addAll(matchStats);
                }));
                results.clear();
                results.addAll(weeklyResults);
            }));
        }
        Collections.sort(results);
        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @GetMapping("/stats/standings/player/overall/{season_id}")
    public ResponseEntity<ArrayList<HashMap<String, String>>> getPlayerOverallStatsBySeason(@PathVariable Long season_id){
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        StandingsQuery query = new StandingsQuery();
        query.setSeasonId(season_id);
        List<PlayerStats> playerStats = getPlayerStats(query).getBody();
        if (playerStats != null) {
            playerStats.forEach(player -> {
                if(null ==  player.getPlayer() || player.getPlayer().getPlayerName().isBlank() ){
                    return;
                }
                HashMap<String, String> overallStats = new HashMap<>();
                overallStats.put("PlayerName", player.getPlayer().getPlayerName());
                overallStats.put("OverallPerc", String.valueOf(player.getOverall().getWinPercentage()));
                results.add(overallStats);
            });
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @GetMapping("/stats/players/eligible")
    public ResponseEntity<ArrayList<HashMap<String, String>>> getPlayerEligibilty() {
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        long currentSeason = 6;
        Player playerQuery = new Player();
        playerQuery.setSeasonId(currentSeason);
        ArrayList<Player> players = (ArrayList<Player>) playerService.getquery(playerQuery);
        ArrayList<Player> eligiblePlayers = new ArrayList<>();
        Collections.sort(players);
        players.forEach(player -> {
            if(player.getPlayerName().isBlank()){
                return;
            }
            int gamesPlayed = gameService.getGamesByPlayer(player.getId())
                    .stream()
                    .filter(distinctByKey(Game::getMatch))
                    .map(Game::getMatch)
                    .toList().size();
            player.setPlayoffEligible(player.isPlayoffEligible() || gamesPlayed > 7);
            if (player.isPlayoffEligible()) {
                eligiblePlayers.add(player);
            }
            HashMap<String, String> playerEligibility = new HashMap<>();
            playerEligibility.put("PlayerName", player.getPlayerName());
            playerEligibility.put("PlayerTeam", player.getTeam().getTeamName());
            playerEligibility.put("MatchesPlayed", String.valueOf(gamesPlayed));
            playerEligibility.put("isPlayoffEligible", String.valueOf(player.isPlayoffEligible()));
            results.add(playerEligibility);
        });
        playerService.saveAll(eligiblePlayers);
        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @GetMapping("/stats/games/{id}")
    public ResponseEntity<ArrayList<Game>> getGamesByPlayer(@PathVariable Long id) {
        ArrayList<Game> games = (ArrayList<Game>) gameService.getGamesByPlayer(id);
        System.out.println("Game Count: "+games.size());
        return (!games.isEmpty())? ResponseEntity.ok(games): ResponseEntity.badRequest().build();
    }

    @GetMapping("/stats/matches/{id}")
    public ResponseEntity<ArrayList<Match>> getMatchesByPlayer(@PathVariable Long id) {
        ArrayList<Game> games = (ArrayList<Game>) gameService.getGamesByPlayer(id);
        List <Match> matches = games.stream().filter(distinctByKey(Game::getMatch)).map(Game::getMatch).collect(Collectors.toList());

        System.out.println("Match Count: "+matches.size());
        return (!matches.isEmpty())? ResponseEntity.ok((ArrayList<Match>) matches): ResponseEntity.badRequest().build();
    }

    @GetMapping("/stats/player/improved/{seasonId}")
    public ResponseEntity<ArrayList<HashMap<String, String>>> getMostImprovedPlayer(@PathVariable Long seasonId) {
        if (seasonId == null || seasonId < 2) return ResponseEntity.badRequest().build();
        ArrayList<HashMap<String, String>> results = getPlayerImprovements(seasonId).stream()
                .filter(improvement -> improvement.difference() > 0.0)
                .limit(3)
                .map(this::toPlayerImprovementResult)
                .collect(Collectors.toCollection(ArrayList::new));
        return !results.isEmpty() ? ResponseEntity.ok(results) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/stats/player/improved/{seasonId}/{universalId}")
    public ResponseEntity<HashMap<String, String>> getMostImprovedPlayer(
            @PathVariable Long seasonId, @PathVariable Long universalId) {
        if (seasonId == null || seasonId < 2 || universalId == null) {
            HashMap<String, String> error = new HashMap<>();
            error.put("Error", "Season ID must be at least 2 and universal ID must be provided");
            return ResponseEntity.badRequest().body(error);
        }

        Optional<PlayerImprovement> playerImprovement = getPlayerImprovements(seasonId).stream()
                .filter(improvement -> universalId.equals(improvement.stats().getPlayer().getUniversalId()))
                .findFirst();
        if (playerImprovement.isEmpty()) {
            HashMap<String, String> error = new HashMap<>();
            error.put("Error", "Player was not eligible or did not have stats in both seasons");
            return ResponseEntity.badRequest().body(error);
        }

        PlayerImprovement improvement = playerImprovement.get();
        if (improvement.difference() <= 0.0) {
            HashMap<String, String> error = new HashMap<>();
            error.put("Error", "Player did not improve season over season");
            error.put("Improvement", String.valueOf(improvement.difference()));
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok(toPlayerImprovementResult(improvement));
    }

    private List<PlayerImprovement> getPlayerImprovements(Long seasonId) {
        Optional<Schedule> lastSeasonSchedule = getLatestSchedule(seasonId - 1);
        Optional<Schedule> currentSeasonSchedule = getLatestSchedule(seasonId);
        if (lastSeasonSchedule.isEmpty() || currentSeasonSchedule.isEmpty()) return List.of();

        StandingsQuery queryLastSeason = new StandingsQuery(seasonId - 1);
        queryLastSeason.setWeekId((int) lastSeasonSchedule.get().getWeekId());
        StandingsQuery queryCurrentSeason = new StandingsQuery(seasonId);
        queryCurrentSeason.setWeekId((int) currentSeasonSchedule.get().getWeekId());

        List<PlayerStats> lastSeasonStats = getPlayerStats(queryLastSeason).getBody();
        List<PlayerStats> currSeasonStats = getPlayerStats(queryCurrentSeason).getBody();
        if (lastSeasonStats == null || currSeasonStats == null) return List.of();

        Map<Long, PlayerStats> eligibleLastSeason = lastSeasonStats.stream()
                .filter(this::isEligiblePlayerWithUniversalId)
                .collect(Collectors.toMap(
                        stats -> stats.getPlayer().getUniversalId(),
                        Function.identity(),
                        (first, duplicate) -> first));

        List<PlayerImprovement> improvements = new ArrayList<>();
        for (PlayerStats currentStats : currSeasonStats) {
            if (!isEligiblePlayerWithUniversalId(currentStats)) continue;

            PlayerStats previousStats = eligibleLastSeason.get(currentStats.getPlayer().getUniversalId());
            if (previousStats == null) continue;

            double previousWinPercentage = previousStats.getOverall().getWinPercentage();
            double currentWinPercentage = currentStats.getOverall().getWinPercentage();
            if (!Double.isFinite(previousWinPercentage) || !Double.isFinite(currentWinPercentage)) continue;

            double improvement = currentWinPercentage - previousWinPercentage;
            improvements.add(new PlayerImprovement(currentStats, previousWinPercentage, currentWinPercentage, improvement));
        }

        return improvements.stream()
                .sorted(Comparator.comparingDouble(PlayerImprovement::difference).reversed())
                .toList();
    }

    private Optional<Schedule> getLatestSchedule(long seasonId) {
        Schedule scheduleQuery = new Schedule();
        scheduleQuery.setSeasonId(seasonId);
        return scheduleService.getquery(scheduleQuery).stream()
                .max(Comparator.comparing(
                                Schedule::getMatchDate,
                                Comparator.nullsFirst(Comparator.naturalOrder()))
                        .thenComparingLong(Schedule::getWeekId));
    }

    private HashMap<String, String> toPlayerImprovementResult(PlayerImprovement improvement) {
        PlayerStats currentStats = improvement.stats();
        HashMap<String, String> playerResult = new HashMap<>();
        playerResult.put("UniversalId", String.valueOf(currentStats.getPlayer().getUniversalId()));
        playerResult.put("PlayerName", currentStats.getPlayer().getPlayerName());
        playerResult.put("TeamName", currentStats.getTeam().getTeamName());
        playerResult.put("PreviousOverallWinPercentage", String.valueOf(improvement.previousWinPercentage()));
        playerResult.put("CurrentOverallWinPercentage", String.valueOf(improvement.currentWinPercentage()));
        playerResult.put("Improvement", String.valueOf(improvement.difference()));
        return playerResult;
    }

    private record PlayerImprovement(PlayerStats stats, double previousWinPercentage,
                                     double currentWinPercentage, double difference) {}

    private boolean isEligiblePlayerWithUniversalId(PlayerStats stats) {
        return stats != null
                && stats.getPlayer() != null
                && stats.getPlayer().getUniversalId() != null
                && stats.getPlayer().isPlayoffEligible();
    }

    private Optional<List<Schedule>> getSchedules(StandingsQuery query){
        Schedule scheduleQuery = new Schedule();
        scheduleQuery.setSeasonId(query.getSeasonId());
        scheduleQuery.setWeekId(query.getWeekId());
        return Optional.ofNullable(scheduleService.getCulmativeWeeks(scheduleQuery));
    }

    private Optional<List<Match>> getMatchesBySchedule(StandingsQuery query, long scheduleId){
        Match matchQuery = new Match();
        matchQuery.setSeasonId(query.getSeasonId());
        matchQuery.setScheduleId(scheduleId);
        return Optional.ofNullable(matchService.getquery(matchQuery));
    }

    private Optional<List<Game>> getGamesByPlayer(long playerId){
        return Optional.ofNullable(gameService.getGamesByPlayer(playerId));
    }

    private Optional<List<Game>> getGames(StandingsQuery query, Match match){
        Game gameQuery = new Game();
        gameQuery.setMatch(match);
        return Optional.ofNullable(gameService.getquery(gameQuery));
    }

    private TeamStandings initializeTeamStandings(Match match, boolean isHome){
        TeamStandings teamStandings = new TeamStandings();
        teamStandings.setTeam((isHome)?match.getHomeTeam():match.getAwayTeam());
        teamStandings.setScore(0);
        teamStandings.setTotalGamesPlayed(0);
        return teamStandings;
    }

    private PlayerStats initializePlayerStats(Match match, Game game, boolean isHomeTeam, boolean isDoubles){
        PlayerStats playerStats = new PlayerStats();
        playerStats.setTeam((isHomeTeam)?match.getHomeTeam():match.getAwayTeam());
        playerStats.setPlayer((isHomeTeam)
                ?(!isDoubles)?game.getHomePlayer():game.getHomePlayerDBL()
                :(!isDoubles)?game.getAwayPlayer():game.getAwayPlayerDBL());
        return playerStats;
    }

    private PlayerStats updatePlayerStats(Game game, PlayerStats playerStats, boolean isHome){
        if(GameType.CRICKET.getId() == game.getGameType().getId()){
            if(game.isDoubles()){
                playerStats.setDoublesCricket(getGameStats(playerStats.getDoublesCricket(), game, isHome));
            } else {
                playerStats.setSinglesCricket(getGameStats(playerStats.getSinglesCricket(), game, isHome));
            }
        } else {
            if(!game.isDoubles()){
                playerStats.setSinglesX01(getGameStats(playerStats.getSinglesX01(), game, isHome));
            } else {
                playerStats.setDoublesX01(getGameStats(playerStats.getDoublesX01(), game, isHome));
            }
        }
        playerStats.setOverall(updateOverallStats(playerStats));
        return playerStats;
    }

    private GameStats getGameStats(GameStats gameStats, Game game, boolean isHome){
        if(gameStats == null) {new GameStats(game.getGameType());}
        gameStats.setNumWins(gameStats.getNumWins() + ((isHome)?game.getHomeScore():game.getAwayScore()));
        gameStats.setNumLosses(gameStats.getNumLosses() + ((isHome)?game.getAwayScore():game.getHomeScore()));
        int totalGames = gameStats.getNumWins()  + gameStats.getNumLosses();
        gameStats.setWinPercentage((double) gameStats.getNumWins() / totalGames);
        return gameStats;
    }

    private GameStats updateOverallStats(PlayerStats playerStats){
        int totalWins = playerStats.getSinglesCricket().getNumWins()
                + playerStats.getSinglesX01().getNumWins()
                + playerStats.getDoublesCricket().getNumWins()
                + playerStats.getDoublesX01().getNumWins();
        int totalLoses = playerStats.getSinglesCricket().getNumLosses()
                + playerStats.getSinglesX01().getNumLosses()
                + playerStats.getDoublesCricket().getNumLosses()
                + playerStats.getDoublesX01().getNumLosses();
        double overallWinPercentage = (double) totalWins / (totalWins + totalLoses);

        return new GameStats(null, totalWins, totalLoses, overallWinPercentage);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
