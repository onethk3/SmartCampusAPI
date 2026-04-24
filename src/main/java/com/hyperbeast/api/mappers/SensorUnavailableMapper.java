package com.hyperbeast.api.mappers;

import com.hyperbeast.api.exceptions.SensorUnavailableException;
import com.hyperbeast.api.models.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {
    @Override
    public Response toResponse(SensorUnavailableException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(new ErrorMessage(e.getMessage(), 403))
                .type(MediaType.APPLICATION_JSON).build();
    }
}
