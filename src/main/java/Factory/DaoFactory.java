package Factory;

public interface DaoFactory {
    <T> T createDao(Class<T> daoClass);
}
