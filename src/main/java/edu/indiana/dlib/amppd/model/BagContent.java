package edu.indiana.dlib.amppd.model;

import java.util.Date;

import edu.indiana.dlib.amppd.web.GalaxyJobState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * BagContent contains information of a final output file generated by a step of a Galaxy workflow invocation.
 * BagContent is not persisted as it's a subset of the fields in DashboardResult. 
 * The relationship of OutputBag and BagContent is as follows:
 *   An OutputBag for a primaryfile includes all BagContents associated with that primaryfile;
 *   An OutputBag for an item includes all BagContents associated with the primaryfiles contained in the item;
 *   similarly for collection and unit OutputBag. 
 * @author yingfeng
 */
@Data
@EqualsAndHashCode
@ToString(callSuper=true, onlyExplicitlyIncluded=true)
public class BagContent {	    
	private Long id;			// the id in DashboardResult
	private Long primaryfileId;
	private String submitter;
	private Date date;
	private String workflowId;
	private String invocationId;
	private String stepId;
	private String outputId;	
	private String workflowName;
	private String workflowStep; // same as tool_id
	private String toolVersion;	 
	private String outputFile;
	private String outputType;
	private String outputUrl;	// this is not stored in DashboardResult but generated as {baseUrl}/dashboard/{id}/output for Dashboard
}
