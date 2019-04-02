package edu.iu.dlib.amppd.model;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RouteLink defines the from/to nodes as well as the correspondence between the outputs of the from node and the inputs of the to node in a workflow route graph. 
 * @author yingfeng
 *
 */
@Entity
@Getter @Setter @NoArgsConstructor
public class RouteLink {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;    
    private Long workflowId;
    private Long fromMgmModeId;
    private Long toMgmModeId;    
        
    private HashMap<Integer, Integer> mgmModeIoMap;    
    private MgmMode fromMgmMode;
    private MgmMode toMgmMode;
    
}

