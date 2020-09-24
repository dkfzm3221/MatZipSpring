package com.koreait.matzip.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.Const;
import com.koreait.matzip.FileUtils;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestFile;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;

@Service
public class RestService {
	@Autowired
	public RestMapper mapper;

	@Autowired
	public CommonMapper cMapper;

	public List<RestDMI> selRestList(RestPARAM param) {
		return mapper.selRestList(param);
	}

	List<CodeVO> selCategoryList() {
		CodeVO param = new CodeVO();
		param.setI_m(1);

		return cMapper.selCodeList(param);
	}

	int insRest(RestPARAM param) {

		return mapper.insRest(param);
	}// 연결

	@Transactional
	public void delRestTran(RestPARAM param) {
		mapper.delRestRecMenu(param);
		mapper.delRestMenu(param);
		mapper.delRest(param);
	}

	public RestDMI selRest(RestPARAM param) {
		return mapper.selRest(param);
	}

	public int delRestRecMenu(RestPARAM param) {
		return mapper.delRestRecMenu(param);
	}

	public int delRestMenu(RestPARAM param) {
		return mapper.delRestMenu(param);
	}

	public int insRecMenus(MultipartHttpServletRequest mReq) {
		int i_user = SecurityUtils.getLoginUserPk(mReq.getSession());
		int i_rest = Integer.parseInt(mReq.getParameter("i_rest"));
		if(_authFail(i_rest, i_user)) {
			return Const.FAIL; // 장난질 금지 
		}
		
		
		List<MultipartFile> fileList = mReq.getFiles("menu_pic");
		String[] menuNmArr = mReq.getParameterValues("menu_nm");
		String[] menuPriceArr = mReq.getParameterValues("menu_price");

		// 저장위치
		String path = Const.realPath + "/resources/img/rest/" + i_rest + "/rec_menu/";
		List<RestRecMenuVO> list = new ArrayList();

		for (int i = 0; i < menuNmArr.length; i++) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);

			String menu_nm = menuNmArr[i];
			int menu_price = CommonUtils.parseStringToInt(menuPriceArr[i]);
			vo.setI_rest(i_rest);
			vo.setMenu_nm(menu_nm);
			vo.setMenu_price(menu_price);

			// 파일 각 저장
			MultipartFile mf = fileList.get(i);

			if (mf.isEmpty()) {
				continue;
			} // 파일이 없으면 스킵!

			String originFileNm = mf.getOriginalFilename();
			String ext = FileUtils.getExt(originFileNm);
			String saveFileNm = UUID.randomUUID() + ext;
			try {
				mf.transferTo(new File(path + saveFileNm));
				vo.setMenu_pic(saveFileNm);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		for (RestRecMenuVO vo : list) {
			mapper.insRestRecMenu(vo);
		}

		return i_rest;
	}
	
	
	
	
	
	public int delRecMenu(RestPARAM param, String realPath) {
		//파일 삭제
		List<RestRecMenuVO> list = mapper.selRestRecMenus(param);
		if(list.size() == 1) {
			RestRecMenuVO item = list.get(0);
			
			if(item.getMenu_pic() != null && !item.getMenu_pic().equals("")) { //이미지 있음 > 삭제
				File file = new File(realPath + item.getMenu_pic());
				if(file.exists()) {
					if(file.delete()) {
						return mapper.delRestRecMenu(param);
					}else {
						return 0;
					}
				}
			}
		}
		
		return mapper.delRestRecMenu(param);
	}
	
	
	public List<RestRecMenuVO> selRestRecMenus(RestPARAM param){
		return mapper.selRestRecMenus(param);
	}

	public int insRestMenu(RestFile param, int i_user) {
		if(_authFail(param.getI_rest(), i_user)) {
			return Const.FAIL;
		}
		
		String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/menu/";
		
		List<RestRecMenuVO> list = new ArrayList();
		
		for(MultipartFile mf : param.getMenu_pic()) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);
			
			String saveFileNm = FileUtils.saveFile(path, mf);
			vo.setMenu_pic(saveFileNm);
			vo.setI_rest(param.getI_rest());
			
		}
		for(RestRecMenuVO vo : list) {
			mapper.insRestMenu(vo);
		}
		return Const.SUCCESS;
	}
	
	
	
	private boolean _authFail(int i_rest, int i_user) {
		RestPARAM param = new RestPARAM();
		param.setI_rest(i_rest);
		
		RestDMI dbResult = mapper.selRest(param);
		if(dbResult == null || dbResult.getI_user() != i_user) {
			return true;
		}
		return false;
	}
	
	public List<RestRecMenuVO> selRestMenus(RestPARAM param){
		return mapper.selRestMenus(param);
	}

}

