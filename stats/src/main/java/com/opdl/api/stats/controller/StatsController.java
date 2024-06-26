package com.opdl.api.stats.controller;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.*;
import com.opdl.api.stats.service.GameService;
import com.opdl.api.stats.service.MatchService;
import com.opdl.api.stats.service.PlayerService;
import com.opdl.api.stats.service.ScheduleService;
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
                            } else {
                                System.out.println("No GAMES found");
                            }
                            AtomicBoolean foundHomeTeam = new AtomicBoolean(false);
                            AtomicBoolean foundAwayTeam = new AtomicBoolean(false);
                            results.stream().forEach(result -> {
                                if(result.getTeam().getId() == homeTeamEntry.getTeam().getId()){
                                    result.setScore(result.getScore() + homeTeamEntry.getScore());
                                    result.setTotalGamesPlayed(result.getTotalGamesPlayed() + homeTeamEntry.getTotalGamesPlayed());
                                    foundHomeTeam.set(true);
                                }
                                else if(result.getTeam().getId() == awayTeamEntry.getTeam().getId()){
                                    result.setScore(result.getScore() + awayTeamEntry.getScore());
                                    result.setTotalGamesPlayed(result.getTotalGamesPlayed() + awayTeamEntry.getTotalGamesPlayed());
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
            Collections.sort(results);
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
        Collections.sort(finalResults);
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
        //ArrayList<PlayerStats> finalResults = new ArrayList<PlayerStats>();
        Collections.sort(results);
        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @GetMapping("/stats/players/eligible")
    public ResponseEntity<HashMap<Player, Integer>> getPlayerEligibilty() {
        HashMap<Player, Integer> playerEligibility = new HashMap<>();
        long currentSeason = 2;
        Player playerQuery = new Player();
        playerQuery.setSeasonId(currentSeason);
        ArrayList<Player> players = (ArrayList<Player>) playerService.getquery(playerQuery);
        players.forEach(player -> {
            int gamesPlayed = gameService.getGamesByPlayer(player.getId())
                    .stream()
                    .filter(distinctByKey(Game::getMatch))
                    .map(Game::getMatch)
                    .toList().size();
            player.setPlayoffEligible(gamesPlayed > 8);
            playerEligibility.put(player, gamesPlayed);
        });

        return (!playerEligibility.isEmpty())? ResponseEntity.ok(playerEligibility): ResponseEntity.badRequest().build();
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
