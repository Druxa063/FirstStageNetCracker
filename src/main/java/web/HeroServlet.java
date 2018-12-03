package web;

import model.Hero;
import org.apache.log4j.Logger;
import repository.HeroRepositoryImpl;
import service.HeroService;
import service.HeroServiceImpl;
import util.ValidationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class HeroServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(HeroServlet.class);

    private HeroService service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = new HeroServiceImpl(new HeroRepositoryImpl());
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            switch (action == null ? "all" : action) {
                case "find" :
                    StringBuilder like = new StringBuilder(request.getParameter("nameHero"));
                    boolean matches = Boolean.parseBoolean(request.getParameter("matches"));
                    if (!matches) {
                        like.insert(0, "%");
                        like.append("%");
                    }
                    List<Hero> listName = service.getByName(like.toString());
                    String json = listName.toString();
                    response.getWriter().write(json);
                    if (listName.size() > 0) {
                        log.info("Hero successfully found");
                    } else {
                        log.info("Hero not found");
                    }
                    break;
                case "sort" :
                    String sortParam = request.getParameter("sortParam");
                    StringBuilder nameHero = new StringBuilder(request.getParameter("nameHero"));
                    nameHero.insert(0, "%");
                    nameHero.append("%");
                    String sortJson = service.getByNameSortNameOrPower(nameHero.toString(), sortParam).toString();
                    response.getWriter().write(sortJson);
                    log.info("Hero successfully sorted by " + sortParam);
                    break;
                case "delete":
                    int id = getId(request);
                    service.delete(id);
                    log.info("Hero successfully deleted");
                    break;
                case "update":
                    Hero hero = service.get(getId(request));
                    response.getWriter().write(hero.toString());
                    log.info("forward to saveForm");
                    break;
                case "all":
                default:
                    log.info("forward to heroes");
                    String allJSON = service.getAll().toString();
                    response.getWriter().write(allJSON);
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
            String phone = request.getParameter("phone");
            String logo = request.getParameter("logo");
            Hero hero = new Hero(
                    id.isEmpty() ? null : getId(request),
                    name,
                    universe,
                    power,
                    description,
                    alive,
                    phone,
                    logo
            );
            service.validation(hero);
            service.save(hero);
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
