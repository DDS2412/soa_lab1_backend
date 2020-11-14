package soa.space_marines.servlets;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import soa.space_marines.models.SpaceMarine;
import soa.space_marines.services.SpaceMarineService;
import soa.space_marines.utils.Converter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/space/marine/*")
public class SpaceMarineServlet extends HttpServlet {
    private final SpaceMarineService spaceMarineService;

    public SpaceMarineServlet(){
        this.spaceMarineService = new SpaceMarineService();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);

        String[] pathParams = req.getPathInfo().split("/");
        if (pathParams.length != 2) return;

        if (!pathParams[1].equals("create")) return;
        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();

        String currentLine = "";
        while ((currentLine = reader.readLine()) != null) { stringBuilder.append(currentLine); }

        XmlMapper xmlMapper = new XmlMapper();
        SpaceMarine newSpaceMarine = xmlMapper.readValue(stringBuilder.toString(), SpaceMarine.class);

        this.spaceMarineService.saveSpaceMarine(newSpaceMarine);

        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);

        String[] pathParams = req.getPathInfo().split("/");
        if (pathParams.length != 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (!pathParams[1].equals("update"))  {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();

        String currentLine = "";
        while ((currentLine = reader.readLine()) != null) { stringBuilder.append(currentLine); }

        XmlMapper xmlMapper = new XmlMapper();
        SpaceMarine newSpaceMarine = xmlMapper.readValue(stringBuilder.toString(), SpaceMarine.class);

        this.spaceMarineService.updateSpaceMarine(newSpaceMarine);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);

        if(req.getPathInfo() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] pathParams = req.getPathInfo().split("/");
        if(pathParams.length != 3) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Integer id = Converter.intConvert(pathParams[1]);

        if (id == null && !pathParams[2].equals("delete")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        SpaceMarine spaceMarine = this.spaceMarineService.deleteSpaceMarineById(id);
        if (spaceMarine == null){
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        Integer id = Converter.intConvert(pathParams[1]);
        SpaceMarine spaceMarine = this.spaceMarineService.findSpaceMarine(id);

        if (spaceMarine == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setContentType("application/xml");
        PrintWriter writer = resp.getWriter();
        XmlMapper xmlMapper = new XmlMapper();

        writer.println(xmlMapper.writeValueAsString(spaceMarine));
        writer.flush();

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
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Response-Type");

        resp.setHeader("Access-Control-Allow-Methods", "POST, DELETE, GET, PUT");
    }
}
