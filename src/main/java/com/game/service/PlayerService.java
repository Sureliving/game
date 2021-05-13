package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PlayerService {

    boolean existsById(Long aLong);

    Long size();

    List<Player> findAll(Specification<Player> spec);

    Page<Player> findAll(Specification<Player> spec, Pageable pageable);

    Player getPlayerById(Long id);

    void savePlayer(Player player);

    void updatePlayer(Player player);

    void deletePlayerById(Long id);

}

