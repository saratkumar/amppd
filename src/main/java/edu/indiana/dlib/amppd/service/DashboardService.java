package edu.indiana.dlib.amppd.service;

import java.util.List;

import edu.indiana.dlib.amppd.model.DashboardResult;
import edu.indiana.dlib.amppd.web.DashboardResponse;
import edu.indiana.dlib.amppd.web.DashboardSearchQuery;

public interface DashboardService {
	/***
	 * Get a list of all results to display in the workflow dashboard
	 * @return
	 */
	public DashboardResponse getDashboardResults(DashboardSearchQuery query);
	
	/***
	 * Get all final results for the specified primaryfile.
	 * @param primaryfileId ID of the specified primaryfile
	 * @return the list of final results for the specified primaryfile
	 */
	public List<DashboardResult> getFinalDashboardResults(Long primaryfileId);
	
	/**
	 * Adds initial record to the dashboard results in database
	 * @param workflowId
	 * @param workflowName
	 * @param primaryfileId
	 * @param historyId
	 */
	public void addDashboardResult(String workflowId, String workflowName, long primaryfileId, String historyId);	
	
	/**
	 * Refreshes the database. Should only be used for initial population of database or when new columns are added. 
	 * @return
	 */
	public List<DashboardResult> refreshAllDashboardResults();

	/**
	 * Sets the specified dashboardResult according to the specified final status
	 * @param dashboardResultId id of the specified dashboardResult
	 * @param isFinal the specified final status
	 * @return
	 */
	public boolean setResultIsFinal(long dashboardResultId, boolean isFinal);
}
