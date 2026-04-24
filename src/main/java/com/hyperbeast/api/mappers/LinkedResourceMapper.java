package com.hyperbeast.api.mappers;

import com.hyperbeast.api.exceptions.LinkedResourceNotFoundException;
import com.hyperbeast.api.models.ErrorMessage;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LinkedResourceMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    @Override
    public Response toResponse(LinkedResourceNotFoundException e) {
        return Response.status(422) // Semantically accurate for missing references
                .entity(new ErrorMessage(e.getMessage(), 422))
                .type(MediaType.APPLICATION_JSON).build();
    }
}