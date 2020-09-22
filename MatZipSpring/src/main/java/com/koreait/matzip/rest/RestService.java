package com.koreait.matzip.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;


@Service
public class RestService {
	@Autowired
	public RestMapper mapper;
	
	@Autowired
	public CommonMapper cMapper;
	
	
	public List<RestDMI> selRestList(RestPARAM param){
		return mapper.selRestList(param);
	}
		
	
	
	List<CodeVO> selCategoryList() {
		CodeVO param = new CodeVO();
		param.setI_m(1);
		
		return cMapper.selCodeList(param);
	}
	
	
	int insRest(RestPARAM param) {
		
		return mapper.insRest(param);
	}//연결
	
	public RestDMI selRest(RestPARAM param) {
		return mapper.selRest(param);
	}
}
