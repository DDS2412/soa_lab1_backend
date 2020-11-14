package soa.space_marines.servlets;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import soa.space_marines.dto.FilteringObjectDto;
import soa.space_marines.models.SpaceMarine;
import soa.space_marines.services.FilteringSpaceMarineService;
import soa.space_marines.services.SpaceMarineService;
import soa.space_marines.utils.Converter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/space/marine/health/*")
public class SpaceMarineHealthServlet extends HttpServlet {
    private final SpaceMarineService spaceMarineService;
    private final FilteringSpaceMarineService filteringSpaceMarineService;

    public SpaceMarineHealthServlet(){
        this.spaceMarineService = new SpaceMarineService();
        this.filteringSpaceMarineService = new FilteringSpaceMarineService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);

        if(req.getPathInfo() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] pathParams = req.getPathInfo().split("/");

        String[] space_marines = req.getParameterValues("space_marine");
        if (space_marines == null) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        List<Integer> spaceMarinesId = Arrays
                .stream(space_marines)
                .map(Converter::intConvert)
                .collect(Collectors.toList());

        if (!pathParams[1].equals("mean")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        float meanValue = this.spaceMarineService.calculateHealthMeanValue(spaceMarinesId);
        PrintWriter writer = resp.getWriter();

        writer.println(meanValue);
        writer.flush();

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);

        if(req.getPathInfo() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] pathParams = req.getPathInfo().split("/");

        if(pathParams.length != 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Long health = Converter.longConvert(pathParams[1]);

        if (health == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        this.spaceMarineService.deleteSpaceMarineByHealth(health);

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, DELETE");
    }
}
