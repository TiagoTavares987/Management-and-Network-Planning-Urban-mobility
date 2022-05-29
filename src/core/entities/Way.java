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
     **/
    @Override
    public Way Clone() {

        Way way = new Way();
        way.setId(this.getId());
        way.Name = this.Name;
        way.Start = this.Start;
        way.End = this.End;

        ST<Integer, Poi> wayPois = way.getPois();
        for (Integer poiKey : this.pois.keys())
            wayPois.put(poiKey, this.pois.get(poiKey).Clone());

        ST<Integer, Tag> wayTags = way.getTags();
        for (Integer tagKey : this.tags.keys())
            wayTags.put(tagKey, this.tags.get(tagKey).Clone());

        return way;
    }

    public String toString() {
        return getId() + "," + Name + "," + Start + "," + End;
    }

    public String tagsToString() {
        String result = getId().toString();
        for (Integer tagId : tags.keys()) {
            result += "," + tags.get(tagId).getId();
        }
        result += "\n";
        return result;
    }
    public String poisToString() {
        String result = getId().toString();
        for (Integer poiId : pois.keys()) {
            result += "," + pois.get(poiId).getId();
        }
        result += "\n";
        return result;
    }
}