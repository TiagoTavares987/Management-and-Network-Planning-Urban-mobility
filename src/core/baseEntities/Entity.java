package core.baseEntities;


public abstract class Entity {

  private Integer id = 0;

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public abstract Entity Clone();
}