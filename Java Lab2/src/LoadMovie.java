import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JFileChooser;

public class LoadMovie {

	private JFileChooser _FileChooser = new JFileChooser();
	private StringBuilder _StringBuilder = new StringBuilder();

	private BufferedReader _BufferedReader = null;
	private String _Line = "";
	private String _CvsSpliter = ",";
	private Movie newMovie = new Movie();

	public void LoadExam() throws Exception {

		if (_FileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			java.io.File file = _FileChooser.getSelectedFile();
			_BufferedReader = new BufferedReader(new FileReader(file));
			while ((_Line = _BufferedReader.readLine()) != null) {
				String[] moviee = _Line.split(_CvsSpliter);
				newMovie.setTitle(moviee[0]);
				newMovie.set_Author(moviee[1]);
				newMovie.set_Price(Float.parseFloat(moviee[2]));

			}
		} else {

			_StringBuilder.append("No file was selected");
		}
	}

}
