package com.yt.business.repository;

import java.util.List;

import com.yt.business.bean.PlaceBean;
import com.yt.rsal.neo4j.repository.ICrudOperate;

public interface PlaceRepository extends ICrudOperate {
	public PlaceBean getPlaceByGraphId(Long graphId) throws Exception;

	public List<PlaceBean> getAllRootPlaces() throws Exception;

	public List<PlaceBean> getAllSubPlaces(long graphId) throws Exception;

	public void save(long parentId, PlaceBean place, String operator)
			throws Exception;
}
