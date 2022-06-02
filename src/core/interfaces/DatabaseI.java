package core.interfaces;

import java.io.IOException;
import java.text.ParseException;
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

  void ReadFromFile() throws IOException, ParseException;

  void ReadFromFile(String path) throws IOException, ParseException;

  void SaveToBinFile() throws IOException;

  void ReadFromBinFile(String path) throws IOException;

}