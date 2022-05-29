package core.entities;

import core.baseEntities.Entity;
import core.interfaces.CloneI;
import edu.princeton.cs.algs4.ST;

public class Node extends Entity implements CloneI<Node> {
  private final Localization localization = new Localization();
  private final ST<Integer, Tag> tags = new ST<>();
  private final ST<Integer, Poi> pois = new ST<>();
  
  public String Name;

  public Localization getLocalization() { return localization; }
  public ST<Integer, Poi> getPois() { return pois; }
  public ST<Integer, Tag> getTags() {
    return tags;
  }

  /**
   * @return clone igual ao original
   */
  @Override
  public Node Clone() {

    Node node = new Node();
    node.setId(this.getId());
    node.Name = this.Name;

    Localization nodeLocalization = node.getLocalization();
    nodeLocalization.Latitude = this.localization.Latitude;
    nodeLocalization.Longitude = this.localization.Longitude;

    ST<Integer, Poi> nodePois = node.getPois();
    for (Integer poiKey : this.pois.keys())
      nodePois.put(poiKey, this.pois.get(poiKey).Clone());

    ST<Integer, Tag> nodeTags = node.getTags();
    for (Integer tagKey : this.tags.keys())
      nodeTags.put(tagKey, this.tags.get(tagKey).Clone());

    return node;
  }

  @Override
  public String toString() {
    return getId() + "," + Name + "," + localization.Longitude + "," + localization.Latitude;
  }

  public String tagsToString() {
    String result = getId().toString();
    for(Integer tagId : tags.keys()){
      result += "," + tags.get(tagId).getId();
    }
    result += "\n";
    return result;
  }

  public String poisToString() {
    String result = getId().toString();
    for(Integer poiId : pois.keys()){
      result += "," + pois.get(poiId).getId();
    }
    result += "\n";
    return result;
  }
}