package Factory;

import org.freeman.dao.BorderDao;
import org.freeman.dao.CellDao;
import org.freeman.dao.GameDao;
import org.freeman.dao.PlayerDao;
import org.freeman.dao.WinnerDao;
import org.freeman.dao.Impl.BorderDaoImpl;
import org.freeman.dao.Impl.CellDaoImpl;
import org.freeman.dao.Impl.GameDaoImpl;
import org.freeman.dao.Impl.PlayerDaoImpl;
import org.freeman.dao.Impl.WinnerDaoImpl;

public class DaoFactoryImpl implements DaoFactory {

    @Override
    public <T> T createDao(Class<T> daoClass) {
        return switch (daoClass.getName()) {
            case "org.freeman.dao.BorderDao"  -> (T) new BorderDaoImpl();
            case "org.freeman.dao.CellDao"  -> (T) new CellDaoImpl();
            case "org.freeman.dao.GameDao"  -> (T) new GameDaoImpl();
            case "org.freeman.dao.PlayerDao"  -> (T) new PlayerDaoImpl();
            case "org.freeman.dao.WinnerDao" -> (T) new WinnerDaoImpl();
            default -> throw new IllegalArgumentException("不支持的 DAO 类： " + daoClass.getName());
        };
    }
}

