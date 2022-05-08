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

    var user = new User();
    user.setId(this.getId());
    user.UserType = this.UserType;
    user.Username = this.Username;
    user.Password = this.Password;

    var userPois = user.getPois();
    for (var poiKey : this.pois.keys())
      userPois.put(poiKey, this.pois.get(poiKey).Clone());

    return user;
  }
}
