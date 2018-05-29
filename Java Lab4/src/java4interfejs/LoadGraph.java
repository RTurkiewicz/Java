package java4interfejs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

public class LoadGraph {

	private JFileChooser _FileChooser = new JFileChooser();
	private StringBuilder _StringBuilder = new StringBuilder();
	private int[][] tablica = new int[7][7];

	String contentOfQuestionText = "";
	String contentOfAnswerText = "";

	private BufferedReader _BufferedReader = null;
	private String _Line = "";
	private String _CvsSpliter = ",";

	public void GraphLoading() throws Exception {

		if (_FileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			java.io.File file = _FileChooser.getSelectedFile();
			_BufferedReader = new BufferedReader(new FileReader(file));
			int i = 0;
			while ((_Line = _BufferedReader.readLine()) != null) {
				String[] temp = _Line.split(_CvsSpliter);
				
				int j = 0;
				while ((_Line = _BufferedReader.readLine()) != null) {
					tablica[i][j] = Integer.parseInt(temp[j]);
					j++;
				}
				i++;
			}
			// input.close();
		} else {

			_StringBuilder.append("No file was selected");
		}
	}

	public int[][] getTablica() {
		return tablica;
	}

	public void setTablica(int[][] tablica) {
		this.tablica = tablica;
	}
	
}
