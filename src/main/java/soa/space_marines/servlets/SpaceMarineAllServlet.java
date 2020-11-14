package soa.space_marines.servlets;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import soa.space_marines.dto.FilteringObjectDto;
import soa.space_marines.dto.PageableSpaceMarinesDto;
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

@WebServlet("/space/marine/all")
public class SpaceMarineAllServlet extends HttpServlet {
    private final SpaceMarineService spaceMarineService;
    private final FilteringSpaceMarineService filteringSpaceMarineService;

    public SpaceMarineAllServlet(){
        this.spaceMarineService = new SpaceMarineService();
        this.filteringSpaceMarineService = new FilteringSpaceMarineService();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);

        String atPage = req.getParameter("at_page");
        String pageNumber = req.getParameter("page_number");

        FilteringObjectDto filteringObjectDto = filteringSpaceMarineService.prepareFilteringObjectDto(req.getParameterMap());

        if ((atPage != null && !atPage.isEmpty()) && (pageNumber != null && !pageNumber.isEmpty())){
            Integer intAtPage = Converter.intConvert(atPage);
            Integer intPageNumber = Converter.intConvert(pageNumber);

            if (intAtPage != null && intPageNumber != null){
                resp.setContentType("application/xml");
                PrintWriter writer = resp.getWriter();
                String[] sortParams = req.getParameterValues("sort");
                String sortState = req.getParameter("sort_state");
                sortParams = sortParams == null ? new String[0] : sortParams;

                PageableSpaceMarinesDto spaceMarines = this.spaceMarineService.findAllSpaceMarines(
                        intAtPage,
                        intPageNumber,
                        filteringObjectDto,
                        sortParams,
                        sortState);
                XmlMapper xmlMapper = new XmlMapper();

                writer.println(xmlMapper.writeValueAsString(spaceMarines));
                writer.flush();

                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
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
