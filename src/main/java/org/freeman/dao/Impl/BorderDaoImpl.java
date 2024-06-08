package org.freeman.dao.Impl;

import MyUtils.MyConnnect;
import MyUtils.MyDate;
import MyUtils.MyLog;
import MyUtils.MyUuid;
import org.freeman.dao.BorderDao;
import org.freeman.object.Border;
import org.freeman.object.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BorderDaoImpl implements BorderDao {

    private static final MyLog LOG = MyLog.getInstance();

    private static final Connection connection =  MyConnnect.getConnection();

    @Override
    public Border AddBorder(Border border) throws SQLException {
        assert connection != null;
        if(border.getLength()==null || border.getWidth()==null){
            LOG.error("棋盘信息不全");
            return null;
        }
        String sql = String.format("INSERT INTO border VALUES ('%s','%s','%s','%s','%s');",
                MyUuid.getUuid(),border.getLength(),
                border.getWidth(),MyDate.getNowInDateTime(),
                MyDate.getNowInDateTime());
        int affectedRow = connection.prepareStatement(sql).executeUpdate();
        if(affectedRow>0){
            return GetBorders(border).getFirst();
        }
        return null;
    }

    @Override
    public List<Border> GetBorders() {
        String sql = "select * from player";
        assert connection != null;
        return getBorderBySql(sql);
    }

    @Override
    public List<Border> GetBorders(Border border) {
        StringBuilder sb = new StringBuilder("SELECT * FROM Player WHERE 1=1");
        if (border.getId() != null) {
            sb.append(" AND id = '").append(border.getId()).append("'");
        }
        if (border.getLength() != null) {
            sb.append(" AND name = '").append(border.getLength()).append("'");
        }
        if (border.getWidth() != null) {
            sb.append(" AND name = '").append(border.getWidth()).append("'");
        }
        if (border.getGmtCreated() != null) {
            String gmtCreated = MyDate.truncateTime(border.getGmtCreated());
            sb.append(" AND gmt_created = '").append(gmtCreated).append("'");
        }
        if (border.getGmtModified() != null) {
            String gmtModified = MyDate.truncateTime(border.getGmtModified());
            sb.append(" AND gmt_modified = '").append(gmtModified).append("'");
        }
        String sql = sb.toString();
        List<Border>borders = getBorderBySql(sql);
        if (borders.isEmpty()) { LOG.error("查询失败"); return null;}
        return borders;
    }

    private static List<Border> getBorderBySql(String sql){
        List<Border> borders = new ArrayList<>();
        assert connection != null;
        System.out.println(sql);
        try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
            while (rs.next()) {
                Border border = new Border();
                border.setId(UUID.fromString(rs.getString("id")));
                border.setLength(rs.getInt("length"));
                border.setWidth(rs.getInt("width"));
                border.setGmtCreated(rs.getTimestamp("gmtCreated"));
                border.setGmtModified(rs.getTimestamp("gmtModified"));
                borders.add(border);
            }
        }catch (Exception e){
            LOG.info("getBorderBySql Exception: " + e.getMessage());
            e.fillInStackTrace();
        }
        return borders;
    }
}
