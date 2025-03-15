package ku.cs.services;

public interface DataSources<T> {
    T readData();

    void writeData(T t, Boolean append);

}
