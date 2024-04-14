package entities;

import javafx.beans.property.SimpleStringProperty;
/**
 * 
 * @author Group7
 * this class  is to  organize the values in the  table view
 *
 */

public class personAddToTable {
	
	private SimpleStringProperty id;
	
	

/**
 * 
 * @return id
 */
	public String getId() {
		return id.get();
	}

/**
 * 
 * @param id
 */
	public void setId(String id) {
		this.id.set(id);
	}


/**
 * constructor 
 * @param fullName
 * @param id
 * @param Guide
 */
	public personAddToTable( SimpleStringProperty id) {
		
		this.id = id;
		
	}
}
