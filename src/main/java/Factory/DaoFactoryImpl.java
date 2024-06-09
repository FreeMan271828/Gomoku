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
        if (daoClass.equals(BorderDao.class)) {
            return (T) new BorderDaoImpl();
        } else if (daoClass.equals(CellDao.class)) {
            return (T) new CellDaoImpl();
        } else if (daoClass.equals(GameDao.class)) {
            return (T) new GameDaoImpl();
        } else if (daoClass.equals(PlayerDao.class)) {
            return (T) new PlayerDaoImpl();
        } else if (daoClass.equals(WinnerDao.class)) {
            return (T) new WinnerDaoImpl();
        } else {
            throw new IllegalArgumentException("不支持的 DAO 类： " + daoClass.getName());
        }
    }
}
