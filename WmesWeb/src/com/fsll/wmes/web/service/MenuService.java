package com.fsll.wmes.web.service;

import java.util.List;

import com.fsll.core.entity.SysMenu;

public interface MenuService {

	/***
     * 获取菜单
     * @author wwluo
     * @date 2016-07-22
     */
    public List<SysMenu> findMenu();
    
    /**
     * 获取权限菜单
     * @author mqzou
     * @date 2016-08-24
     * @param memberId
     * @return
     */
    public List<SysMenu> findMenuForPower(String memberId);

}
