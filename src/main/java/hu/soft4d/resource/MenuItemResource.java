package hu.soft4d.resource;

import hu.soft4d.model.MenuItem;
import hu.soft4d.resource.converter.MenuItemConverter;
import hu.soft4d.resource.dto.MenuItemDto;
import hu.soft4d.resource.utils.Roles;
import io.quarkus.security.Authenticated;
import liquibase.pro.packaged.R;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/menu/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class MenuItemResource {

    @Inject
    MenuItemConverter menuItemConverter;

    @GET
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successful query",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = MenuItem.class))}
        ),
    })
    @RolesAllowed(Roles.USER_ROLE)
    public List<MenuItemDto> findAll() {
        return MenuItem.listAll().stream().map(item -> menuItemConverter.toExternal((MenuItem) item)).collect(Collectors.toList());
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
    @RolesAllowed(Roles.USER_ROLE)
    public MenuItemDto findById(@PathParam("id") Long id) {
        return menuItemConverter.toExternal ((MenuItem) MenuItem.findByIdOptional(id).orElseThrow(NotFoundException::new));
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
    @RolesAllowed(Roles.ADMIN_ROLE)
    public Response save(MenuItemDto menuItem, @Context UriInfo uriInfo) {
        MenuItem entity = menuItemConverter.toBusiness(menuItem);
        MenuItem.persist(entity);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(entity.id.toString());
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
    @Transactional
    @NoCache
    @RolesAllowed(Roles.ADMIN_ROLE)
    public Response updateMenuItem(MenuItemDto menuItem, @Context UriInfo uriInfo) throws InvocationTargetException, IllegalAccessException {
        Optional<MenuItem> entity = MenuItem.findByIdOptional(menuItem.id);
        if (entity.isEmpty()) {
            throw new NotFoundException();
        }

        BeanUtils.copyProperties(entity.get(), menuItemConverter.toBusiness(menuItem));

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
    @RolesAllowed(Roles.ADMIN_ROLE)
    public void deleteMenuItem(@PathParam("id") Long id) {
        MenuItem.deleteById(id);
    }
}
