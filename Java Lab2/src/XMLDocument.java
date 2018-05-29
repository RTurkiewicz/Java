import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public class XMLDocument {
	/**
	 * Definiuje interfejst API, kt�ry umo�liwia aplikacji uzyskiwanie paresra
	 * generuj�cego obiakty DOM z dokument�w XML.
	 */
	private DocumentBuilderFactory _documentBuilderFactory;
	/**
	 * Umo�liwia dost�p do dokumentu XML.
	 */
	private DocumentBuilder _documentBuilder;
	/**
	 * Zapewnia podstawowy dostep do plik�w HMTL oraz XML.
	 */
	private Document _document;
	/**
	 * TransformerFactory u�ywany jest tworzenia obiekt�w Transformer i Templates.
	 */
	private TransformerFactory _transformerFactory;
	/**
	 * Transformuje drzewo zr�d�owe w drzewo wynikowe.
	 */
	private Transformer _transformer;
	/**
	 * Mdel dokumentu (DOM).
	 */
	private DOMSource _source;
	/**
	 * Przeksztafca na zywk�y tekst z takich pli�w jak HTML oraz XML.
	 */
	private StreamResult _result;
	/**
	 * �cie�ka dost�pu do pliku xml.
	 */
	private String _path = "C:\\Users\\Argon\\Desktop\\JavaLab2\\Java(Lab2)\\xml";
	/**
	 * G��wny element dokumentu xml.
	 */
	private Element _rootElement;

	public XMLDocument() throws Exception {
	}


	public void CreateNewXmlFIle() {

		try {

			_documentBuilderFactory = DocumentBuilderFactory.newInstance();
			_documentBuilder = _documentBuilderFactory.newDocumentBuilder();
			_document = _documentBuilder.newDocument();

			_rootElement = _document.createElement("Movies");
			_document.appendChild(_rootElement);

			// write the content into xml file
			_transformerFactory = TransformerFactory.newInstance();
			_transformer = _transformerFactory.newTransformer();
			_source = new DOMSource(_document);
			_result = new StreamResult(new File(_path + "Movies.xml"));
			_transformer.transform(_source, _result);

			// Output to console for testing
			StreamResult consoleResult = new StreamResult(System.out);
			_transformer.transform(_source, consoleResult);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Dodaje film do pliku Movie.xml na podsatwie wpisanych danych z aplikacji.
	 * 
	 * @param document
	 *            Dokument
	 * @param rootElement
	 *            G��wny element pliku xml
	 * @param titleValue
	 *            Tytu�
	 * @param directorValue
	 *            director
	 * @param premiereValue
	 *            Data premiery
	 * @param priceValue
	 *            Cena
	 * @param picturePathValue
	 *            �cie�ka dos�pu do zdj�cia z ok��dk� ksia�ki
	 * @throws ParserConfigurationException
	 *             Wskazuje powa�ny b��d konfiguracji.
	 * @throws TransformerException
	 *             okre�la stan, kt�ry wyst�pi� podczas procesu transformacji.
	 * @throws SAXException
	 *             Sprawdza poprawno�� sk��dni XML
	 * @throws IOException
	 *             Sygnalizuje, �e wyst�pi� wyj�tek We / Wy.
	 */
	public void AddDataToXml(Document document, Element rootElement, String titleValue, String directorValue,
			String premiereValue, String priceValue, String picturePathValue)
			throws ParserConfigurationException, TransformerException, SAXException, IOException {

		_documentBuilderFactory = DocumentBuilderFactory.newInstance();
		_documentBuilder = getDocumentBuilderFactory().newDocumentBuilder();
		document = _documentBuilder.parse(_path + "Movies.xml");

		rootElement = document.getDocumentElement();

		Element movie = document.createElement("Movie");

		Attr title = document.createAttribute("Title");
		title.setValue(titleValue);
		movie.setAttributeNode(title);

		Attr director = document.createAttribute("Director");
		director.setValue(directorValue);
		movie.setAttributeNode(director);

		Attr premiere = document.createAttribute("Premiere");
		premiere.setValue(premiereValue);
		movie.setAttributeNode(premiere);

		Attr price = document.createAttribute("Price");
		price.setValue(priceValue);
		movie.setAttributeNode(price);

		Attr picturePath = document.createAttribute("Picture");
		picturePath.setValue(picturePathValue);
		movie.setAttributeNode(picturePath);

		rootElement.appendChild(movie);

		// write the content into xml file
		_transformerFactory = TransformerFactory.newInstance();
		_transformer = _transformerFactory.newTransformer();
		_source = new DOMSource(document);
		_result = new StreamResult(_path + "Movies.xml");
		_transformer.transform(_source, _result);

		// Output to console for testing
		StreamResult consoleResult = new StreamResult(System.out);
		_transformer.transform(_source, consoleResult);

	}

	/**
	 * Sprawdza czy istnieje w katalogu xml plik o nazwie Movies.xml
	 * 
	 * @return true if exists / false if not
	 */
	public Boolean CheckIfFileExists() {

		if (new File(_path + "Movies.xml").exists()) {
			System.out.println("File existed");
			return true;
		} else {
			return false;
		}

	}

	public void set_DocumentBuilderFactory(DocumentBuilderFactory documentBuilderFactory) {
		this._documentBuilderFactory = documentBuilderFactory;
	}

	public DocumentBuilderFactory getDocumentBuilderFactory() {
		return _documentBuilderFactory;
	}

	public void set_Document(Document document) {
		this._document = document;
	}

	public Document get_Document() {
		return _document;
	}

	public void set_RotElement(Element rootElement) {
		this._rootElement = rootElement;
	}

	public Element get_RotElement() {
		return _rootElement;
	}

	public void set_Path(String path) {
		this._path = path;
	}

	public String get_Path() {
		return _path;
	}

}
