package com.yt.business.repository.neo4j;

import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

import com.yt.business.bean.UserAccountBean;
import com.yt.business.bean.UserProfileBean;

@QueryResult
public class UserTuple {
	@ResultColumn("account")
	private UserAccountBean account;

	@ResultColumn("profile")
	private UserProfileBean profile;
	
	public UserTuple(){
	}

	public UserAccountBean getAccount() {
		account.setProfile(this.profile);
		return account;
	}

	public void setAccount(UserAccountBean account) {
		this.account = account;
	}

	public UserProfileBean getProfile() {
		return profile;
	}

	public void setProfile(UserProfileBean profile) {
		this.profile = profile;
	}

	
	
}