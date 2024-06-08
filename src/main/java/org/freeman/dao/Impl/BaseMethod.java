package org.freeman.dao.Impl;

import myUtils.MyDate;

import java.sql.Timestamp;

public class BaseMethod {

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
