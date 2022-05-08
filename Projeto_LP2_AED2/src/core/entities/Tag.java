package core.entities;

import core.baseEntities.Entity;
import core.interfaces.CloneI;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;
import java.util.Objects;


public class Tag extends Entity implements CloneI<Tag> {

  private final ArrayList<Entity> extra_info = new ArrayList<>();

  public String Description;

  public ArrayList<Entity> get_extra_info() { return extra_info; }

  /**
   * @return clone igual ao original
   */
  @Override
  public Tag Clone() {

    var tag = new Tag();
    tag.setId(this.getId());
    tag.Description = this.Description;

    var tagExtraInfo = tag.get_extra_info();
    for (var extraInfo : this.extra_info)
      tagExtraInfo.add(extraInfo.Clone());

    return tag;
  }
}