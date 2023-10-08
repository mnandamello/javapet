package br.com.fiap.domain.resources;

import br.com.fiap.domain.entity.servico.Servico;
import br.com.fiap.domain.service.ServicoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Path("servico/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServicoResource implements Resource<Servico, Long>{

    @Context
    UriInfo uriInfo;

    ServicoService service = new ServicoService();
    @GET
    @Override
    public Response findAll() {
        List<Servico> all = service.findAll();
        return Response.ok( all ).build();
    }
    @GET
    @Path("/{id}")
    @Override
    public Response findById(@PathParam("id") Long id) {
        Servico servico = service.findById( id );

        if (Objects.isNull( servico )) return Response.status( 404 ).build();

        return Response.ok( servico ).build();
    }
    @POST
    @Override
    public Response persiste(Servico servico) {
        servico.setId( null );
        Servico s = service.persiste( servico );

        if (Objects.isNull( s.getId() ))
            return Response.notModified( "Não foi possível persistir: " + servico ).build();

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path( String.valueOf( s.getId() ) ).build();

        return Response.created( uri ).entity( s ).build();
    }
}