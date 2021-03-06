package com.yt.business.bean;

import org.springframework.data.neo4j.annotation.NodeEntity;

import com.yt.business.SocialBeanImpl;
import com.yt.business.common.Constants;
import com.yt.business.common.Constants.AlongIntentionType;
import com.yt.hbase.annotation.HbaseColumn;
import com.yt.hbase.annotation.HbaseTable;
import com.yt.neo4j.annotation.Neo4jRelationship;
import com.yt.neo4j.annotation.Neo4jRelationship.Direction;

/**
 * 该实体定义了系统中的行程的结伴信息。结伴信息和行程以及结伴发布者之间的关系通过图状数据库Neo4j存储
 * 
 * @author Tony.Zhang
 * 
 */
@HbaseTable(name = "T_ROUTE_ALONG_INFO")
@NodeEntity
public class AlongBean extends SocialBeanImpl {
	private static final long serialVersionUID = -3433522673262851121L;

	@HbaseColumn(name = "name")
	private String title;

	@HbaseColumn(name = "img")
	private String imageUrls; // 图片

	@HbaseColumn(name = "itnt")
	private AlongIntentionType intention; // 结伴目的

	@HbaseColumn(name = "dline")
	// 截止期限
	private long deadLine;

	private int  num;

	@HbaseColumn(name = "rdesc")
	// 要求描述
	private String memo;

	@HbaseColumn(name = "lnla")
	// 经纬度
	private String longLat;

	@HbaseColumn(name = "addr")
	// 位置描述
	private String address;

	private int applyNum;  //报名人数

	@Neo4jRelationship(relationship = Constants.RELATION_TYPE_BELONG, type = UserProfileBean.class, direction = Direction.OUTGOING)
	private transient UserProfileBean publisher; // 结伴信息发布者信息

	@Neo4jRelationship(relationship = Constants.RELATION_TYPE_BELONG, type = RouteMainBean.class, direction = Direction.OUTGOING)
	private transient  RouteMainBean route;

	public AlongBean() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public AlongIntentionType getIntention() {
		return intention;
	}

	public void setIntention(AlongIntentionType intention) {
		this.intention = intention;
	}

	public long getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(long deadLine) {
		this.deadLine = deadLine;
	}

	public String getLongLat() {
		return longLat;
	}

	public void setLongLat(String longLat) {
		this.longLat = longLat;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(int applyNum) {
		this.applyNum = applyNum;
	}

	public UserProfileBean getPublisher() {
		return publisher;
	}

	public void setPublisher(UserProfileBean publisher) {
		this.publisher = publisher;
	}

	public RouteMainBean getRoute() {
		return route;
	}

	public void setRoute(RouteMainBean route) {
		this.route = route;
	}
}
