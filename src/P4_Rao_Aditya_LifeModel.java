import java.util.ArrayList;



public class P4_Rao_Aditya_LifeModel extends GridModel<Boolean>{
	ArrayList<GenerationListener> gl;
	public P4_Rao_Aditya_LifeModel(Boolean[][] gridData) {
		super(gridData);
		gl = new ArrayList<GenerationListener>();
	}
	private int curGen;
	

	public void nextGeneration() {
		boolean[][] temp = new boolean[getNumRows()][getNumCols()];
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				temp[i][j] = getValueAt(i, j);
			}
		}
		int count = 0;
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				for (int k = 0; k < 8; k++) {
					try {
						if (k == 0) {
							if (getValueAt(i - 1, j) == true) {
								count++;
							}
						} else if (k == 1) {
							if (getValueAt(i - 1, j-1) == true) {
								count++;
							}
						} else if (k == 2) {
							if (getValueAt(i - 1, j+1) == true) {
								count++;
							}
						} else if (k == 3) {
							if (getValueAt(i + 1, j) == true) {
								count++;
							}
						} else if (k == 4) {
							if (getValueAt(i + 1, j - 1) == true) {
								count++;
							}
						} else if (k == 5) {
							if (getValueAt(i + 1, j + 1) == true) {
								count++;
							}
						} else if (k == 6) {
							if (getValueAt(i, j + 1) == true) {
								count++;
							}
						} else {
							if (getValueAt(i, j - 1) == true) {
								count++;
							}
						}
					} catch (IndexOutOfBoundsException e) {
						continue;
					}
				}
				
				if (count <= 1 || count >= 4) {
					temp[i][j] = false;
				} else if (count == 3) {
					temp[i][j] = true;
				}
				count = 0;
			}
		}
		
		for (int l = 0; l < temp.length; l++) {
			for (int m = 0; m < temp.length; m++) {
				setValueAt(l, m, temp[l][m]);
			}
		}
		
		setGeneration(curGen + 1);
		
		
	}
	void addGenerationListener(GenerationListener l){
		gl.add(l);
	}
	
	void removeGenerationListener(GenerationListener l) {
		gl.remove(l);
	}
	
	void setGeneration(int gen){
		for(GenerationListener g : gl){
			
			g.generationChanged(gen-1, gen);
		}
		curGen = gen;
		
	}

	int getGeneration(){
		return curGen;
	}


}
