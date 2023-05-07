package hu.soft4d.resource;

import hu.soft4d.model.Category;
import hu.soft4d.model.MenuItem;
import hu.soft4d.resource.utils.Roles;
import io.quarkus.security.Authenticated;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
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
@Path("/menu/category")
//@Authenticated
public class CategoryResource {
    @GET
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful query",
                content = {@Content(mediaType = "application/json",
                schema = @Schema(type = SchemaType.ARRAY, implementation = Category.class))}
            ),
    })
    //@RolesAllowed(Roles.USER_ROLE)
    public List<Category> findAll() {
        return Category.listAll();
    }

    @NoCache
    @GET
    @Path("{id}")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful query",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))}
            ),
            @APIResponse(responseCode = "404", description = "Entity not found",
                    content = {@Content(mediaType = "application/json")}
            )
    })
    //@RolesAllowed(Roles.USER_ROLE)
    public Category findById(@PathParam("id") Long id) {
        return (Category) Category.findByIdOptional(id).orElseThrow(NotFoundException::new);
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
    public Response saveCategory(Category category, @Context UriInfo uriInfo) {
        MenuItem.persist(category);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(category.id.toString());
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
//    @RolesAllowed(Roles.ADMIN_ROLE)
    public Response updateCategory(@RequestBody Category category, @PathParam("id") Long id, @Context UriInfo uriInfo) throws InvocationTargetException, IllegalAccessException {
        if(!id.equals(category.id)) {
            throw new BadRequestException();
        }

        Optional<Category> entity = Category.findByIdOptional(category.id);
        if (entity.isEmpty()) {
            throw new NotFoundException();
        }

        BeanUtils.copyProperties(entity.get(), category);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(category.id.toString());
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
    //@RolesAllowed(Roles.ADMIN_ROLE)
    public void deleteCategory(@PathParam("id") Long id) {
        Category.deleteById(id);
    }


    @GET
    @Path("{id}/items")
    //@RolesAllowed(Roles.USER_ROLE)
    public List<MenuItem> getItems(@PathParam("id") Long id) {
        var category = (Category) Category.findByIdOptional(id).orElseThrow(NotFoundException::new);

        return category.items;
    }
}
