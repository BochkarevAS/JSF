package ru.company.servlets;

import ru.company.controller.SearchController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.company.entity.Book;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name="ImageServlet", urlPatterns = "/ImageServlet")
public class ImageServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");

        try (OutputStream out = response.getOutputStream()) {
            int index = Integer.valueOf(request.getParameter("index"));
            SearchController searchController = (SearchController) request.getSession(false).getAttribute("searchController");
            byte[] image = ((Book) searchController.getPager().getList().get(index)).getImage();
            response.setContentLength(image.length);
            out.write(image);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
