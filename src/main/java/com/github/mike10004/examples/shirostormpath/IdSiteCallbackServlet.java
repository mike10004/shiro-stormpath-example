/*
 * (c) 2015 Mike Chaberski
 *
 * Distributed under MIT License
 */
package com.github.mike10004.examples.shirostormpath;

import static com.google.common.base.Preconditions.checkNotNull;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.idsite.AccountResult;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;

/**
 *
 * @author mchaberski
 */
@WebServlet(name = "id-site-callback", urlPatterns = {IdSiteCallbackServlet.SERVLET_PATH})
public class IdSiteCallbackServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(IdSiteCallbackServlet.class.getName());
    public static final String SERVLET_PATH = "/id-site-callback";
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Stormpaths stormpaths = new Stormpaths();
        Application application = stormpaths.buildApplication(request.getServletContext());
        AccountResult accountResult = application.newIdSiteCallbackHandler(request).getAccountResult();
        Account account = accountResult.getAccount();
        log.log(Level.INFO, "logging in with principal {0}", account);
        AuthenticationToken token = new IdSiteAuthenticationToken(account.getUsername(), account);
        SecurityUtils.getSubject().login(token);
        response.sendRedirect(request.getContextPath()); // to welcome file
    }

    public static class StormpathAccountAuthenticationToken implements AuthenticationToken {

        private final Account account;
        private final Object credentialsPlaceholder;
        
        public StormpathAccountAuthenticationToken(Account account) {
            this.account = checkNotNull(account);
            credentialsPlaceholder = "";
        }
        
        @Override
        public Object getPrincipal() {
            return account;
        }

        @Override
        public Object getCredentials() {
            return credentialsPlaceholder;
        }
        
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet that handles redirect from ID Site";
    }

}
