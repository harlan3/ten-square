package searchtool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;

import org.json.JSONObject;
import java.util.regex.Pattern;
import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;

public class SearchTool {

	private ClientMain clientMain = new ClientMain();
	private DatabaseAPI databaseAPI = new DatabaseAPI();
	private List list1;
	int pdfTextureIndex = 1;

	String pdfDir = "../processpdfs/splitpdfs/split_pdfs/";
	//String pdfDir = "../splitpdfs/split_pdfs";

	private String groupFormat(String inputStr) {

		boolean containsDash = inputStr.contains("_");
		String returnStr = inputStr.replaceAll("_", "").replaceAll(" ", "");

		if (!containsDash)
			returnStr += " ";

		return returnStr;
	}

	public void performSearch(String keyword) {

		java.util.List<Integer> pageMatches = databaseAPI.textSearch(pdfTextureIndex, keyword);
		list1.removeAll();

		for (Integer i : pageMatches) {

			Pattern p = Pattern.compile(
					"(\\w+)\\W+(\\w+)\\W+(\\w+)\\W+(\\w+)\\W+" + keyword + "\\W+(\\w+)\\W+(\\w+)\\W+(\\w+)\\W+(\\w+)");
			Matcher m = p.matcher("_ _ _ _ " + databaseAPI.retrievePageText(pdfTextureIndex, i) + " _ _ _ _");

			while (m.find()) {
				String pageStr = String.format("%1$3s  ", i);
				list1.add(pageStr + groupFormat(m.group(1)) + groupFormat(m.group(2)) + groupFormat(m.group(3))
						+ groupFormat(m.group(4)) + groupFormat(keyword) + groupFormat(m.group(5))
						+ groupFormat(m.group(6)) + groupFormat(m.group(7)) + groupFormat(m.group(8)));
			}
		}
	}

	public void setupDisplay() {

		Display display = new Display();
		Shell shell = new Shell(display, (SWT.CLOSE | SWT.MIN | SWT.TITLE));
		shell.setText("Ten-Square Search Tool");

		// create a new GridLayout with two columns
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);

		Label label1 = new Label(shell, SWT.NONE);
		label1.setText("PDF Texture Index:");
		GridData data5 = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		label1.setLayoutData(data5);

		File dir = new File(pdfDir);
		String items[] = dir.list();
		Arrays.sort(items);
		Combo combo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setItems(items);
		combo.select(0);
		GridData gridData1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		combo.setLayoutData(gridData1);
		Listener listener1 = new Listener() {
			public void handleEvent(Event event) {

				JSONObject jsonObject = new JSONObject();
				pdfTextureIndex = combo.getSelectionIndex() + 1;
				jsonObject.put("msgId", new Integer(1));
				jsonObject.put("newTextureIndex", new Integer(pdfTextureIndex));
				clientMain.sendUDPMessage(jsonObject.toString());
			}
		};
		combo.addListener(SWT.Selection, listener1);

		Label label2 = new Label(shell, SWT.NONE);
		label2.setText("PDF Search Keyword:");
		GridData gridData2 = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		label2.setLayoutData(gridData2);

		Text textBox = new Text(shell, SWT.BORDER);
		GridData gridData3 = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		textBox.setLayoutData(gridData3);

		Button button1 = new Button(shell, SWT.PUSH);
		button1.setText("PDF Search");
		GridData gridData4 = new GridData(SWT.CENTER, SWT.TOP, false, false, 2, 1);
		gridData4.widthHint = 150;
		button1.setLayoutData(gridData4);
		Listener listener2 = new Listener() {
			public void handleEvent(Event event) {
				performSearch(textBox.getText());
			}
		};
		button1.addListener(SWT.Selection, listener2);

		Slider slider1 = new Slider(shell, SWT.HORIZONTAL);
		slider1.setMaximum(110); // The 110 equates to a 100 value
		slider1.setMinimum(1);
		slider1.setIncrement(1);
		GridData gridData5 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gridData5.horizontalSpan = 2;
		gridData5.widthHint = 510;
		slider1.setLayoutData(gridData5);
		slider1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("msgId", new Integer(3));
				jsonObject.put("newZoomFactor", new Integer(slider1.getSelection()));
				clientMain.sendUDPMessage(jsonObject.toString());
			}
		});

		Label label3 = new Label(shell, SWT.NONE);
		label3.setText("Page");
		GridData gridData6 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		label3.setLayoutData(gridData6);

		Label label4 = new Label(shell, SWT.NONE);
		label4.setText("Search Results");
		GridData gridData7 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		label4.setLayoutData(gridData7);

		list1 = new List(shell, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL);
		GridData gridData8 = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData8.horizontalSpan = 2;
		gridData8.widthHint = 500;
		gridData8.heightHint = 700;
		list1.setLayoutData(gridData8);
		Listener listener4 = new Listener() {
			public void handleEvent(Event event) {

				// Ignore event from keypress
				if (list1.getSelectionIndex() < 0)
					return;
				
				JSONObject jsonObject = new JSONObject();
				String selection = list1.getItem(list1.getSelectionIndex());
				int selectedPage = Integer.parseInt(selection.substring(0, 3).trim());
				jsonObject.put("msgId", new Integer(2));
				jsonObject.put("newPageNum", new Integer(selectedPage));
				clientMain.sendUDPMessage(jsonObject.toString());
			}
		};
		list1.addListener(SWT.Selection, listener4);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
		clientMain.socket.close();
	}

	public static void main(String[] args) {

		SearchTool searchTool = new SearchTool();
		searchTool.databaseAPI.initDatabase();

		searchTool.setupDisplay();
	}
}
