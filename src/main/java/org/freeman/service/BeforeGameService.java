package org.freeman.service;

import MyUtils.DependencyContainer;
import org.freeman.dao.BorderDao;
import org.freeman.object.Border;

import java.sql.SQLException;
import java.util.List;

public class BeforeGameService {
    private final BorderDao borderDao = DependencyContainer.get(BorderDao.class);
    public void chooseBorder() throws SQLException {
        Border border = borderDao.GetBorders(9,9).getFirst();
    }

}
