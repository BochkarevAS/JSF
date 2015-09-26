package ru.company.filter;

import org.hibernate.SessionFactory;
import ru.company.entity.HibernateUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CheckSessionFilter", urlPatterns = {"/view/*", "/PdfContentServlet"})
public class CheckSessionFilter implements Filter {

    private SessionFactory sessionFactory;

    public void init(FilterConfig config) throws ServletException {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest wrappedRequest = (HttpServletRequest) request;
        HttpServletResponse wrappedResponse = (HttpServletResponse) response;

        sessionFactory.getCurrentSession().beginTransaction();

        chain.doFilter(wrappedRequest, wrappedResponse);

        sessionFactory.getCurrentSession().getTransaction().commit();

    }

    public void destroy() {
        //NOP
    }

}
