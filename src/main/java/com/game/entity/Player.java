package com.game.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "title")
    public String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "race")
    public Race race;

    @Enumerated(EnumType.STRING)
    @Column(name = "profession")
    public Profession profession;

    @Column(name = "experience")
    public Integer experience;

    @Column(name = "level")
    public Integer level;

    @Column(name = "untilNextLevel")
    public Integer untilNextLevel;

    @Column(name = "birthday")
    public Date birthday;

    @Column(name = "banned")
    public Boolean banned;

    public Long getBirthday() {
        return birthday != null ? birthday.getTime() : null;
    }

    public void setBirthday(Long birthday) {
        this.birthday = new Date(birthday);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }
}
