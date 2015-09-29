package com.yt.business.repository;

import java.util.List;

import com.yt.business.CrudAllInOneOperate;
import com.yt.business.bean.SceneResourceBean;

public interface SceneRepository extends CrudAllInOneOperate {
	public SceneResourceBean getSceneByGraphId(Long graphId) throws Exception;

	public List<SceneResourceBean> getScenesByPage(int start, int limit)
			throws Exception;
}
