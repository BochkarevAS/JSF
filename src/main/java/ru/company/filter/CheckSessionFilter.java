package ru.company.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "CheckSessionFilter", urlPatterns = "/view/*")
public class CheckSessionFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
        //NOP
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest wrappedRequest = (HttpServletRequest) request;
        HttpServletResponse wrappedResponse = (HttpServletResponse) response;

        HttpSession session = wrappedRequest.getSession(false);

        if (session == null || session.isNew()) {
            wrappedResponse.sendRedirect(wrappedRequest.getContextPath() + "/index.xhtml");
        } else {
            chain.doFilter(wrappedRequest, wrappedResponse);
        }
    }

    public void destroy() {
        //NOP
    }

}
