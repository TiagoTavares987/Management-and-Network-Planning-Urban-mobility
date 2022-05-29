package core.interfaces;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Interface da database
 * @param <T> tabela da database que corresponde ao tipo T
 */
public interface DatabaseI<T> {

  T GetEntity(Integer id);

  ArrayList<T> GetTable();

  T Insert(T entity);

  T Update(T entity);

  boolean Delete(Integer id);

  void SaveToFile() throws IOException;

  void ReadFromFile() throws IOException;

  void SaveToBinFile() throws IOException;

  void ReadFromBinFile() throws IOException;

}