package core.entities;

import core.baseEntities.Entity;
import core.interfaces.CloneI;


public class Poi extends Entity implements CloneI<Poi> {
  
  private final Localization localization = new Localization();
  
  public String Name;
  public String Description;

  public Localization getLocalization() { return localization; }

  /**
   * @return clone igual ao original
   */
  @Override
  public Poi Clone() {

    Poi poi = new Poi();
    poi.setId(this.getId());
    poi.Name = this.Name;
    poi.Description = this.Description;

    Localization poiLocalization = poi.getLocalization();
    poiLocalization.Latitude = this.localization.Latitude;
    poiLocalization.Longitude = this.localization.Longitude;

    return poi;
  }

  @Override
  public String toString() {
    return getId() + "," + Name + "," + Description + "," + localization.Longitude + "," + localization.Latitude;
  }
}