package com.opdl.api.stats.service;

import com.opdl.api.stats.comparator.HighInOutReportComparator;
import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.HighInOut;
import com.opdl.api.stats.model.HighInOutReport;
import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.repository.HighInOutRepository;
import com.opdl.api.stats.specification.HighInOutSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;

import static java.util.stream.Collectors.*;

@Service
public class HighInOutService {
    @Autowired
    private HighInOutRepository highInOutRepository;

    @Autowired
    private HighInOutReportComparator highInOutReportComparator;

    public List<HighInOut> getPlayerWithHighInOut(long playerId) throws ResourceNotFoundException {
        List<HighInOut> results = new ArrayList<>();
        Specification<HighInOut> spec = HighInOutSpecifications.hasPlayerWithHighOrOut(playerId);
        results = highInOutRepository.findAll(spec);
        return results;
    }

    public List<HighInOut> getPlayerWithHighIns(long seasonId) throws ResourceNotFoundException {
        List<HighInOut> results = new ArrayList<>();
        Specification<HighInOut> spec = HighInOutSpecifications.getPlayersWithHighInForSeason(seasonId);
        results = highInOutRepository.findAll(spec);
        return results;
    }

    public List<HighInOut> getPlayerWithHighOuts(long seasonId) throws ResourceNotFoundException {
        List<HighInOut> results = new ArrayList<>();
        Specification<HighInOut> spec = HighInOutSpecifications.getPlayersWithHighOutForSeason(seasonId);
        results = highInOutRepository.findAll(spec);
        return results;
    }

    public List<HighInOutReport> reportPlayersWithHighIns(long seasonId) throws ResourceNotFoundException {
        return generateHighInOutReport(true, seasonId);
    }

    public List<HighInOutReport> reportPlayersWithHighOuts(long seasonId) throws ResourceNotFoundException {
        return generateHighInOutReport(false, seasonId);
    }

    private List<HighInOutReport> generateHighInOutReport (boolean isHighIn, long seasonId) {
        List<HighInOutReport> results = new ArrayList<>();
        List<HighInOut> data = isHighIn?getPlayerWithHighIns(seasonId):getPlayerWithHighOuts(seasonId);
        Map<Player, Long> byPlayer = data.stream()
                .collect(groupingBy(HighInOut::getPlayer, counting()));
        byPlayer.forEach((player, count) -> {
            HighInOutReport report = new HighInOutReport();
            report.setPlayer(player);
            if(isHighIn){
                report.setNumHighIns(Math.toIntExact(count));
                report.setHighestIn(data.stream().filter(record -> record.getPlayer() == player).max(Comparator.comparing(HighInOut::getHighIn)).orElse(new HighInOut()).getHighIn());
            }
            if(!isHighIn){
                report.setNumHighOuts(Math.toIntExact(count));
                report.setHighestOut(data.stream().filter(record -> record.getPlayer() == player).max(Comparator.comparing(HighInOut::getHighOut)).orElse(new HighInOut()).getHighOut());
            }
            results.add(report);
        });
        if (isHighIn) {
            results.sort(highInOutReportComparator::compareHighIns);
        } else {
            results.sort(highInOutReportComparator::compareHighOuts);
        }

        return results;
    }


}
