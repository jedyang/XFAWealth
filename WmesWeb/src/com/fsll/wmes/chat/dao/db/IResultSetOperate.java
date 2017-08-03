package com.fsll.wmes.chat.dao.db;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by mjhuang
 */
public interface IResultSetOperate {
    //操作resultSet返回相应的数据
    Object getObject(ResultSet resultSet);
    Object getObject(Statement statement);
}
