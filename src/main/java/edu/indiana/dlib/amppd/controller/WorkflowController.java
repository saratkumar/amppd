package edu.indiana.dlib.amppd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.jmchilton.blend4j.galaxy.beans.Workflow;
import com.github.jmchilton.blend4j.galaxy.beans.WorkflowDetails;

import edu.indiana.dlib.amppd.exception.GalaxyWorkflowException;
import edu.indiana.dlib.amppd.service.WorkflowService;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for REST operations on Workflow.
 * @author yingfeng
 */
//@CrossOrigin(origins = "*")
@RestController
@Slf4j
public class WorkflowController {

	@Autowired
	private WorkflowService workflowService;
	
	/**
	 * List all workflows currently existing in Galaxy.
	 * @return a list of workflows with name, ID, and URL.
	 */
	@GetMapping("/workflows")
	public List<Workflow> listWorkflows(
			@RequestParam(required = false) Boolean showPublished, 
			@RequestParam(required = false) Boolean showHidden, 
			@RequestParam(required = false) Boolean showDeleted) {	
		List<Workflow> workflows = null;
	
		try {
			String published = showPublished == null ? "null" : showPublished.toString(); 
			String hidden = showHidden == null ? "null" : showHidden.toString(); 
			String deleted = showDeleted == null ? "null" : showDeleted.toString(); 
			log.info("Listing workflows in Galaxy, showPublished = " + published + ", showHidden = " + hidden + ", showDeleted = " + deleted);
			workflows = workflowService.listWorkflows(showPublished, showHidden, showDeleted);
		}
		catch (Exception e) {
			String msg = "Unable to retrieve workflows from Galaxy.";
			log.error(msg);
			throw new GalaxyWorkflowException(msg, e);
		}
		
		return workflows;
	}
	
	/**
	 * Show details of a workflow based on information retrieved from Galaxy.
	 * Note: Set instance to true if the workflow ID is returned from invocation listing, in which case it's likely not a StoredWorkflow ID.
	 * @param workflowId ID of the queried workflow
	 * @param instance true if fetch by Workflow ID instead of StoredWorkflow id, false by default
	 * @param includeToolName include tool name in the workflow details if true, true by default
	 * @return all the details information of the queried workflow
	 */
	@GetMapping("/workflows/{workflowId}")
	public WorkflowDetails showWorkflow(
			@PathVariable("workflowId") String workflowId, 
			@RequestParam(required = false) Boolean instance,
			@RequestParam(required = false) Boolean includeToolName) {	
		WorkflowDetails workflow = null;
	
		try {
			log.info("Retrieving workflow details with ID: " +  workflowId + ", instance: " + instance + ", includeToolName: " + includeToolName);
			workflow = workflowService.showWorkflow(workflowId, instance, includeToolName);
		}
		catch (Exception e) {
			throw new GalaxyWorkflowException("Unable to retrieve workflow details with ID " + workflowId + " from Galaxy.", e);
		}

		return workflow;
	}
	
	
}
