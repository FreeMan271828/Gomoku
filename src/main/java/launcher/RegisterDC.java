package launcher;

import myUtils.DependencyContainer;
import org.freeman.dao.BorderDao;
import org.freeman.dao.CellDao;
import org.freeman.dao.GameDao;
import org.freeman.dao.Impl.BorderDaoImpl;
import org.freeman.dao.Impl.CellDaoImpl;
import org.freeman.dao.Impl.GameDaoImpl;
import org.freeman.dao.Impl.PlayerDaoImpl;
import org.freeman.dao.PlayerDao;

public class RegisterDC {

    public static void register() throws Exception {

        DependencyContainer.register(BorderDao.class,new BorderDaoImpl());

        DependencyContainer.register(GameDao.class,new GameDaoImpl());

        DependencyContainer.register(PlayerDao.class,new PlayerDaoImpl());

        DependencyContainer.register(CellDao.class,new CellDaoImpl());
    }


}
