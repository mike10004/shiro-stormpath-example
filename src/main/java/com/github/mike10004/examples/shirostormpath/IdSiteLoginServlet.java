/*
 * (c) 2015 Mike Chaberski
 *
 * Distributed under MIT License
 */
package com.github.mike10004.examples.shirostormpath;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;
import java.io.IOException;

import java.io.OutputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @author mchaberski
 */
@WebServlet(name = "IdSiteLoginServlet", urlPatterns = {"/login"})
public class IdSiteLoginServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(IdSiteLoginServlet.class.getName());

    /**
     * Handles the HTTP <code>GET</code> method. Constructs the site URL
     * and adds redirect headers to the response. Closes the response output
     * stream.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("GET " + request.getRequestURI());
        
        if (isSubjectAuthenticated()) {
            // TODO check for URL to follow
            response.sendRedirect(request.getServletContext().getContextPath());
        } else {
            String idSiteUrl = constructIdSiteUrl(request);
            sendRedirect(response, idSiteUrl);
        }
    }
    
    protected boolean isSubjectAuthenticated() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    protected URI createCallbackUri(ServletRequest request) {
        String contextPath = request.getServletContext().getContextPath();
        String host = request.getServerName();
        int port = request.getServerPort();
        String scheme = request.getScheme();
        return createCallbackUri(scheme, host, port, contextPath);
    }
    
    protected URI createCallbackUri(String scheme, String host, int port, String contextPath) {
        checkNotNull(scheme, "scheme must be non-null"); 
        checkNotNull(host, "host must be non-null"); 
        checkNotNull(contextPath, "context path must be non-null");
        checkArgument("http".equals(scheme) || "https".equals(scheme), "scheme must be http or https");
        checkArgument(port > 0 && port <= 65535, "port must be in range [1,65535]");
        checkArgument(contextPath.startsWith("/"), "context path must start with /");
        String portStr = ":" + port;
        if ((port == 80 && "http".equals(scheme)) || port == 443 && "https".equals(scheme)) {
            portStr = "";
        }
        String servletPath = IdSiteCallbackServlet.SERVLET_PATH;
        return URI.create(scheme + "://" + host + portStr + contextPath + servletPath);
    }
    
    protected String constructIdSiteUrl(HttpServletRequest request) {
        Stormpaths stormpaths = new Stormpaths();
        Properties stormpathProps = stormpaths.loadStormpathProperties(request.getServletContext());
        ApiKey apiKey = stormpaths.toApiKey(stormpathProps);
        Client client = Clients.builder().setApiKey(apiKey).build();
        String applicationRestUrl = stormpathProps.getProperty("stormpath.applicationRestUrl");
        Application application = client.getResource(applicationRestUrl, Application.class);
        String callbackUri = createCallbackUri(request).toString();
        String idSiteUrl = application.newIdSiteUrlBuilder()
                .setCallbackUri(callbackUri)
                .build();
        log.info("created id site url " + idSiteUrl);
        return idSiteUrl;
    }
    
    protected void sendRedirect(HttpServletResponse response, String idSiteUrl) throws IOException {
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
        response.setHeader("Location", idSiteUrl);
        try (OutputStream out = response.getOutputStream()) { // just close it
            out.flush();
        }
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet that redirects to ID Site page";
    }

}
