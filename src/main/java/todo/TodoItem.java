package todo;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "TODO")
public class TodoItem extends PanacheEntityBase {
  @Id public String id;
  public String text;
  public boolean done;
}
