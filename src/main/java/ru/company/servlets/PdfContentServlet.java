package ru.company.servlets;

import ru.company.controller.SearchController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "PdfContentServlet", urlPatterns = "/PdfContentServlet")
public class PdfContentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/pdf");

        try (OutputStream out = response.getOutputStream()) {
            int id = Integer.valueOf(request.getParameter("id"));
            Boolean save = Boolean.valueOf(request.getParameter("save"));
            String filename = request.getParameter("filename");

            SearchController searchController = (SearchController) request.getSession(false).getAttribute("searchController");
            byte[] content = searchController.getContent(id);
            response.setContentLength(content.length);
            if (save) {
                response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(filename, "UTF-8") + ".pdf");
            }
            out.write(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
