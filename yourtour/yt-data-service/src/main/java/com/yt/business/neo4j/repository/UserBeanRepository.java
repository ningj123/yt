/** 
 * @(#)UserBeanRepository.java
 *
 * Copyright 2015, 迪爱斯通信设备有限公司保留.<br>
 *
 * @author John Peng
 */
package com.yt.business.neo4j.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.yt.business.bean.UserBean;

/**
 * 关于用户实体（UserBean）的关系查询接口定义。
 * 
 * <p>
 * <b>修改历史：</b>
 * <table border="1">
 * <tr>
 * <th>修改时间</th>
 * <th>修改人</th>
 * <th>备注</th>
 * </tr>
 * <tr>
 * <td>2015年6月22日</td>
 * <td>john</td>
 * <td>Create</td>
 * </tr>
 * </table>
 * 
 * @author john
 * 
 * @version 1.0
 * @since 1.0
 */
public interface UserBeanRepository extends GraphRepository<UserBean> {

	/**
	 * 获取关注了相同行程的用户列表
	 * 
	 * @param user
	 *            　指定的用户
	 * @return　用户列表
	 */
	@Query("START u=node({0}) MATCH u-[:watchRoute]->(route)<-[:watchRoute]-(users) RETURN users")
	public List<UserBean> getUsersWatchSameRoutes(UserBean user);

	/**
	 * 根据分页要求查询所有的用户
	 * 
	 * @param skip
	 *            分页起始记录数
	 * @param limit
	 *            本页最大记录数
	 * @return 该页的用户
	 */
	@Query("MATCH (users:UserBean) RETURN users SKIP {0} LIMIT {1}")
	public List<UserBean> getUsersByPage(long skip, long limit);

}
