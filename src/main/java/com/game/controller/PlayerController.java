package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    //getPlayers
    @RequestMapping(value = "/rest/players", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Player>> getShips(@RequestParam(value = "name", required = false) String name,
                                                 @RequestParam(value = "title", required = false) String title,
                                                 @RequestParam(value = "race", required = false) Race race,
                                                 @RequestParam(value = "profession", required = false) Profession profession,
                                                 @RequestParam(value = "after", required = false) Long after,
                                                 @RequestParam(value = "before", required = false) Long before,
                                                 @RequestParam(value = "banned", required = false) Boolean banned,
                                                 @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                 @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                 @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                 @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                 @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
                                                 @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        Specification<Player> spec = getPlayerSpecificationBuilder(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel).build();
        Page<Player> allPlayers = playerService.findAll(spec, PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName())));
        return new ResponseEntity<>(allPlayers.getContent(), HttpStatus.OK);

    }
    //getCount
    @RequestMapping(value = "/rest/players/count", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Long> getCount(   @RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "title", required = false) String title,
                                            @RequestParam(value = "race", required = false) Race race,
                                            @RequestParam(value = "profession", required = false) Profession profession,
                                            @RequestParam(value = "after", required = false) Long after,
                                            @RequestParam(value = "before", required = false) Long before,
                                            @RequestParam(value = "banned", required = false) Boolean banned,
                                            @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                            @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                            @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        Specification<Player> spec = getPlayerSpecificationBuilder(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel).build();
        if (spec != null) {
            List<Player> players = playerService.findAll(spec);
            return new ResponseEntity<>(Long.valueOf(players.size()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(playerService.size(), HttpStatus.OK);
        }

    }

    //Create
    @RequestMapping(value = "/rest/players", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        try {
            if (player.getBirthday() != null) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(player.getBirthday());
                int mYear = c.get(Calendar.YEAR);

                if (    player.getName() == null            ||
                        player.getTitle() == null           ||
                        player.getRace() == null            ||
                        player.getProfession() == null      ||
                        player.getExperience() == null      ||
                        player.getName().length() > 12      ||
                        player.getTitle().length() > 30     ||
                        "".equals(player.getName())         ||
                        player.getExperience() < 0          ||
                        player.getExperience() > 10000000   ||
                        player.getBirthday() < 0            ||
                        mYear < 2000                        ||
                        mYear > 3000                            )
                {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                } else {
                    player.setBanned(player.getBanned() != null && (player.getBanned()));
                    Integer level = (int)((Math.sqrt(2500 + (200 * player.getExperience()))) - 50) / 100;
                    player.setLevel(level);
                    Integer untilNextLevel = 50 * (level + 1) * (level + 2) - player.getExperience();
                    player.setUntilNextLevel(level);
                    playerService.savePlayer(player);
                    return new ResponseEntity<>(player, HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    private PlayerSpecificationsBuilder getPlayerSpecificationBuilder(  String name,
                                                                        String title,
                                                                        Race race,
                                                                        Profession profession,
                                                                        Long after,
                                                                        Long before,
                                                                        Boolean banned,
                                                                        Integer minExperience,
                                                                        Integer maxExperience,
                                                                        Integer minLevel,
                                                                        Integer maxLevel) {
        PlayerSpecificationsBuilder builder = new PlayerSpecificationsBuilder();
        if (name != null) {
            builder.with("name", ":", name);
        }
        if (title != null) {
            builder.with("title", ":", title);
        }
        if (race != null) {
            builder.with("race", ":", race);
        }
        if (profession != null) {
            builder.with("profession", ":", profession);
        }
        if (after != null) {
            builder.with("birthday", ">", after);
        }
        if (before != null) {
            builder.with("birthday", "<", before);
        }
        if (banned != null) {
            builder.with("isUsed", ":", banned);
        }
        if (minExperience != null) {
            builder.with("experience", ">", minExperience);
        }
        if (maxExperience != null) {
            builder.with("experience", "<", maxExperience);
        }
        if (minLevel != null) {
            builder.with("level", ">", minLevel);
        }
        if (maxLevel != null) {
            builder.with("level", "<", maxLevel);
        }
        return builder;
    }

}
