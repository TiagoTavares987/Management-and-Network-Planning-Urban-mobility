package core.entities;

import core.enums.UserType;
import core.baseEntities.Entity;
import core.interfaces.CloneI;

import edu.princeton.cs.algs4.ST;


public class User extends Entity implements CloneI<User> {

  private final ST<Integer, UserPoi> pois = new ST<>();

  public UserType UserType;
  public String Username;
  public String Password;

  public ST<Integer, UserPoi> getPois() {
    return pois;
  }

  /**
   * @return clone igual ao original
   */
  public User Clone() {

    User user = new User();
    user.setId(this.getId());
    user.UserType = this.UserType;
    user.Username = this.Username;
    user.Password = this.Password;

    ST<Integer, UserPoi> userPois = user.getPois();
    for (Integer poiKey : this.pois.keys())
      userPois.put(poiKey, this.pois.get(poiKey).Clone());

    return user;
  }

  public String toString() {
    return getId() + "," + UserType + "," + Username + "," + Password;
  }

  public String poisToString() {

    if (pois.size() == 0)
      return "";

    String result = getId().toString();
    for (Integer poiId : pois.keys()) {
      result += "," + pois.get(poiId).poi;
      result += "," + pois.get(poiId).date;
    }
    return result;
  }
}
