package web;

import model.Hero;
import org.apache.log4j.Logger;
import repository.HeroRepository;
import repository.HeroRepositoryImpl;
import util.ValidationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class HeroServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(HeroServlet.class);

    private HeroRepository repository;

    @Override
    public void init() throws ServletException {
        super.init();
        repository = new HeroRepositoryImpl();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action == null ? "all" : action) {
                case "find" :
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    StringBuilder like = new StringBuilder(request.getParameter("nameHero"));
                    boolean matches = Boolean.parseBoolean(request.getParameter("matches"));
                    if (!matches) {
                        like.insert(0, "%");
                        like.append("%");
                    }
                    String json = repository.getByName(like.toString()).toString();
                    response.getWriter().write(json);
                    log.info("Hero successfully found");
                    break;
                case "sort" :
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    String sortParam = request.getParameter("sortParam");
                    StringBuilder nameHero = new StringBuilder(request.getParameter("nameHero"));
                    nameHero.insert(0, "%");
                    nameHero.append("%");
                    String sortJson = repository.getByNameSortNameOrPower(nameHero.toString(), sortParam).toString();
                    response.getWriter().write(sortJson);
                    log.info("Hero successfully sorted by " + sortParam);
                    break;
                case "delete":
                    int id = getId(request);
                    repository.delete(id);
                    log.info("Hero successfully deleted");
                    break;
                case "update":
                    Hero hero = repository.get(getId(request));
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(hero.toString());
                    log.info("forward to saveForm");
                    break;
                case "ajax" :
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(repository.getAll().toString());
                    log.info("getAll through ajax");
                    break;
                case "all":
                default:
                    log.info("forward to heroes");
                    request.getRequestDispatcher("/listHero.jsp").forward(request, response);
                    break;
            }
        } catch (SQLException e) {
            log.debug("SQLException in doGet : ", e);
            e.printStackTrace();
        } catch (Exception e) {
            log.debug("Exception in doGet : ", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String universe = request.getParameter("universe");
            int power = Integer.parseInt(request.getParameter("power"));
            String description = request.getParameter("description");
            boolean alive = Boolean.parseBoolean(request.getParameter("alive"));
            if (name.length() > 30) {
                throw new ValidationException("The name should not be more than 30 characters");
            }
            if (id.isEmpty() & !repository.getByName(name).isEmpty()) { //проверка при создании героя на дубликат
                throw new ValidationException("Hero with the same name already exists");
            }
            if(!id.isEmpty() && !name.equalsIgnoreCase(repository.get(getId(request)).getName())) { //проверка при изменении имени на другое
                throw new ValidationException("Hero with the same name already exists");
            }
            if (power < 0 || power > 100) {
                throw new ValidationException("The power should not be less than 0 and greater than 100");
            }
            Hero hero = new Hero(
                    id.isEmpty() ? null : getId(request),
                    name,
                    universe,
                    power,
                    description,
                    alive
            );
            repository.save(hero);
            log.info("Hero successfully create/update");
        } catch (ValidationException e) {
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
            log.debug("Exception validation : ", e);
        } catch (SQLException e) {
            log.debug("SQLException in doPost : ", e);
            e.printStackTrace();
        } catch (Exception e) {
            log.debug("Exception in doPost : ", e);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
