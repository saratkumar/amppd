package edu.indiana.dlib.amppd.web;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DashboardSearchQuery {
	private int pageNum;
	private int resultsPerPage;
	private String[] filterBySubmitters;
	private String[] filterByWorkflows;
	private String[] filterByItems;
	private String[] filterByFiles;
	private String[] filterBySteps;
	private GalaxyJobState[] filterByStatuses;
	private String[] filterBySearchTerm;	
	private DashboardSortRule sortRule;
	private List <Date> filterByDates;
	private boolean filterByFinal;
}
