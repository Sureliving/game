package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService{

    private PlayerRepository repository;

    @Autowired
    public void setPlayerRepository(PlayerRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsById(Long aLong) {
        return repository.existsById(aLong);
    }

    @Override
    public Long size() {
        return repository.count();
    }

    @Override
    public Page<Player> findAll(Specification<Player> spec, Pageable pageable) { return repository.findAll(spec, pageable);}

    @Override
    public List<Player> findAll(Specification<Player> spec) {
        return repository.findAll(spec);
    }

    @Override
    public Player getPlayerById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void savePlayer(Player player) {
        repository.save(player);
    }

    @Override
    public void updatePlayer(Player player) {
        repository.save(player);
    }

    @Override
    public void deletePlayerById(Long id) {
        repository.deleteById(id);
    }
}
