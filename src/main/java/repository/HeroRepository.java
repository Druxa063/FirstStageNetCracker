package repository;

import model.Hero;

import java.sql.SQLException;
import java.util.List;

public interface HeroRepository {

    boolean save(Hero hero) throws SQLException;

    boolean delete(int id) throws SQLException;

    Hero get(int id) throws SQLException;

    List<Hero> getByName(String name) throws SQLException;

    List<Hero> getByNameSortNameOrPower(String name, String sort) throws SQLException;

    List<Hero> getAll() throws SQLException;
}
