package web;

import model.Hero;
import repository.HeroRepository;
import repository.HeroRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class HeroServlet extends HttpServlet {

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
                    request.getRequestDispatcher("/listHero.jsp").forward(request, response);
                    break;
                case "delete":
                    int id = getId(request);
                    repository.delete(id);
                    response.sendRedirect("heroes");
                    break;
                case "create":
                case "update":
                    Hero hero = action.equals("create") ?
                            new Hero() : repository.get(getId(request));
                    request.setAttribute("hero", hero);
                    request.getRequestDispatcher("/heroForm.jsp").forward(request, response);
                    break;
                case "all":
                default:
                    request.setAttribute("heroes", repository.getAll());
                    request.getRequestDispatcher("/listHero.jsp").forward(request, response);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Hero hero = new Hero(
                id.isEmpty() ? null : getId(request),
                request.getParameter("name"),
                request.getParameter("universe"),
                Integer.parseInt(request.getParameter("power")),
                request.getParameter("description"),
                Boolean.parseBoolean(request.getParameter("alive"))
        );
        try {
            repository.save(hero);
            response.sendRedirect("/heroes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
