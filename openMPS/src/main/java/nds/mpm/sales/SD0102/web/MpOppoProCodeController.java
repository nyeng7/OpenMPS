package nds.mpm.sales.SD0102.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nds.mpm.common.vo.PageSet;
import nds.mpm.common.vo.ResultEx;
import nds.mpm.common.web.Consts;
import nds.mpm.common.web.CorsFilter;
import nds.mpm.common.web.TMMBaseController;
import nds.mpm.login.vo.MPUserSession;
import nds.mpm.sales.SD0102.service.MpOppoProCodeService;
import nds.mpm.sales.SD0102.vo.MpOppoProCodeDefaultVO;
import nds.mpm.sales.SD0102.vo.MpOppoProCodeVO;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : MpOppoProCodeController.java
 * @Description : MpOppoProCode Controller class
 * @Modification Information
 *
 * @author dd
 * @since d
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
@RequestMapping("/mpm/{corpCode}/sd0102/mpcustprocode")
public class MpOppoProCodeController extends TMMBaseController {

	private static final Logger _logger = LoggerFactory.getLogger(MpOppoProCodeController.class);
	
	@Autowired
	protected CorsFilter		_filter;
	
    @Resource(name = "mpOppoProCodeService")
    private MpOppoProCodeService mpOppoProCodeService;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
	
    /**
	 * mp_oppo_pro_code 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 MpOppoProCodeDefaultVO
	 * @return "/mpOppoProCode/MpOppoProCodeList"
	 * @exception Exception
	 */
    @RequestMapping(value="")
    public ResponseEntity<ResultEx> selectMpOppoProCodeList(
    		@PathVariable("corpCode") String corpCode,
    		HttpServletRequest req,
    		HttpServletResponse res, 
    		ModelMap model)
            throws Exception {
    	
        ResultEx result = new ResultEx( Consts.ResultCode.RC_OK );
    	
        int pageIndex = Integer.parseInt(StringUtils.defaultIfEmpty(req.getParameter("pageIndex"),"0"));
    	int pageSize = Integer.parseInt(StringUtils.defaultIfEmpty(req.getParameter("pageSize"),"0"));
    	
    	String custCode = req.getParameter("custCode");
    	
    	MpOppoProCodeVO searchVO = new MpOppoProCodeVO();
    	searchVO.setCorpCode(corpCode);
    	searchVO.setCustCode(custCode);
    	
    	searchVO.setPageIndex(pageIndex);
    	searchVO.setPageSize(pageSize);
    	
        List<?> mpOppoProCodeList = mpOppoProCodeService.selectMpOppoProCodeList(searchVO);

    	PageSet pageSet = new PageSet();
        
        int totCnt = mpOppoProCodeService.selectMpOppoProCodeListTotCnt(searchVO);
        pageSet.setPageIndex(pageIndex);
    	pageSet.setPageSize(pageSize);
    	pageSet.setTotalRecordCount(totCnt);
    	pageSet.setResult(mpOppoProCodeList);
		
    	result.setExtraData(pageSet);
    	
    	return _filter.makeCORSEntity( result );
    } 
    
    @RequestMapping("/mpOppoProCode/addMpOppoProCodeView.do")
    public String addMpOppoProCodeView(
            @ModelAttribute("searchVO") MpOppoProCodeDefaultVO searchVO, Model model)
            throws Exception {
        model.addAttribute("mpOppoProCodeVO", new MpOppoProCodeVO());
        return "/mpOppoProCode/MpOppoProCodeRegister";
    }
    
    @RequestMapping(value="",method=RequestMethod.POST)
    public ResponseEntity<ResultEx> addMpOppoProCode(
    		@PathVariable("corpCode") String corpCode,
            @RequestBody List<EgovMap> mpOppoProCodeVOs,
            HttpServletRequest req)
            throws Exception {
    	
    	ResultEx result = new ResultEx( Consts.ResultCode.RC_OK );
    	
    	MPUserSession sess = getSession(req);
    	int nRet = 0;
    	
    	List ilist = new ArrayList();
    	//중복검색
    	if(mpOppoProCodeVOs != null)
    	{
    		for(EgovMap vo : mpOppoProCodeVOs)
        	{
    			vo.put("corpCode",corpCode);
    			if(sess != null)
    			{
    				vo.put("crUser", sess.getUser().getId());
    			}
        		ilist.add(vo);
        	}
    	}
    	
    	result = mpOppoProCodeService.insertMpOppoProCode(ilist);
        return _filter.makeCORSEntity( result );
    }
    
    @RequestMapping("/mpOppoProCode/updateMpOppoProCode.do")
    public String updateMpOppoProCode(
            MpOppoProCodeVO mpOppoProCodeVO,
            @ModelAttribute("searchVO") MpOppoProCodeDefaultVO searchVO, SessionStatus status)
            throws Exception {
        mpOppoProCodeService.updateMpOppoProCode(mpOppoProCodeVO);
        status.setComplete();
        return "forward:/mpOppoProCode/MpOppoProCodeList.do";
    }
    
    @RequestMapping("/mpOppoProCode/deleteMpOppoProCode.do")
    public String deleteMpOppoProCode(
            MpOppoProCodeVO mpOppoProCodeVO,
            @ModelAttribute("searchVO") MpOppoProCodeDefaultVO searchVO, SessionStatus status)
            throws Exception {
        mpOppoProCodeService.deleteMpOppoProCode(mpOppoProCodeVO);
        status.setComplete();
        return "forward:/mpOppoProCode/MpOppoProCodeList.do";
    }

}
