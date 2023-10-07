package pl.lodz.p.it.ssbd2023.ssbd03.util.etag;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@EtagValidator
public class EtagValidatorFilter implements ContainerResponseFilter {

    @Inject
    private MessageVerifier messageVerifier;

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        String header = containerRequestContext.getHeaderString("If-Match");
        if (header == null || header.isEmpty()) {
            containerRequestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
        } else if (!messageVerifier.validateSignature(header)) {
            containerRequestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
        }
    }
}
