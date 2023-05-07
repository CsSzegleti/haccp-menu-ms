package hu.soft4d.resource;

import hu.soft4d.model.MenuItem;
import hu.soft4d.resource.utils.Roles;
import io.quarkus.security.Authenticated;
import org.apache.commons.beanutils.BeanUtils;
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

@Path("/menu/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@Authenticated
public class MenuItemResource {

    @GET
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successful query",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(type = SchemaType.ARRAY, implementation = MenuItem.class))}
        ),
    })
    //@RolesAllowed(Roles.USER_ROLE)
    public List<MenuItem> findAll() {
        return MenuItem.listAll();
    }

    @NoCache
    @GET
    @Path("{id}")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successful query",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = MenuItem.class))}
        ),
        @APIResponse(responseCode = "404", description = "Entity not found",
            content = {@Content(mediaType = "application/json")}
        )
    })
    //@RolesAllowed(Roles.USER_ROLE)
    public MenuItem findById(@PathParam("id") Long id) {
        return (MenuItem) MenuItem.findByIdOptional(id).orElseThrow(NotFoundException::new);
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
    @POST
    @Transactional(Transactional.TxType.REQUIRED)
    @NoCache
    //@RolesAllowed(Roles.ADMIN_ROLE)
    public Response save(MenuItem menuItem, @Context UriInfo uriInfo) {
        MenuItem.persist(menuItem);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(menuItem.id.toString());
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
    @PUT
    @Path("{id}")
    @Transactional
    @NoCache
    @RolesAllowed(Roles.ADMIN_ROLE)
    public Response updateMenuItem(MenuItem menuItem, @PathParam("id") Long id, @Context UriInfo uriInfo) throws InvocationTargetException, IllegalAccessException {
        if(!id.equals(menuItem.id)) {
            throw new BadRequestException();
        }

        Optional<MenuItem> entity = MenuItem.findByIdOptional(menuItem.id);
        if (entity.isEmpty()) {
            throw new NotFoundException();
        }

        BeanUtils.copyProperties(entity.get(), menuItem);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(menuItem.id.toString());
        return Response.ok(builder.build()).build();
    }

    @NoCache
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Delete successful",
            content = {@Content(mediaType = "application/json")}
        )
    })
    @DELETE
    @Transactional
    @Path("{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    //@RolesAllowed(Roles.ADMIN_ROLE)
    public void deleteMenuItem(@PathParam("id") Long id) {
        MenuItem.deleteById(id);
    }
}
