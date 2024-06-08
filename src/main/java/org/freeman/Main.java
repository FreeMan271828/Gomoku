package org.freeman;

import MyUtils.MyLog;
import org.freeman.dao.Impl.PlayerDaoImpl;
import org.freeman.dao.PlayerDao;
import org.freeman.object.Player;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws SQLException {
        PlayerDao playerDao = new PlayerDaoImpl();

    }
}