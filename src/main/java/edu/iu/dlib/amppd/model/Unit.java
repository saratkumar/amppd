package edu.iu.dlib.amppd.model;

import java.util.ArrayList;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Organization unit that owns collections and workflows.
 * @author yingfeng
 *
 */
@Entity
@Getter @Setter @NoArgsConstructor
public class Unit extends Content {
	
    private ArrayList<Collection> collections;
    private ArrayList<Workflow> workflows;
}
