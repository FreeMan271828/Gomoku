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

public class DaoFactory {

    private static final BorderDao borderDao = new BorderDaoImpl();
    private static final CellDao cellDao = new CellDaoImpl();
    private static final GameDao gameDao = new GameDaoImpl();
    private static final PlayerDao playerDao = new PlayerDaoImpl();
    private static final WinnerDao winnerDao = new WinnerDaoImpl();

    public static <T> T createDao(Class<T> daoClass) {
        return switch (daoClass.getName()) {
            case "org.freeman.dao.BorderDao"  -> (T) borderDao;
            case "org.freeman.dao.CellDao"  -> (T) cellDao;
            case "org.freeman.dao.GameDao"  -> (T) gameDao;
            case "org.freeman.dao.PlayerDao"  -> (T) playerDao;
            case "org.freeman.dao.WinnerDao" -> (T) winnerDao;
            default -> throw new IllegalArgumentException("不支持的 DAO 类： " + daoClass.getName());
        };
    }

}

