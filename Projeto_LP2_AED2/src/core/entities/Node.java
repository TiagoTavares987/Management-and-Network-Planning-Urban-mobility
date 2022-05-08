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

    var node = new Node();
    node.setId(this.getId());
    node.Name = this.Name;

    var nodeLocalization = node.getLocalization();
    nodeLocalization.Latitude = this.localization.Latitude;
    nodeLocalization.Longitude = this.localization.Longitude;

    var nodePois = node.getPois();
    for (var poiKey : this.pois.keys())
      nodePois.put(poiKey, this.pois.get(poiKey).Clone());

    var nodeTags = node.getTags();
    for (var tagKey : this.tags.keys())
      nodeTags.put(tagKey, this.tags.get(tagKey).Clone());

    return node;
  }
}