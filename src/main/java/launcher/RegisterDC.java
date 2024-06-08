package launcher;

import myUtils.DependencyContainer;
import org.freeman.dao.*;
import org.freeman.dao.Impl.*;

public class RegisterDC {

    public static void register() throws Exception {

        DependencyContainer.register(BorderDao.class,new BorderDaoImpl());

        DependencyContainer.register(GameDao.class,new GameDaoImpl());

        DependencyContainer.register(PlayerDao.class,new PlayerDaoImpl());

        DependencyContainer.register(CellDao.class,new CellDaoImpl());

        DependencyContainer.register(WinnerDao.class,new WinnerDaoImpl());
    }


}
