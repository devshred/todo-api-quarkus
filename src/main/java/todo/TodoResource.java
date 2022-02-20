package todo;

import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/todo")
public class TodoResource {
    @GET
    public List<TodoEntity> allTodoItems() {
        return TodoEntity.listAll();
    }

    @POST
    @Transactional
    public Response createTodoItem(TodoEntity todoEntity) {
        todoEntity.id = UUID.randomUUID();
        todoEntity.persist();
        return Response.status(201).entity(todoEntity).build();
    }

    @GET
    @Path("/{id}")
    public TodoEntity getTodoItem(@PathParam("id") UUID id) {
        TodoEntity todoEntity = TodoEntity.findById(id);

        if (todoEntity == null) {
            throw new WebApplicationException("TodoItem with " + id + " does not exist.", 404);
        }

        return todoEntity;
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response changeTodoItem(@PathParam("id") UUID id, TodoEntity patchedObject) {
        TodoEntity dbObject = TodoEntity.findById(id);

        if (dbObject == null) {
            throw new WebApplicationException("TodoItem with " + id + " does not exist.", 404);
        }

        dbObject.done = patchedObject.done;
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteTodoItem(@PathParam("id") UUID id) {
        TodoEntity dbObject = TodoEntity.findById(id);

        if (dbObject == null) {
            throw new WebApplicationException("TodoItem with " + id + " does not exist.", 404);
        }

        dbObject.delete();
        return Response.noContent().build();
    }
}
