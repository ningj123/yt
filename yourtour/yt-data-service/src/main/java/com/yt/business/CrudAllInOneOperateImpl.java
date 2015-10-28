package com.yt.business;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yt.hbase.BaseBean;
import com.yt.neo4j.bean.Neo4jBaseBean;
import com.yt.neo4j.bean.Neo4jBaseDictBean;
import com.yt.neo4j.repository.CrudGeneralOperate;

public class CrudAllInOneOperateImpl extends CrudGeneralOperate implements
		CrudAllInOneOperate {
	private static final Log LOG = LogFactory
			.getLog(CrudAllInOneOperateImpl.class);

	@Autowired
	private CrudAllInOneConfig config;

	/**
	 * 默认的构造函数
	 */
	public CrudAllInOneOperateImpl() {
		super();
	}

	// 判断是否要保存到Hbase中
	private boolean isSave2Hbase() {
		if (config == null) {
			return false;
		}
		return config.isSave2Hbase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.neo4j.repository.CrudGeneralOperate#delete(com.yt.neo4j.bean.
	 * Neo4jBaseBean)
	 */
	@Override
	public void delete(Neo4jBaseBean bean) throws Exception {
		super.delete(bean);

		if (isSave2Hbase() && bean instanceof BaseBean) {
			BaseBean hbaseBean = (BaseBean) bean;
			this.deleteRow(hbaseBean);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.neo4j.repository.CrudGeneralOperate#delete(java.lang.Class)
	 */
	@Override
	public void delete(Class<? extends Neo4jBaseBean> clazz) throws Exception {
		super.delete(clazz);

		if (isSave2Hbase() && clazz.isAssignableFrom(BaseBean.class)) {
			@SuppressWarnings("unchecked")
			Class<? extends BaseBean> c = (Class<? extends BaseBean>) clazz;
			this.deleteRows(c);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.neo4j.repository.CrudGeneralOperate#save(com.yt.neo4j.bean.
	 * Neo4jBaseBean, java.lang.String)
	 */
	@Override
	public void save(Neo4jBaseBean neo4jBean, String operator) throws Exception {
		// 对CrudGeneralOperate中的save方法的完善，支持hbase。
		if (neo4jBean == null) {
			String msg = "The Neo4jBean is null.";
			if (LOG.isWarnEnabled()) {
				LOG.warn(msg);
			}
			throw new NullPointerException("The Neo4jBean is null.");
		}
		Class<? extends Neo4jBaseBean> clazz = neo4jBean.getClass();
		// 判断该图节点是否存在
		Neo4jBaseBean bean = null;
		if (neo4jBean.getGraphId() != null) {
			// 以ID为最优先判断
			bean = get(clazz, neo4jBean.getGraphId());
		}
		if (bean == null && neo4jBean instanceof Neo4jBaseDictBean) {
			// 如果是字典类型的节点，则通过代码来判断该bean是否已经存在
			bean = get(clazz, "code", ((Neo4jBaseDictBean) neo4jBean).getCode());
		}
		if (bean == null && neo4jBean instanceof BaseBeanImpl) {
			// 否则根据rowKey来判断
			bean = get(clazz, "rowKey", ((BaseBeanImpl) neo4jBean).getRowKey());
		}
		if (bean == null) {
			// 该记录不存在，更新Create时间
			neo4jBean.setCreatedTime(System.currentTimeMillis());
			neo4jBean.setGraphId(null);
			neo4jBean.setCreatedUserId(operator);
		} else {
			// 该记录已经存在，更新Update时间
			neo4jBean.setGraphId(bean.getGraphId());
			neo4jBean.setUpdatedTime(System.currentTimeMillis());
			neo4jBean.setUpdatedUserId(operator);
		}

		// 先保存指定的节点
		Neo4jBaseBean tar = template.save(neo4jBean);
		// 再保存关系
		super.saveRelationsOnly(neo4jBean);
		if (LOG.isDebugEnabled()) {
			if (neo4jBean instanceof Neo4jBaseDictBean) {
				LOG.debug(String.format(
						"Save Neo4jBean(Dict) success, code: %s, graphId: %d.",
						((Neo4jBaseDictBean) tar).getCode(), tar.getGraphId()));
			} else {
				LOG.debug(String.format("Save Neo4jBean success, graphId: %d.",
						tar.getGraphId()));
			}
		}

		if (isSave2Hbase() && neo4jBean instanceof BaseBean) {
			BaseBean hbaseBean = (BaseBean) neo4jBean;
			this.saveRow(hbaseBean);
		}
	}

	// 检测Hbase的CRUD操作类是否注入
	private void checkHbaseCrud() throws Exception {
		if (config == null || config.getHbaseCrud() == null) {
			throw new Exception("The Hbase CRUD Operate is null!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.CrudOperate#saveRow(com.yt.hbase.BaseBean)
	 */
	@Override
	public void saveRow(BaseBean bean) throws Exception {
		checkHbaseCrud();
		config.getHbaseCrud().saveRow(bean);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.CrudOperate#saveRows(java.util.List)
	 */
	@Override
	public void saveRows(List<? extends BaseBean> beans) throws Exception {
		checkHbaseCrud();
		config.getHbaseCrud().saveRows(beans);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.CrudOperate#deleteRow(com.yt.hbase.BaseBean)
	 */
	@Override
	public void deleteRow(BaseBean bean) throws Exception {
		checkHbaseCrud();
		config.getHbaseCrud().deleteRow(bean);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.CrudOperate#deleteRows(java.lang.Class)
	 */
	@Override
	public void deleteRows(Class<? extends BaseBean> clazz) throws Exception {
		checkHbaseCrud();
		config.getHbaseCrud().deleteRows(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.CrudOperate#getRow(java.lang.String, java.lang.String)
	 */
	@Override
	public BaseBean getRow(String className, String rowkey) throws Exception {
		checkHbaseCrud();
		return config.getHbaseCrud().getRow(className, rowkey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.CrudOperate#getRow(java.lang.Class, java.lang.String)
	 */
	@Override
	public BaseBean getRow(Class<? extends BaseBean> clazz, String rowKey)
			throws Exception {
		checkHbaseCrud();
		return config.getHbaseCrud().getRow(clazz, rowKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.CrudOperate#getRows(java.lang.Class)
	 */
	@Override
	public List<? extends BaseBean> getRows(Class<? extends BaseBean> clazz)
			throws Exception {
		checkHbaseCrud();
		return config.getHbaseCrud().getRows(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.CrudOperate#getRows(java.lang.Class, long, long)
	 */
	@Override
	public List<? extends BaseBean> getRows(Class<? extends BaseBean> clazz,
			long ts1, long ts2) throws Exception {
		checkHbaseCrud();
		return config.getHbaseCrud().getRows(clazz, ts1, ts2);
	}

}
