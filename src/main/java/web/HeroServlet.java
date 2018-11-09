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

    private Connection connection;
    private HeroRepository repository;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Properties properties = new Properties();
            URL location = HeroServlet.class.getProtectionDomain().getCodeSource().getLocation();
            properties.load(new FileInputStream(location.getFile() + "postgres.properties"));
            String driver = properties.getProperty("database.driver");
            String url = properties.getProperty("database.url");
            String user = properties.getProperty("database.username");
            String password = properties.getProperty("database.password");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            repository = new HeroRepositoryImpl(connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action == null ? "all" : action) {
                case "find" :
                    request.setAttribute("heroes", Arrays.asList(repository.getByName(request.getParameter("nameHero"))));
                    log.info("Hero successfully find");
                    request.getRequestDispatcher("/listHero.jsp").forward(request, response);
                    break;
                case "delete":
                    int id = getId(request);
                    repository.delete(id);
                    log.info("Hero successfully delete");
                    response.sendRedirect("heroes");
                    break;
                case "create":
                case "update":
                    Hero hero = action.equals("create") ?
                            new Hero() : repository.get(getId(request));
                    request.setAttribute("hero", hero);
                    log.info("forward to heroForm.jsp ");
                    request.getRequestDispatcher("/heroForm.jsp").forward(request, response);
                    break;
                case "all":
                default:
                    request.setAttribute("heroes", repository.getAll());
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
            if (repository.getByName(name) != null) {
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
            response.sendRedirect("/heroes");
        } catch (ValidationException e) {
            log.debug("Exception validation : ", e);
            request.setAttribute("message", e.getMessage());
            request.getRequestDispatcher("/validation.jsp").forward(request, response);
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
