package com.collabnet.ccf.ccfmaster.controller.web;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionStatus;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.ArtifactDetail;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.RepositoryDetail;
import com.collabnet.ccf.ccfmaster.web.model.RepositoryMappingsModel;


@RequestMapping("/admin/**")
@Controller
public class LandscapeRepositoryMappingsController extends AbstractLandscapeController {


	@Autowired 
	public CCFRuntimePropertyHolder ccfRuntimePropertyHolder;
	private static final String RMDID = "rmdid";
	private static final Logger log = LoggerFactory.getLogger(LandscapeRepositoryMappingsController.class);
	private static final String FORWARD = "forward";
	
	/**
	* Controller method to display repository mapping directions for TF to Participant
	* 
	*/  
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART, method = RequestMethod.GET)
    public String displayrepositorymappingtftopart(
    		@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
    		Model model, HttpServletRequest request) {
		doList(Directions.FORWARD, page, size, model);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
	 }
    
	
	/**
	* Controller method to resume synchronization 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_RESUMESYNCHRONIZATION, method = RequestMethod.POST)
    public String  resumeSynchronization(@RequestParam(ControllerConstants.DIRECTION) String paramdirection, Model model, HttpServletRequest request) {
		 setStatusForRMDs(RepositoryMappingDirectionStatus.RUNNING, paramdirection, model, request);
		 if(paramdirection.equals(FORWARD)){
			 return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
		 }
		 else{
			 return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
		 }
    }


	public static void setStatusForRMDs(RepositoryMappingDirectionStatus status, String paramdirection, Model model,
			HttpServletRequest request) {
		 String[] items = request.getParameterValues(RMDID);
		 Directions directions = FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		 try{
			setStatusForRMDs(items, directions, status);
			String messageCode = (status == RepositoryMappingDirectionStatus.PAUSED) ? ControllerConstants.RMDPAUSESYNCSUCCESSMESSAGE : ControllerConstants.RMDRESUMESYNCSUCCESSMESSAGE;
			FlashMap.setSuccessMessage(messageCode);
		 }
		 catch(Exception exception){
			String messageCode = (status == RepositoryMappingDirectionStatus.PAUSED) ? ControllerConstants.RMDPAUSESYNCFAILUREMESSAGE : ControllerConstants.RMDRESUMESYNCFAILUREMESSAGE;
			FlashMap.setErrorMessage(messageCode, exception.getMessage());
		 }
	}
	
	
	private static List<RepositoryMappingDirection> setStatusForRMDs(String[] rmIds, Directions direction, RepositoryMappingDirectionStatus status) {
		if (rmIds == null) rmIds = new String[0];
		ArrayList<RepositoryMappingDirection> ret = new ArrayList<RepositoryMappingDirection>(rmIds.length);
		for (String rmId : rmIds) {
			RepositoryMapping repositoryMapping = RepositoryMapping.findRepositoryMapping(Long.valueOf(rmId));
			RepositoryMappingDirection repositoryMappingDirection = RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMappingAndDirection(repositoryMapping, direction).getSingleResult();
			repositoryMappingDirection.setStatus(status);
			ret.add(repositoryMappingDirection.merge());
		}
		return ret;
	}
	
	@InitBinder
	public void myInitBinder(WebDataBinder binder) {
		// do not bind these fields
		binder.setDisallowedFields(new String[] { "repositoryMappingDirection", });
	}
	
	/**
	* Controller method to pause synchronization 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_PAUSESYNCHRONIZATION, method = RequestMethod.POST)
    public String  pauseSynchronization(@RequestParam(ControllerConstants.DIRECTION) String paramdirection, Model model, HttpServletRequest request) {
		setStatusForRMDs(RepositoryMappingDirectionStatus.PAUSED, paramdirection, model, request);
		if (paramdirection.equals(FORWARD)) {
			return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
		} else {
			return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
		}
    }
	
	/**
	* Controller method to delete synchronization 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_DELETESYNCHRONIZATION, method = RequestMethod.POST)
    public String  deleteSynchronization(@RequestParam(ControllerConstants.DIRECTION) String paramdirection, Model model, HttpServletRequest request) {
		String[] items = request.getParameterValues(RMDID);
		if (items == null) 
			items = new String[0];
		Directions directions = FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		try{
			for (String rmId : items) {
				RepositoryMapping repositoryMapping = RepositoryMapping.findRepositoryMapping(Long.valueOf(rmId));
				RepositoryMappingDirection repositoryMappingDirection = RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMappingAndDirection(repositoryMapping, directions).getSingleResult();
				repositoryMappingDirection.remove();
			}
			FlashMap.setSuccessMessage(ControllerConstants.RMDDELETESUCCESSMESSAGE);
		} catch(Exception exception) {
			FlashMap.setErrorMessage(ControllerConstants.RMDDELETEFAILUREMESSAGE, exception.getMessage());
		}
		
		//List<RepositoryMappingDirection> rmds = RepositoryMappingDirection.findRepositoryMappingDirectionsByDirection(directions).getResultList();
		//populateModel(model, rmds);
		
		if(paramdirection.equals(FORWARD)){
			return "redirect:/" +UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
		} else {
			return "redirect:/" +UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
		}
    }
	
	/**
	* Controller method to display repository mapping directions for Participant to TF
	* 
	*/  
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF, method = RequestMethod.GET)
    public String displayrepositorymappingparttotf(
    		@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
    		Model model, HttpServletRequest request) {
		doList(Directions.REVERSE, page, size, model);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
	}


	/**
	 * @param direction
	 * @param page
	 * @param size
	 * @param model
	 */
	private void doList(Directions direction, Integer page, Integer size, Model model) {
		List<RepositoryMappingDirection> rmds = paginate(
				RepositoryMappingDirection.findRepositoryMappingDirectionsByDirection(direction),
				RepositoryMappingDirection.countRepositoryMappingDirectionsByDirection(direction),
				page, size, model)
			.getResultList();
		populateModel(model, rmds);
	}


	/**
	 * Helper method to populate Repository Mapping Direction Model
	 * @param model
	 * @param rmds
	 */
	private void populateModel(Model model, List<RepositoryMappingDirection> rmds) {
		String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
		List<RepositoryMappingsModel> rmmList = makeRepositoryMappingsModel(rmds, tfUrl);
		
		Landscape landscape=ControllerHelper.findLandscape(model);
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("landscape",landscape);
		model.addAttribute("selectedLink", "repositorymappings");
		model.addAttribute("repositoryMappingsModel", rmmList);
	}
	
	public static List<RepositoryMappingsModel> makeRepositoryMappingsModel(
			List<RepositoryMappingDirection> repositoryMappingDirectionList,
			String tfUrl) {
		List<RepositoryMappingsModel> rmmList = new ArrayList<RepositoryMappingsModel>(repositoryMappingDirectionList.size());

		for (RepositoryMappingDirection rmd: repositoryMappingDirectionList){
			RepositoryMappingsModel rmm = new RepositoryMappingsModel();
			rmm.setRepositoryMappingDirection(rmd);
			rmm.setTfUrl(tfUrl);
			rmm.setArtifatTFUrl(tfUrl);
			rmm.setHospitalCount(HospitalEntry.countHospitalEntrysByRepositoryMappingDirection(rmd));
			rmm.setIdentityMappingCount(IdentityMapping.countIdentityMappingsByRepositoryMapping(rmd.getRepositoryMapping()));
			Directions dir = rmd.getDirection();
			String tfRepoId = (dir == Directions.FORWARD) ? rmd.getSourceRepositoryId() : rmd.getTargetRepositoryId();
			RepositoryDetail repositoryDetail = RepositoryConnections.detailsFor(tfRepoId);
			ArtifactDetail artifactDetail=RepositoryConnections.detailsForArtifact(rmd.getLastSourceArtifactId());
			rmm.setArtifactDetail(artifactDetail);
			rmm.setRepositoryDetail(repositoryDetail);

			rmmList.add(rmm);
		
		}
		return rmmList;
	}	
	
}
