/*
 * (c) 2015 Mike Chaberski
 *
 * Distributed under MIT License
 */
package com.github.mike10004.examples.shirostormpath;

import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeys;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

/**
 *
 * @author mchaberski
 */
public class Stormpaths {
    
    private static final Logger log = Logger.getLogger(Stormpaths.class.getName());
    
    public Properties loadStormpathProperties(ServletContext servletContext) {
        Properties p = new Properties();
        try {
            URL resource = servletContext.getResource("/WEB-INF/stormpath.properties");
            if (resource != null) {
                try (InputStream in = resource.openStream()) {
                    p.load(in);
                }
            } else {
                throw new FileNotFoundException("/WEB-INF/stormpath.properties");
            }
        } catch (IOException e) {
            log.log(Level.WARNING, "failed to load stormpath properties", e);
        }
        return p;
    }
    
    public ApiKey toApiKey(Properties stormpathProps) {
        ApiKey apiKey = ApiKeys.builder().setProperties(stormpathProps)
                .setIdPropertyName("stormpath.apiKey.id")
                .setSecretPropertyName("stormpath.apiKey.secret")
                .build();
        return apiKey;
    }

    public Client buildClient(Properties stormpathProps) {
        ApiKey apiKey = toApiKey(stormpathProps);
        Client client = Clients.builder().setApiKey(apiKey).build();
        return client;
    }
    
    public Application buildApplication(Properties stormpathProperties, Client client) {
        String applicationRestUrl = stormpathProperties.getProperty("stormpath.applicationRestUrl");
        Application application = client.getResource(applicationRestUrl, Application.class);
        return application;
    }
    
    public Application buildApplication(ServletContext servletContext) {
        Properties p = loadStormpathProperties(servletContext);
        Client client = buildClient(p);
        Application application = buildApplication(p, client);
        return application;
    }
}
