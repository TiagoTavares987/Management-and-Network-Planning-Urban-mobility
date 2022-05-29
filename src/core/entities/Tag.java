package core.entities;

import core.baseEntities.Entity;
import core.interfaces.CloneI;
import java.util.ArrayList;

public class Tag extends Entity implements CloneI<Tag> {

  private final ArrayList<Entity> extra_info = new ArrayList<>();

  public String Description;

  public ArrayList<Entity> get_extra_info() { return extra_info; }

  /**
   * @return clone igual ao original
   */
  @Override
  public Tag Clone() {

    Tag tag = new Tag();
    tag.setId(this.getId());
    tag.Description = this.Description;

    ArrayList<Entity> tagExtraInfo = tag.get_extra_info();
    for (Entity extraInfo : this.extra_info)
      tagExtraInfo.add(extraInfo.Clone());

    return tag;
  }

  public String toString() {
    return getId() + "," + Description;
  }

  public String extra_info_ToString() {
    String result = getId().toString();
    for (Entity extraInfo : extra_info) {
      result += "," + extra_info.get(extraInfo.getId());
    }
    result += "\n";
    return result;
  }
}