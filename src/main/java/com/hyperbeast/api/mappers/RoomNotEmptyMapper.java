package com.hyperbeast.api.mappers;

import com.hyperbeast.api.exceptions.RoomNotEmptyException;
import com.hyperbeast.api.models.ErrorMessage;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {
    @Override
    public Response toResponse(RoomNotEmptyException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorMessage(e.getMessage(), 409))
                .type(MediaType.APPLICATION_JSON).build();
    }
}