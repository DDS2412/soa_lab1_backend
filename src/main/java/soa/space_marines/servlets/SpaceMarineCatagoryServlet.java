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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/space/marine/category/*")
public class SpaceMarineCatagoryServlet extends HttpServlet {
    private final SpaceMarineService spaceMarineService;

    public SpaceMarineCatagoryServlet(){
        this.spaceMarineService = new SpaceMarineService();
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

        Integer category = Converter.intConvert(pathParams[1]);
        if (category == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<SpaceMarine> spaceMarines = this.spaceMarineService.findSpaceMarineWhenCategoryGreater(category);
        resp.setContentType("application/xml");
        PrintWriter writer = resp.getWriter();
        XmlMapper xmlMapper = new XmlMapper();

        writer.println(xmlMapper.writeValueAsString(spaceMarines));
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
        resp.setHeader("Access-Control-Allow-Methods", "GET");
    }
}
