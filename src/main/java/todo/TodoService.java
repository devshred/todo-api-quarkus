package todo;

import java.util.List;
import java.util.UUID;

public interface TodoService {
  List<TodoEntity> all();

  TodoEntity create(TodoEntity todoEntity);

  TodoEntity findById(UUID id);

  boolean changeStatus(UUID id, boolean done);

  boolean delete(UUID id);
}
