package org.freeman.dao.Impl;

import MyUtils.MyConnnect;
import MyUtils.MyDate;
import MyUtils.MyLog;
import MyUtils.MyUuid;
import org.freeman.dao.BorderDao;
import org.freeman.object.Border;
import org.freeman.object.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        StringBuilder sb = new StringBuilder("SELECT * FROM border WHERE 1=1");
        if (border.getId() != null) {
            sb.append(" AND id = '").append(border.getId()).append("'");
        }
        if (border.getLength() != null) {
            sb.append(" AND length = ").append(border.getLength());
        }
        if (border.getWidth() != null) {
            sb.append(" AND width = ").append(border.getWidth());
        }
        MyDate.SetTimeParam(sb, border.getGmtCreated(), border.getGmtModified());
        String sql = sb.toString();
        List<Border>borders = getBorderBySql(sql);
        if (borders.isEmpty()) { LOG.error("查询失败"); return null;}
        return borders;
    }

    @Override
    public List<Border> GetBorders(int length, int width) throws SQLException {
        assert connection != null;
        if(length==0 || width==0){ LOG.error("棋盘信息不全"); return null; }
        String sql = "select * from border WHERE length= "+length+ " and width="+width;
        return getBorderBySql(sql);
    }

    @Override
    public Border GetBorder(UUID id) {
        assert connection != null;
        if(id==null){LOG.error("没有找到参数"); return null; }
        String sql = String.format("SELECT * FROM border WHERE id = '%s'", id);
        return getBorderBySql(sql).getFirst();
    }

    private static List<Border> getBorderBySql(String sql){
        List<Border> borders = new ArrayList<>();
        assert connection != null;
        try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
            while (rs.next()) {
                Border border = new Border();
                border.setId(UUID.fromString(rs.getString("id")));
                border.setLength(rs.getInt("length"));
                border.setWidth(rs.getInt("width"));
                border.setGmtCreated(rs.getTimestamp("gmt_created"));
                border.setGmtModified(rs.getTimestamp("gmt_modified"));
                borders.add(border);
            }
        }catch (Exception e){
            LOG.info("getBorderBySql Exception: " + e.getMessage());
            e.fillInStackTrace();
        }
        return borders;
    }
}
