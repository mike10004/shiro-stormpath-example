/*
 * (c) 2015 Mike Chaberski
 *
 * Distributed under MIT License
 */
package com.github.mike10004.examples.shirostormpath;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author mchaberski
 */
@WebFilter(filterName = "UserInfoFilter", urlPatterns = {"/*"})
public class UserInfoFilter implements Filter {

    public static final String ATTR_USER_OBJECT = "user";
    public static final String ATTR_USER_JSON = "userJson";
    
    private static final boolean debug = true;
    private FilterConfig filterConfig;
    
    public UserInfoFilter() {
    }    
    
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("UserInfoFilter:DoBeforeProcessing");
        }
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            Object userObject = buildUserObject(subject);
            log("user: " + userObject);
            request.getServletContext().setAttribute(ATTR_USER_OBJECT, userObject);
            String userJson = new GsonBuilder().setPrettyPrinting().create().toJson(userObject);
            request.getServletContext().setAttribute(ATTR_USER_JSON, userJson);
        } else {
            log("UserInfoFilter: subject not authenticated; destination = " + ((HttpServletRequest)request).getRequestURI());
        }
    }    
    
    /**
     * 
     * @param subject an authenticated subject
     * @return 
     */
    protected Object buildUserObject(Subject subject) {
        PrincipalCollection principals = subject.getPrincipals();
        Set<String> realms = principals.getRealmNames();
        Map<String, Object>  principalsByRealm = new TreeMap<>();
        for (String realm : realms) {
            Collection principalsForRealm = principals.fromRealm(realm);
            List<Object> principalsForRealmAsList = Lists.newArrayList(principalsForRealm);
            principalsByRealm.put(realm, principalsForRealmAsList);
        }
        return principalsByRealm;
    }
    
    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        if (debug) {
            log("UserInfoFilter:doFilter()");
        }
        
        doBeforeProcessing(request, response);
        if (chain != null) {
            chain.doFilter(request, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    protected FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    protected void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {                
                log("UserInfoFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("UserInfoFilter()");
        }
        StringBuilder sb = new StringBuilder("UserInfoFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
    
    public void log(String msg) {
        FilterConfig fc = filterConfig;
        if (fc != null) {
            fc.getServletContext().log(msg);
        } else {   
            Logger.getLogger(getClass().getName()).info(msg);
        }
    }
    
}
