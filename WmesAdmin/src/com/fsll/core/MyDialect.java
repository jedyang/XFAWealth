package com.fsll.core;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
/**
 * hibernate方言类
 * @author zpzhou E-mail:zpzhou@fsll.cn
 * @version 1.0.0
 * Created On: 2016-10-20
 */
public class MyDialect extends MySQLDialect{
	public MyDialect(){  
        super();  
        //函数名必须是小写，试验大写出错  
        //SQLFunctionTemplate函数第一个参数是函数的输出类型，varchar2对应new StringType()    number对应new IntegerType()  
        //?1代表第一个参数,?2代表第二个参数     这是数据库wx_f_get_partystr函数只需要一个参数，所以写成wx_f_get_partystr(?1)  
        this.registerFunction("get_exchange_rate", new SQLFunctionTemplate(new DoubleType(), "get_exchange_rate(?1,?2,?3)"));  
        this.registerFunction("fn_getconfigname", new SQLFunctionTemplate(new StringType(), "fn_getconfigname(?1,?2)"));   
        this.registerFunction("fn_queryIfafirmTeam", new SQLFunctionTemplate(new StringType(), "fn_queryIfafirmTeam(?1)")); 
        
    }  
}
