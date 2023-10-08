package br.com.fiap.domain.resources;

import br.com.fiap.domain.entity.animal.Animal;
import br.com.fiap.domain.service.AnimalService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Path("animal/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnimalResource implements Resource<Animal, Long>{

    @Context
    UriInfo uriInfo;

    AnimalService service = new AnimalService();
    @GET
    @Override
    public Response findAll() {
        List<Animal> all = service.findAll();
        return Response.ok( all ).build();
    }
    @GET
    @Path("/{id}")
    @Override
    public Response findById(@PathParam("id") Long id) {

        Animal animal = service.findById(id);

        if (Objects.isNull( animal )) return Response.status( 404 ).build();

        return Response.ok( animal ).build();
    }
    @POST
    @Override
    public Response persiste(Animal animal) {
        animal.setId( null );
        Animal a = service.persiste( animal );

        if (Objects.isNull( a.getId() ))
            return Response.notModified( "Não foi possível persistir: " + animal ).build();

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path( String.valueOf( a.getId() ) ).build();

        return Response.created( uri ).entity( a ).build();
    }
}