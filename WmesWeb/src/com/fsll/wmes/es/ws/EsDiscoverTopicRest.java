package com.fsll.wmes.es.ws;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.es.model.EsDiscoverTopic;
import com.fsll.wmes.es.service.EsDiscoverTopicService;

/**
 * 查找书箱的全文搜索引擎例子
 */
@Controller
@RequestMapping("/es/discoverTopic")
public class EsDiscoverTopicRest {
    Logger logger = LoggerFactory.getLogger(EsDiscoverTopicRest.class);
    
    @Autowired
    private EsDiscoverTopicService esDiscoverTopicService;

    /**
     * 查询数据
     *
     * @return
     */
    @RequestMapping(value = "/findAll", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<EsDiscoverTopic> findAll(){
    	
    	esDiscoverTopicService.delete("3");
    	esDiscoverTopicService.delete("4");
    	
   	    EsDiscoverTopic discoverTopic = new EsDiscoverTopic();
   	    discoverTopic.setId("3");
   	    discoverTopic.setTypeId("1");
   	    discoverTopic.setAuthor("测试员");
   	    discoverTopic.setClickCoount(100L);
   	    discoverTopic.setReplyCount(200L);
	   	discoverTopic.setTopic("监听和连接节点的频率");
	   	discoverTopic.setOrderBy(0L);
	   	discoverTopic.setIsRecommend("0");
	   	discoverTopic.setIsTop("0");
	   	discoverTopic.setContent("设置为true的时候忽略连接节点时的群集名称验证");
	   	discoverTopic.setLastReply("回复测试");
	   	discoverTopic.setLastReplyTime(new Date());
	   	discoverTopic.setCreateTime(new Date());
	   	discoverTopic.setLastUpdate(new Date());
	   	esDiscoverTopicService.saveOrUpdate(discoverTopic);
	    	
	   	discoverTopic = new EsDiscoverTopic();
   	    discoverTopic.setId("4");
   	    discoverTopic.setTypeId("1");
   	    discoverTopic.setAuthor("测试员");
   	    discoverTopic.setClickCoount(150L);
   	    discoverTopic.setReplyCount(250L);
	   	discoverTopic.setTopic("客户端版本要和集群版本一致");
	   	discoverTopic.setOrderBy(0L);
	   	discoverTopic.setIsRecommend("0");
	   	discoverTopic.setIsTop("0");
	   	discoverTopic.setContent("客户端版本要和集群版本一致，否则有可能出现不可预知的错误");
	   	discoverTopic.setLastReply("回复测试2");
	   	discoverTopic.setLastReplyTime(new Date());
	   	discoverTopic.setCreateTime(new Date());
	   	discoverTopic.setLastUpdate(new Date());
	   	esDiscoverTopicService.saveOrUpdate(discoverTopic);
	   	
   	
		JsonPaging jsonpaging = new JsonPaging();
		jsonpaging.setPage(1);
		jsonpaging.setRows(5);
		EsDiscoverTopic qObj = new EsDiscoverTopic();
		qObj.setTopic("客户端版本");
		jsonpaging = esDiscoverTopicService.findAll(jsonpaging,qObj);
		List<EsDiscoverTopic> ls = jsonpaging.getList();
    	return ls;
    }
    
    /**
     * 获得一条数据
     *
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public EsDiscoverTopic findById(@RequestParam("id") String id) {
    	return esDiscoverTopicService.findById(id);
    }
    
    /**
     * 新增
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public EsDiscoverTopic save(@RequestBody EsDiscoverTopic book) {   	
        return esDiscoverTopicService.saveOrUpdate(book);
    }
    
    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public EsDiscoverTopic update(@RequestParam("id") String id,@RequestBody EsDiscoverTopic book) {
    	book.setId(id);
    	esDiscoverTopicService.saveOrUpdate(book);
        return book;
    }
    
    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void delete(@RequestParam("id") String id) {
    	esDiscoverTopicService.delete(id);
    }

}
