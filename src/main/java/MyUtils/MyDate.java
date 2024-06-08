package MyUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {

    public static String getNowInDateTime(){
        // 获取当前时间的Date对象
        java.util.Date currentDate = new Date();

        // 创建SimpleDateFormat对象，并设置日期时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 使用SimpleDateFormat格式化日期时间

        return sdf.format(currentDate);
    }

    public static String truncateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(timestamp.getTime()));
    }

    public static void SetTimeParam(StringBuilder sb, Timestamp gmtCreated2, Timestamp gmtModified2) {
        if (gmtCreated2 != null) {
            String gmtCreated = MyDate.truncateTime(gmtCreated2);
            sb.append(" AND gmt_created = '").append(gmtCreated).append("'");
        }
        if (gmtModified2 != null) {
            String gmtModified = MyDate.truncateTime(gmtModified2);
            sb.append(" AND gmt_modified = '").append(gmtModified).append("'");
        }
    }

}
