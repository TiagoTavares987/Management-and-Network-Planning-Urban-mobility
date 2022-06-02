package database;

import java.io.IOException;
import java.text.ParseException;

public abstract class Database {
    public void ReadFromFile() throws IOException, ParseException {
        ReadFromFile(null);
    }
    public abstract void ReadFromFile(String path) throws IOException, ParseException;

    public void ReadFromBinFile() throws IOException, ParseException {
        ReadFromBinFile(null);
    }
    public abstract void ReadFromBinFile(String path) throws IOException, ParseException;
}
