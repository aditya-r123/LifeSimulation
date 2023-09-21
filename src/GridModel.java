import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is a class the can be used to store a 2D array of objects of any type. GridListeners that are
 * added to the list of listeners will be notified whenever changes are made to the 2D array.
 * @author Ted_McLeod
 *
 * @param <T> the type of objects stored in the 2D array
 */
public class GridModel<T> {
	
	/** The 2D array holding the model data */
	private T[][] grid;
	
	/** The list of listeners to notify when the grid data changes */
	private ArrayList<GridListener<T>> listeners;
	
	/** a message to pass when throwing an exception due to trying to pass null grid data */
	private static final String GRID_NULL_MSG = "grid cannot be null.";
	
	/**
	 * Constructs a grid initialized with the given grid data. To ensure that only this class
	 * can make any further changes to the grid, the data is copied into a new 2D array.
	 * @param grid the 2d array of data to initialize the grid to.
	 * @throws IllegalArgumentException if the gridData is null
	 */
	public GridModel(T[][] gridData) {
		if (gridData == null) throw new IllegalArgumentException(GRID_NULL_MSG);
		this.grid = copy2DArr(gridData);
		this.listeners = new ArrayList<>();
	}
	
	/**
	 * Returns a copy of the given 2D array that holds all the same objects.
	 * @param arr the 2D array to copy
	 * @return a copy of the given 2D array that holds all the same objects.
	 */
	@SuppressWarnings("unchecked")
	private <E> E[][] copy2DArr(E[][] arr) {
		if (arr == null) return null;
		if (arr.length == 0) return (E[][]) new Object[0][0];
		E[][] copy = (E[][]) new Object[arr.length][arr[0].length];
		for (int r = 0; r < arr.length; r++) {
			System.arraycopy(arr[r], 0, copy[r], 0, arr[r].length);
		}
		return copy;
	}
	
	/**
	 * Add a listener that should be notified whenever the grid changes.
	 * @param l the listener to add
	 */
	public void addListener(GridListener<T> l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	/**
	 * Removes a listener from the list of listeners.
	 * @param l the listener to remove
	 */
	public void removeListener(GridListener<T> l) {
		listeners.remove(l);
	}
	
	/**
	 * Returns the number of rows in the grid.
	 * @return the number of rows in the grid.
	 */
	public int getNumRows() {
		return this.grid.length;
	}
	
	/**
	 * Returns the number of columns in the grid.
	 * @return the number of columns in the grid.
	 */
	public int getNumCols() {
		return this.grid.length > 0 ? this.grid[0].length : 0;
	}
	
	/**
	 * Returns the object at (row, col) in the grid.
	 * @param row the row
	 * @param col the column
	 * @return the object at (row, col) in the grid.
	 */
	public T getValueAt(int row, int col) {
		return this.grid[row][col];
	}
	
	/**
	 * Sets the value at the given (row, col) and calls cellChanged() on each listener if the value
	 * is not equal according to the equals() method or if it changed from null to a non-null value.
	 * @param row the row
	 * @param col the column
	 * @param val the new value
	 */
	public void setValueAt(int row, int col, T val) {
		T oldVal = this.grid[row][col];
		this.grid[row][col] = val;
		if (oldVal == null && val != null || oldVal != null && !oldVal.equals(val)) {
			for (GridListener<T> l : listeners) {
				l.cellChanged(row, col, oldVal, val);
			}
		}
	}

	/**
	 * Replaces the 2D array with a completely new 2D array and calls gridReplaced() on each listener.
	 * @param grid the new 2D array
	 * @throws IllegalArgumentException if the gridData is null
	 */
	public void setGrid(T[][] grid) {
		if (grid == null) throw new IllegalArgumentException(GRID_NULL_MSG);
		this.grid = copy2DArr(grid);
		for (GridListener<T> l : listeners) {
			l.gridReplaced();
		}
	}

	/**
	 * Returns a string representation of this grid along with a list of listeners.
	 */
	@Override
	public String toString() {
		return "GridModel [grid=" + Arrays.toString(grid) + ", listeners=" + listeners + "]";
	}
	
}
