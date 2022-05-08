package core.interfaces;

import java.util.ArrayList;

/**
 * Interface da database
 * @param <T> tipo da database
 */
public interface DatabaseI<T> {

  public T GetEntity(Integer id);

  public ArrayList<T> GetTable();

  public T Insert(T entity);

  public T Update(T entity);

  public boolean Delete(Integer id);

}