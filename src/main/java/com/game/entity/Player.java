package com.game.entity;


import javax.persistence.*;
import java.util.Calendar;
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

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        banned = (banned != null && (banned));
        this.banned = banned;
    }

    public void setLevel(Integer experience) {
        this.level = (int)((Math.sqrt(2500 + (200 * experience))) - 50) / 100;
    }

    public void setUntilNextLevel(Integer experience) {
        int level = (int)((Math.sqrt(2500 + (200 * experience))) - 50) / 100;
        this.untilNextLevel = 50 * (level + 1) * (level + 2) - experience;
    }

    public static boolean idIsOk(Long id) {
        return id > 0;
    }

    public static boolean nameIsOk(String name) {
        return name.length() <= 12;
    }

    public static boolean titleIsOk(String title) {
        return title.length() <= 30 ;
    }

    public static boolean raceIsOk(Race race) {
        return race != null;
    }

    public static boolean professionIsOk(Profession profession) {
        return profession != null;
    }

    public static boolean birthdayIsOk(Long birthday) {
        if (birthday == null || birthday < 0) return false;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(birthday);
        int mYear = c.get(Calendar.YEAR);
        return mYear >= 2000 && mYear <= 3000;
    }

    public static boolean experienceIsOk(Integer experience) {
        return experience >= 0 && experience <= 10000000 ;
    }
}
