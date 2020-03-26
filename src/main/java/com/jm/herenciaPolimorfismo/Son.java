package com.jm.herenciaPolimorfismo;

public class Son extends Father {

    private String sing;
    private String walk;
    private String talk;
    private int eatForDay;
    private int sleepHours;
    private String gender;
    private String skin;
    private String eyes;
    private String lie;
    private String play;

    public Son(String sing, String walk, String talk, int eatForDay, int sleepHours, String gender, String skin, String eyes, String lie, String play) {
        this.sing = sing;
        this.walk = walk;
        this.talk = talk;
        this.eatForDay = eatForDay;
        this.sleepHours = sleepHours;
        this.gender = gender;
        this.skin = skin;
        this.eyes = eyes;
        this.lie = lie;
        this.play = play;
    }

    public String getSing() {
        return sing;
    }

    public void setSing(String sing) {
        this.sing = sing;
    }

    public String getWalk() {
        return walk;
    }

    public void setWalk(String walk) {
        this.walk = walk;
    }

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }

    public int getEatForDay() {
        return eatForDay;
    }

    public void setEatForDay(int eatForDay) {
        this.eatForDay = eatForDay;
    }

    public int getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(int sleepHours) {
        this.sleepHours = sleepHours;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getLie() {
        return lie;
    }

    public void setLie(String lie) {
        this.lie = lie;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    @Override
    public void description() {
        super.description();
    }

    @Override
    public void sing() {
        System.out.println(getSing());
    }

    @Override
    public void walk() {
        System.out.println(getWalk());
    }

    @Override
    public void talk() {
        System.out.println(getTalk());
    }

    @Override
    public void eat() {
        System.out.println("Como " + getEatForDay() + " veces al dia");
    }

    @Override
    public void sleep() {
        System.out.println("Duermo " + getSleepHours() + " horas");
    }

    @Override
    public void gender() {
        System.out.println("Soy " + getGender());
    }

    @Override
    public void skin() {
        System.out.println("Soy de piel " + getSkin());
    }

    @Override
    public void eyes() {
        System.out.println("Mis ojos son color " + getEyes());
    }

    @Override
    public void play() {
        System.out.println(getPlay());
    }

    @Override
    public void lie() {
        System.out.println(getLie());
    }

}
