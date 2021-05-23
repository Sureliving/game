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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    //getPlayers
    @GetMapping(value = "/players")
    @ResponseBody
    public ResponseEntity<List<Player>> getPlayers(@RequestParam(value = "name", required = false) String name,
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
    @GetMapping(value = "/players/count")
    @ResponseBody
    public ResponseEntity<Long> getCount(@RequestParam(value = "name", required = false) String name,
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
            return new ResponseEntity<>((long) players.size(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(playerService.size(), HttpStatus.OK);
        }

    }

    //Create
    @PostMapping(value = "/players")
    @ResponseBody
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        try {
            if (playerService.isPlayerValid(player)) {
                player.setBanned(player.getBanned());
                player.setLevel(player.getExperience());
                player.setUntilNextLevel(player.getExperience());

                playerService.savePlayer(player);

                return new ResponseEntity<>(player, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable(value = "id") String pathId) {
        final Long id = convertIdToLong(pathId);
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!playerService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(playerService.getPlayerById(id), HttpStatus.OK);
        }
    }

    //Update
    @PostMapping(value = "/players/{id}")
    @ResponseBody
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player,
                                               @PathVariable(value = "id") Long id) {

        //final Long id = convertIdToLong(pathId);
        if (id == 0 || !Player.idIsOk(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!playerService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Player savedPlayer = playerService.getPlayerById(id);

            String playerName = player.getName();
            String playerTitle = player.getTitle();
            Race playerRace = player.getRace();
            Profession playerProfession = player.getProfession();
            Long playerBirthday = player.getBirthday();
            Boolean playerIsBanned = player.getBanned();
            Integer playerExperience = player.getExperience();
            if(     playerName != null ||
                    playerTitle!= null ||
                    playerRace != null ||
                    playerProfession != null ||
                    playerBirthday != null ||
                    playerIsBanned != null ||
                    playerExperience != null) {
                if (playerName != null && Player.nameIsOk(playerName)) savedPlayer.setName(playerName);
                if (playerTitle!= null && Player.titleIsOk(playerTitle)) savedPlayer.setTitle(playerTitle);
                if (Player.raceIsOk(playerRace)) savedPlayer.setRace(playerRace);
                if (Player.professionIsOk(playerProfession)) savedPlayer.setProfession(playerProfession);
                if (Player.birthdayIsOk(playerBirthday)) {
                    savedPlayer.setBirthday(playerBirthday);
                } else if (playerBirthday != null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                if (playerIsBanned != null) savedPlayer.setBanned(playerIsBanned);
                if (playerExperience != null && Player.experienceIsOk(playerExperience)) {
                    savedPlayer.setExperience(playerExperience);
                    savedPlayer.setLevel(playerExperience);
                    savedPlayer.setUntilNextLevel(playerExperience);
                } else if (playerExperience != null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                playerService.updatePlayer(savedPlayer);
            }
            return new ResponseEntity<>(savedPlayer, HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/players/{id}")
    public ResponseEntity<Player> deleteShip(@PathVariable(value = "id") Long id) {
        //final Long id = convertIdToLong(pathId);
        if (id == 0 || !Player.idIsOk(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!playerService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            playerService.deletePlayerById(id);
            return new ResponseEntity<>(HttpStatus.OK);
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
            builder.with("banned", ":", banned);
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


    private Long convertIdToLong(String pathId) {
        if (pathId == null || "0".equals(pathId)) {
            return null;
        } else {
            try {
                return Long.parseLong(pathId);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}
