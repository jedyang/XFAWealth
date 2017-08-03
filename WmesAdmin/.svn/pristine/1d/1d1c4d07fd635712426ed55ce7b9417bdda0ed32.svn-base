/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 选择读库或者写库
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-8-26
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
    	String ds = DataSourceHolder.getDataSource();
        return ds;
    }
}
