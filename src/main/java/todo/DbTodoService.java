package todo;

import java.util.List;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class DbTodoService implements TodoService {

  @Override
  public List<TodoEntity> all() {
    return TodoEntity.listAll();
  }

  @Override
  @Transactional
  public TodoEntity create(TodoEntity todoEntity) {
    todoEntity.id = UUID.randomUUID();
    todoEntity.persist();
    return todoEntity;
  }

  @Override
  public TodoEntity findById(UUID id) {
    return TodoEntity.findById(id);
  }

  @Override
  @Transactional
  public boolean changeStatus(UUID id, boolean done) {
    TodoEntity dbObject = TodoEntity.findById(id);
    if (dbObject == null) {
      return false;
    }
    dbObject.done = done;
    return true;
  }

  @Override
  @Transactional
  public boolean delete(UUID id) {
    TodoEntity dbObject = TodoEntity.findById(id);
    if (dbObject == null) {
      return false;
    }
    dbObject.delete();
    return true;
  }
}
