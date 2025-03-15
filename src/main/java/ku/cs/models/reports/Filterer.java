package ku.cs.models.reports;

public interface Filterer<T> {
    Boolean filter(T t);
}
