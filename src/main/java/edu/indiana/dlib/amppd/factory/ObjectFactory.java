package edu.indiana.dlib.amppd.factory;


import edu.indiana.dlib.amppd.model.Bundle;
import edu.indiana.dlib.amppd.model.Collection;
import edu.indiana.dlib.amppd.model.CollectionSupplement;
import edu.indiana.dlib.amppd.model.Item;
import edu.indiana.dlib.amppd.model.ItemSupplement;
import edu.indiana.dlib.amppd.model.Job;
import edu.indiana.dlib.amppd.model.JobMgmMode;
import edu.indiana.dlib.amppd.model.Mgm;
import edu.indiana.dlib.amppd.model.MgmMode;
import edu.indiana.dlib.amppd.model.MgmModeInput;
import edu.indiana.dlib.amppd.model.MgmModeOutput;
import edu.indiana.dlib.amppd.model.Primaryfile;
import edu.indiana.dlib.amppd.model.PrimaryfileSupplement;
import edu.indiana.dlib.amppd.model.RouteLink;
import edu.indiana.dlib.amppd.model.Unit;
import edu.indiana.dlib.amppd.model.Workflow;


public class ObjectFactory implements BaseObjectFactory{

	@Override
	public Object createModelObject(String type) {
		// TODO Auto-generated method stub
		Object obj;
		switch(type.toLowerCase())
		{
			case "bundle":
				obj = new Bundle();
				break;
				
			case "collection":
				obj = new Collection();
				break;
			
			case "collectionsupplement":
				obj = new CollectionSupplement();
				break;
				
			case "item":
				obj = new Item();
				break;
				
			case "itemsupplement":
				obj = new ItemSupplement();
				break;
			
			case "job":
				obj = new Job();
				break;
				
			case "jobmgmmode":
				obj = new JobMgmMode();
				break;
				
			case "mgm":
				obj = new Mgm();
				break;
				
			case "mgmmode":
				obj = new MgmMode();
				break;
				
			case "mgmmodeinput":
				obj = new MgmModeInput();
				break;
			
			case "mgmmodeoutput":
				obj = new MgmModeOutput();
				break;
				
		/*
		 * case "mgmmodeio": obj = new MgmModeIo(); break;
		 */
			case "primaryfile":
				obj = new Primaryfile();
				break;
				
			case "primaryfilesupplement":
				obj = new PrimaryfileSupplement();
				break;
				
			case "routelink":
				obj = new RouteLink();
				break;
				
		/*
		 * case "supplement": obj = new Supplement(); break;
		 */
				
			case "unit":
				obj = new Unit();
				break;
				
			case "workflow":
				obj = new Workflow();
				break;
			default:
				throw new IllegalArgumentException("Could not identify Object type");
		
		}
		return obj;
	}

	
	
}
