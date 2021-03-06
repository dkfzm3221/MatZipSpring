package com.koreait.matzip.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.user.model.UserDMI;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Mapper
public interface UserMapper {
	public int insUser(UserVO param);
	public int insFavorite(UserPARAM param);
	
	
	public UserDMI selUser(UserPARAM param);
	List<UserDMI> selFavoriteList(UserPARAM param);
	
	public int delFavorite(UserPARAM param);

}
