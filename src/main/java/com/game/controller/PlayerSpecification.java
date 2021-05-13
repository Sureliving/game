package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class PlayerSpecification implements Specification<Player> {

    private SearchCriteria criteria;

    public PlayerSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }


    @Override
    public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            if (root.get(criteria.getKey()).getJavaType() == (Date.class)) {
                Date date = new Date(Long.parseLong(criteria.getValue().toString()));
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), date);
            }
            else {
                return builder.greaterThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }

        }
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            if (root.get(criteria.getKey()).getJavaType() == (Date.class)) {
                Date date = new Date(Long.parseLong(criteria.getValue().toString()));
                return builder.lessThanOrEqualTo(root.get(criteria.getKey()), date);
            }
            else
                return builder.lessThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            }
            else if (root.get(criteria.getKey()).getJavaType() == Date.class) {
                Date date = new Date(Long.parseLong(criteria.getValue().toString()));
                return builder.equal(root.get(criteria.getKey()), date);
            }
            else if (root.get(criteria.getKey()).getJavaType() == Boolean.class) {
                Boolean bool = new Boolean(criteria.getValue().toString());
                return builder.equal(root.get(criteria.getKey()), bool);
            }
            else if (root.get(criteria.getKey()).getJavaType() == Profession.class) {
                Profession profession = Profession.valueOf(criteria.getValue().toString());
                return builder.equal(root.get(criteria.getKey()), profession);
            }
            else if (root.get(criteria.getKey()).getJavaType() == Race.class) {
                Race race = Race.valueOf(criteria.getValue().toString());
                return builder.equal(root.get(criteria.getKey()), race);
            }
        }
        return null;
    }



}
