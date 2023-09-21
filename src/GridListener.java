
/**
 * GridListeners can add themselves as a listener to a GridModel to be notified of changes to the grid.
 * @author Ted_McLeod
 *
 * @param <T> the type of data stored in the GridModel
 */
public interface GridListener<T> {
	/**
	 * Called by a GridModel on each listener whenever a cell has been set to a new value
	 * @param row the row
	 * @param col the column
	 * @param oldVal the old value
	 * @param newVal the new value
	 */
	public void cellChanged(int row, int col, T oldVal, T newVal);
	
	/**
	 * Called by a GridModel on each listener whenever the grid is set to a new 2D array.
	 */
	public void gridReplaced();
}
