package edu.indiana.dlib.amppd.web;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class WorkflowResultFilterValues {
	private List<String> submitters;
	private List<String> workflows;
	private List<String> collections;
	private List<String> units;
	private List<String> externalIds;
	private List<String> items;
	private List<String> files;
	private List<String> steps;
	private List<String> outputs;
	private List<GalaxyJobState> statuses;
	private List<String> searchTerms;
	private List<Date> dateFilter;
}
