package dev.vsviniciuslima.whatsapp.connections.exceptions;

import dev.vsviniciuslima.common.ErrorResponse;
import dev.vsviniciuslima.whatsapp.connections.exceptions.ConnectionException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.Map;

@ApplicationScoped
public class ConnectionsExceptionMapper {

    @ServerExceptionMapper
    public RestResponse<Object> mapException(ConnectionException connectionException) {
        ErrorResponse errorResponse = new ErrorResponse(
                connectionException.getMessage(),
                Response.Status.UNAUTHORIZED.getStatusCode(),
                Map.of("userPhoneNumber", connectionException.getUserPhoneNumber())
        );
        return RestResponse.status(Response.Status.UNAUTHORIZED, errorResponse);
    }
}
