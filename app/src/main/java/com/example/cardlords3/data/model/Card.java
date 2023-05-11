package com.example.cardlords3.data.model;

import java.util.List;

public class Card {
    private int cardID;
    private String name;
    private int typeID;
    private List<Integer> raceID;
    private int health;
    private int attack;
    private List<Integer> skillID;
    private String image;
    private int cost;
    private int rarity;

    // Default constructor required for calls to DataSnapshot.getValue(Card.class)
    public Card() {}

    public Card(int cardID, String name, int typeID, List<Integer> raceID, int health, int attack, List<Integer> skillID, String image, int cost, int rarity) {
        this.cardID = cardID;
        this.name = name;
        this.typeID = typeID;
        this.raceID = raceID;
        this.health = health;
        this.attack = attack;
        this.skillID = skillID;
        this.image = image;
        this.cost = cost;
        this.rarity = rarity;
    }

    // Getters and setters
    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public List<Integer> getRaceID() {
        return raceID;
    }

    public void setRaceID(List<Integer> raceID) {
        this.raceID = raceID;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public List<Integer> getSkillID() {
        return skillID;
    }

    public void setSkillID(List<Integer> skillID) {
        this.skillID = skillID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }
}
