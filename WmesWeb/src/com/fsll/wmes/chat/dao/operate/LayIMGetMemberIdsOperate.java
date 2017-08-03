package com.fsll.wmes.chat.dao.operate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fsll.wmes.chat.dao.db.IResultSetOperate;
import com.fsll.wmes.chat.util.log.LayIMLog;



/**
 * Created by mjhuang
 */
public class LayIMGetMemberIdsOperate implements IResultSetOperate {
    public Object getObject(ResultSet resultSet) {
        List<String> list = new ArrayList<String>();
        try {
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            LayIMLog.error(ex);
        }
        return list;
    }

    public Object getObject(Statement statement) {
        return null;
    }
}
