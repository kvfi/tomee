package ma.sii.workshop.tomee.rest.service;

import ma.sii.workshop.tomee.rest.dao.PostDAO;
import ma.sii.workshop.tomee.rest.model.Post;

import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/api/post")
@Produces({"text/xml", "application/json"})
public class PostService {

    @EJB
    private PostDAO dao;

    @Path("/create")
    @PUT
    public Post create(@QueryParam("title") String title,
                       @QueryParam("content") String content,
                       @QueryParam("userId") long userId) {
        return dao.create(title, content, userId);
    }

    @Path("/list")
    @GET
    public List<Post> list(@QueryParam("first") @DefaultValue("0") int first,
                           @QueryParam("max") @DefaultValue("20") int max) {
        return dao.list(first, max);
    }

    @Path("/show/{id}")
    @GET
    public Post show(@PathParam("id") long id) {
        return dao.find(id);
    }

    @Path("/delete/{id}")
    @DELETE
    public void delete(@PathParam("id") long id) {
        dao.delete(id);
    }

    @Path("/update/{id}")
    @POST
    public Post update(@PathParam("id") long id,
                       @QueryParam("userId") long userId,
                       @QueryParam("title") String title,
                       @QueryParam("content") String content) {
        return dao.update(id, userId, title, content);
    }
}
