package com.hyperbeast.api.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscovery() {
        try {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("version", "v1.0");
            metadata.put("contact", "lead-architect@westminster.ac.uk");

            Map<String, String> links = new HashMap<>();
            links.put("rooms", "/api/v1/rooms");
            links.put("sensors", "/api/v1/sensors");

            metadata.put("resources", links);

            return Response.ok(metadata).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Discovery Logic Error").build();
        }
    }
}