package todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;

//@ApplicationScoped
public class MemoryTodoService implements TodoService {
  private final Map<UUID, TodoEntity> todoStore = new HashMap();

  @Override
  public List<TodoEntity> all() {
    return new ArrayList<>(todoStore.values());
  }

  @Override
  public TodoEntity create(TodoEntity todoEntity) {
    todoEntity.id = UUID.randomUUID();
    todoStore.put(todoEntity.id, todoEntity);
    return todoEntity;
  }

  @Override
  public TodoEntity findById(UUID id) {
    return todoStore.get(id);
  }

  @Override
  public boolean changeStatus(UUID id, boolean done) {
    TodoEntity entity = todoStore.get(id);
    if (entity == null) {
      return false;
    } else {
      entity.done = done;
      return true;
    }
  }

  @Override
  public boolean delete(UUID id) {
    return todoStore.remove(id) != null;
  }
}
