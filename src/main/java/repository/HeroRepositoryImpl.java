package repository;

import model.Hero;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HeroRepositoryImpl implements HeroRepository {

    private Connection connection;

    public HeroRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    public boolean save(Hero hero) throws SQLException {
        PreparedStatement statement = null;
        if (hero.isNew()) {
            statement = connection.prepareStatement("INSERT INTO hero (name, universe, power, description, alive) VALUES (?, ?, ?, ?, ?)");
        } else {
            statement = connection.prepareStatement("UPDATE hero SET " +
                    "name=?, universe=?, power=?, description=?, alive=? WHERE id=?");
            statement.setInt(6, hero.getId());
        }
        statement.setString(1, hero.getName());
        statement.setString(2, hero.getUniverse());
        statement.setInt(3, hero.getPower());
        statement.setString(4, hero.getDescription());
        statement.setBoolean(5, hero.isAlive());
        statement.close();
        return statement.execute();
    }

    public boolean delete(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM hero WHERE id=?");
        statement.setInt(1, id);
        statement.close();
        boolean execute = statement.execute();
        return execute;
    }

    public Hero get(int id) throws SQLException {
        Hero hero = null;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM hero WHERE id=?");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            hero = getHeroFromResultSet(resultSet);
        }
        statement.close();
        return hero;
    }

    public Hero getByName(String name) throws SQLException {
        Hero hero = null;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM hero WHERE lower(name)=?");
        statement.setString(1, name.toLowerCase());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            hero = getHeroFromResultSet(resultSet);
        }
        statement.close();
        return hero;
    }

    public List<Hero> getAll() throws SQLException {
        List<Hero> list = new ArrayList<Hero>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hero ORDER BY power, name");
        while (resultSet.next()) {
            list.add(getHeroFromResultSet(resultSet));
        }
        statement.close();
        return list;
    }

    private Hero getHeroFromResultSet(ResultSet resultSet) throws SQLException {
        Hero hero = new Hero();
        hero.setId(resultSet.getInt(1));
        hero.setName(resultSet.getString(2));
        hero.setUniverse(resultSet.getString(3));
        hero.setPower(resultSet.getInt(4));
        hero.setDescription(resultSet.getString(5));
        hero.setAlive(resultSet.getBoolean(6));
        return hero;
    }
}
