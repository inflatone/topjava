package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepositoryMapImpl;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final MealService service = new MealServiceImpl(new MealRepositoryMapImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            log.info("getAll");
            req.setAttribute("meals", MealsUtil.getWithExceeded(service.getAll(), 2000));
            req.getRequestDispatcher("meals.jsp").forward(req, resp);
            return;
        }
        switch (action) {
            case "remove":
                int id = getId(req);
                log.info("remove {}", id);
                service.delete(id);
                resp.sendRedirect("meals");
                break;
            case "add":
                log.info("add");
                req.setAttribute("date", LocalDateTime.now());
                req.getRequestDispatcher("add.jsp").forward(req, resp);
                break;
            case "edit":
                int id1 = getId(req);
                log.info("remove {}", id1);
                req.setAttribute("meal", service.get(id1));
                req.getRequestDispatcher("edit.jsp").forward(req, resp);
                break;
            default:
                throw new IllegalArgumentException(String.format("Action %s is illegal", action));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        LocalDateTime date = LocalDateTime.parse(req.getParameter("date"));
        String desc = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        if (id == null) {
            service.create(new Meal(date, desc, calories));
        } else {
            service.update(new Meal(Integer.parseInt(id), date, desc, calories));
        }
        resp.sendRedirect("meals");
    }

    private int getId(HttpServletRequest req) {
        return Integer.parseInt(req.getParameter("id"));
    }
}
