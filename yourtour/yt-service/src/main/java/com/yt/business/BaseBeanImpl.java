package com.yt.business;

import com.yt.core.utils.StringUtils;
import com.yt.hbase.BaseBean;
import com.yt.hbase.annotation.HbaseColumn;
import com.yt.neo4j.Neo4jBaseBean;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import java.io.Serializable;
import java.util.UUID;

@NodeEntity
public class BaseBeanImpl implements Serializable, BaseBean, Neo4jBaseBean,
		Comparable<BaseBeanImpl> {
	private static final long serialVersionUID = -916424014919620404L;

	@Indexed(unique = true)
	private String rowKey = UUID.randomUUID().toString();

	@GraphId
	private Long id = -1l;

	@HbaseColumn(name = "cuid")
	private Long createdUserId = 0l;

	@HbaseColumn(name = "ct")
	private long createdTime = 0l;

	@HbaseColumn(name = "uuid")
	private Long updatedUserId = 0l;

	@HbaseColumn(name = "ut")
	private long updatedTime = 0l;

	@HbaseColumn(name = "rm")
	private boolean isDeleted = false;

	/**
	 * 默认的构造函数
	 */
	public BaseBeanImpl() {
		super();

		this.rowKey = UUID.randomUUID().toString();
	}

	/**
	 * 默认的构造函数
	 */
	public BaseBeanImpl(Long id) {
		super();
		this.id = id;

		this.rowKey = UUID.randomUUID().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(BaseBeanImpl o) {
		if (o == null) {
			return 1;
		}
		Long src = this.getId();
		Long tar = o.getId();
		if (src == null && tar == null) {
			return 0;
		} else if (src != null) {
			return src.compareTo(tar);
		} else {
			return -tar.compareTo(src);
		}
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#getRowKey()
	 */
	@Override
	public String getRowKey() {
		if (StringUtils.isNull(this.rowKey)) {
			this.rowKey = UUID.randomUUID().toString();
		}
		return this.rowKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#setRowKey(java.lang.String)
	 */
	@Override
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#getCreatedUserId()
	 */
	@Override
	public Long getCreatedUserId() {
		return createdUserId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#setCreatedUserId(java.lang.String)
	 */
	@Override
	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#getCreatedTime()
	 */
	@Override
	public long getCreatedTime() {
		return createdTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#setCreatedTime(long)
	 */
	@Override
	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#getUpdatedUserId()
	 */
	@Override
	public Long getUpdatedUserId() {
		return updatedUserId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#setUpdatedUserId(java.lang.String)
	 */
	@Override
	public void setUpdatedUserId(Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#getUpdatedTime()
	 */
	@Override
	public long getUpdatedTime() {
		return updatedTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.hbase.BaseBean#setUpdatedTime(long)
	 */
	@Override
	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.neo4j.Neo4jBaseBean#isDeleted()
	 */
	@Override
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean deleted) {
		this.isDeleted = deleted;
	}

	public boolean isNew() {
		return this.id == null || this.id.longValue() < 1;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		BaseBeanImpl bean = (BaseBeanImpl) obj;

		return bean.getId().equals(this.getId());
	}
}
