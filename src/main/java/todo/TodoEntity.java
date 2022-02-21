package todo;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "TODO")
public class TodoEntity extends PanacheEntityBase {
  @Id public UUID id;
  public String text;
  public boolean done;
}
