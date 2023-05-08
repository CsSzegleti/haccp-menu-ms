package hu.soft4d.resource;

import hu.soft4d.model.Allergen;
import hu.soft4d.resource.utils.Roles;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/menu/allergen")
//@Authenticated
public class AllergenResource {
    @GET
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successful query",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(type = SchemaType.ARRAY, implementation = Allergen.class))}
        ),
    })
    @Operation(
            operationId = "ListAllergens"
    )
    //@RolesAllowed(Roles.USER_ROLE)
    public List<Allergen> findAll() {
        return Allergen.listAll();
    }

    @NoCache
    @GET
    @Path("{id}")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successful query",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Allergen.class))}
        ),
        @APIResponse(responseCode = "404", description = "Entity not found",
            content = {@Content(mediaType = "application/json")}
        )
    })
    @Operation(
            operationId = "GetAllergenById"
    )
    //@RolesAllowed(Roles.USER_ROLE)
    public Allergen findById(@PathParam("id") Long id) {
        return (Allergen) Allergen.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Adding successful",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Response.class))}
        ),
        @APIResponse(responseCode = "500", description = "Item already exists",
            content = {@Content(mediaType = "application/json")}
        )
    })
    @Operation(
            operationId = "AddAllergen"
    )
    @POST
    @Transactional(Transactional.TxType.REQUIRED)
    @NoCache
    //@RolesAllowed(Roles.ADMIN_ROLE)
    public Response save(Allergen allergen, @Context UriInfo uriInfo) {
        Allergen.persist(allergen);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(allergen.id.toString());
        return Response.created(builder.build()).build();
    }

    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Modification successful",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Response.class))}
        ),
        @APIResponse(responseCode = "500", description = "No such item",
            content = {@Content(mediaType = "application/json")}
        )
    })
    @Operation(
            operationId = "ModifyAllergen"
    )
    @PUT
    @Transactional
    @Path("{id}")
    @NoCache
//    @RolesAllowed(Roles.ADMIN_ROLE)
    public Response update(Allergen allergen, @PathParam("id") Long id, @Context UriInfo uriInfo) throws InvocationTargetException, IllegalAccessException {
        if(!id.equals(allergen.id)) {
            throw new BadRequestException();
        }

        Optional<Allergen> entity = Allergen.findByIdOptional(allergen.id);
        if (entity.isEmpty()) {
            throw new NotFoundException();
        }

        BeanUtils.copyProperties(entity.get(), allergen);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        return Response.ok(builder.build()).build();
    }

    @NoCache
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Delete successful",
            content = {@Content(mediaType = "application/json")}
        )
    })
    @Operation(
            operationId = "DeleteAllergenById"
    )
    @DELETE
    @Transactional
    @Path("{id}")
    //@RolesAllowed(Roles.ADMIN_ROLE)
    public void delete(@PathParam("id") Long id) {
        Allergen.deleteById(id);
    }
}
