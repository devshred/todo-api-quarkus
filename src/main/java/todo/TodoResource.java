package todo;

import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("/api")
public class TodoResource {
  @GET
  public List<TodoItem> allAccounts() {
    return TodoItem.listAll();
  }

  @POST
  @Transactional
  public Response createTodoItem(TodoItem todoItem) {
    todoItem.id = UUID.randomUUID().toString();
    todoItem.persist();
    return Response.status(201).entity(todoItem).build();
  }

  @GET
  @Path("/{id}")
  public TodoItem getTodoItem(@PathParam("id") String id) {
    TodoItem todoItem = TodoItem.findById(id);

    if (todoItem == null) {
      throw new WebApplicationException("TodoItem with " + id + " does not exist.", 404);
    }

    return todoItem;
  }

  @PATCH
  @Path("/{id}")
  @Transactional
  public TodoItem deposit(@PathParam("id") String id, TodoItem patchedObject) {
    TodoItem dbObject = TodoItem.findById(id);

    if (dbObject == null) {
      throw new WebApplicationException("TodoItem with " + id + " does not exist.", 404);
    }

    dbObject.done = patchedObject.done;
    return dbObject;
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  public Response closeAccount(@PathParam("id") String id) {
    TodoItem dbObject = TodoItem.findById(id);

    if (dbObject == null) {
      throw new WebApplicationException("TodoItem with " + id + " does not exist.", 404);
    }

    dbObject.delete();
    return Response.noContent().build();
  }
}
