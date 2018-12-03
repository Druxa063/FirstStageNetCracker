package repository;

import model.Hero;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HeroRepositoryImpl implements HeroRepository {

    public HeroRepositoryImpl() {
    }

    public boolean save(Hero hero) throws SQLException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = null;
        if (hero.isNew()) {
            statement = connection.prepareStatement("INSERT INTO hero (name, universe, power, description, alive, phone, logo) VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(7, hero.getLogo());
        } else {
            if (hero.getLogo() == null) {
                statement = connection.prepareStatement("UPDATE hero SET " +
                        "name=?, universe=?, power=?, description=?, alive=?, phone=? WHERE id=?");
                statement.setInt(7, hero.getId());
            } else {
                statement = connection.prepareStatement("UPDATE hero SET " +
                        "name=?, universe=?, power=?, description=?, alive=?, phone=?, logo=? WHERE id=?");
                statement.setString(7, hero.getLogo());
                statement.setInt(8, hero.getId());
            }
        }
        statement.setString(1, hero.getName());
        statement.setString(2, hero.getUniverse());
        statement.setInt(3, hero.getPower());
        statement.setString(4, hero.getDescription());
        statement.setBoolean(5, hero.isAlive());
        statement.setString(6, hero.getPhone());
        boolean execute = statement.execute();
        statement.close();
        connection.close();
        return execute;
    }

    public boolean delete(int id) throws SQLException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM hero WHERE id=?");
        statement.setInt(1, id);
        boolean execute = statement.execute();
        statement.close();
        connection.close();
        return execute;
    }

    public Hero get(int id) throws SQLException {
        Hero hero = null;
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM hero WHERE id=?");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            hero = getHeroFromResultSet(resultSet);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return hero;
    }

    public List<Hero> getByName(String name) throws SQLException {
        List<Hero> list = new ArrayList<Hero>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM hero WHERE lower(name) LIKE ?");
        statement.setString(1, name.toLowerCase());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            list.add(getHeroFromResultSet(resultSet));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return list;
    }

    public List<Hero> getByNameSortNameOrPower(String name, String sort) throws SQLException {
        List<Hero> list = new ArrayList<Hero>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM hero WHERE lower(name) LIKE ? ORDER BY " + sort);
        statement.setString(1, name.toLowerCase());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            list.add(getHeroFromResultSet(resultSet));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return list;
    }

    public List<Hero> getAll() throws SQLException {
        List<Hero> list = new ArrayList<Hero>();
        Connection connection = DBUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hero");
        while (resultSet.next()) {
            list.add(getHeroFromResultSet(resultSet));
        }
        resultSet.close();
        statement.close();
        connection.close();
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
        hero.setPhone(resultSet.getString(7));
        hero.setLogo(resultSet.getString(8));
        return hero;
    }
}
