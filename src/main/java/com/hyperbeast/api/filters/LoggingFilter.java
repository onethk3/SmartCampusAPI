package com.hyperbeast.api.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
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