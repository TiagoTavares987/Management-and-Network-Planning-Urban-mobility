package core.entities;

import core.baseEntities.Entity;
import core.interfaces.CloneI;
import edu.princeton.cs.algs4.ST;


public class Way extends Entity implements CloneI<Way> {

    private final ST<Integer, Tag> tags = new ST<>();
    private final ST<Integer, Poi> pois = new ST<>();

  public String Name;
  public Integer Start;
  public Integer End;

  public ST<Integer, Poi> getPois() { return pois; }
    public ST<Integer, Tag> getTags() {
        return tags;
    }


    /**
     * @return clone igual ao original
     */
  @Override
  public Way Clone() {

      var way = new Way();
      way.setId(this.getId());
      way.Name = this.Name;
      way.Start = this.Start;
      way.End = this.End;

      var wayPois = way.getPois();
      for (var poiKey : this.pois.keys())
          wayPois.put(poiKey, this.pois.get(poiKey).Clone());

      var wayTags = way.getTags();
      for (var tagKey : this.tags.keys())
          wayTags.put(tagKey, this.tags.get(tagKey).Clone());

    return way;
  }
}