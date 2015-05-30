/**
 * An interface for all of the counterpoint species classes.
 * Contains all public functions.
 * 
 * @author Quinn Hannah-White
 * @date 5/30/15
 */

package edu.du.cs.quinn.CounterPoint;

import edu.du.cs.quinn.Music.*;

public interface CounterPoint {
	
	public void assembleLines();
	public Line getSopranoLine();
	public Line getAltoLine();
	public Line getBassLine();

}
