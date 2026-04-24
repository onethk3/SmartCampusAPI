package com.hyperbeast.api.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext request) {
        LOGGER.info("Method: " + request.getMethod() + " | URI: " + request.getUriInfo().getPath());
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {
        LOGGER.info("Status Code: " + response.getStatus());
    }
}
