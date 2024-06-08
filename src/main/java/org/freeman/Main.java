package org.freeman;

import MyUtils.DependencyContainer;
import org.freeman.dao.BorderDao;
import org.freeman.dao.GameDao;
import org.freeman.dao.Impl.BorderDaoImpl;
import org.freeman.dao.Impl.GameDaoImpl;
import org.freeman.dao.Impl.PlayerDaoImpl;
import org.freeman.dao.PlayerDao;
import org.freeman.object.Border;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        registerDC();//依赖注入
        BorderDao borderDao = DependencyContainer.get(BorderDao.class);
        Border border = new Border();
        border.setLength(9);
        border.setWidth(9);
        Border border1 = borderDao.GetBorders(border).getFirst();
        System.out.println(border1.getId());
    }

    public static void registerDC(){
        DependencyContainer.register(BorderDao.class,new BorderDaoImpl());
        DependencyContainer.register(GameDao.class,new GameDaoImpl());
        DependencyContainer.register(PlayerDao.class,new PlayerDaoImpl());
    }
}