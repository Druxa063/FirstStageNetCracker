package model;

public class Hero {

    private Integer id;
    private String name;
    private String universe;
    private int power;
    private String description;
    private boolean alive;

    public Hero() {
    }

    public Hero(String name, String universe, int power, String description, boolean alive) {
        this(null, name, universe, power, description, alive);
    }

    public Hero(Integer id, String name, String universe, int power, String description, boolean alive) {
        this.id = id;
        this.name = name;
        this.universe = universe;
        this.power = power;
        this.description = description;
        this.alive = alive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniverse() {
        return universe;
    }

    public void setUniverse(String universe) {
        this.universe = universe;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" +id + "\", " +
                "\"name\":\""  + name + "\", " +
                "\"universe\":\"" + universe + "\", " +
                "\"power\":" + power + ", "+
                "\"description\":\"" + description + "\", " +
                "\"alive\":" + alive +
                "}";
    }


}
