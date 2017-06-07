import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class Main extends JFrame {

	public Main()  {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		setSize(1000, 1000);
		
		SinglePageView spv = new SinglePageView();
		
		add(spv, BorderLayout.CENTER);
		
		ArrayList<String> files = new ArrayList<String>();
		
		try {
			
			Files.list(Paths.get("/Users/Martin/Dropbox/University/Teaching Fellow/2016-17/Teaching/4CCS1PPA/Semester 2/Assignments/Coursework X/ppa-major-coursework-2017/ppa-major-coursework-2017/marking/student_data/submitted_reports")).forEach(filePath -> {
			    
				if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".pdf")) {
			    
			    	files.add(filePath.toString());
			
			    }
		
			});

		} catch (IOException e) {

			e.printStackTrace();
		}
		
		markingControls(spv, files);
		
	}
	
	private Hashtable<String, Integer> scores;
	
	private void markingControls(SinglePageView spv, ArrayList<String> files) {
		
		scores = new Hashtable<String, Integer>();
		
		JPanel markingControls = new JPanel();
		
		markingControls.setLayout(new GridLayout(1, 0));
		
		//
		
		/*JButton next = new JButton("Next");
		
		markingControls.add(next, BorderLayout.EAST);
		
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				spv.openPDFFile(new File(files.remove(0)));
				
			}
			
		});*/
		
		//
		
		JList author = new JList();
		
		author.setPreferredSize(new Dimension(50, 100));
		
		DefaultListModel<String> authorModel = new DefaultListModel<String>();
		
		author.setModel(authorModel);
		
		markingControls.add(author);
		
		//
		
		ActionListener mark = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JButton pressed = ((JButton)(e.getSource()));
				
				if ( !scores.containsKey(pressed.getName().split("_")[0])) {
				
					scores.put(pressed.getName().split("_")[0], Integer.parseInt(pressed.getName().split("_")[1]));
					
					Main.this.setTitle(Main.this.getTitle() + " " + pressed.getName());
					
				}
				
				if ( scores.size() == 3 ) {
					
					String toWrite = author.getSelectedValue() + ",";
					
					int totalScore = 0;
					
					for ( Entry<String, Integer> entry : scores.entrySet() ) {
						
						toWrite += entry.getKey() + "," + entry.getValue() + ",";
						
						totalScore += entry.getValue();
						
					}
					
					toWrite += "total," + totalScore + "\n";
					
					try {
						
						Files.write(Paths.get("/Users/Martin/Dropbox/University/Teaching Fellow/2016-17/Teaching/4CCS1PPA/Semester 2/Assignments/Coursework X/ppa-major-coursework-2017/ppa-major-coursework-2017/marking/student_data/report-marks.csv"), new ArrayList<String>(Arrays.asList(new String[] {toWrite})), StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
						
						Files.write(Paths.get("/Users/Martin/Dropbox/University/Teaching Fellow/2016-17/Teaching/4CCS1PPA/Semester 2/Assignments/Coursework X/ppa-major-coursework-2017/ppa-major-coursework-2017/marking/student_data/report-marks-backup.csv"), new ArrayList<String>(Arrays.asList(new String[] {toWrite})), StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
						
						
					} catch (IOException e1) {
					
						e1.printStackTrace();
					
					}
					
					String moveFile = files.remove(0);
					
					// System.out.println(moveFile.replace(moveFile.split("/")[moveFile.split("/").length - 1], "/Complete/" + moveFile.split("/")[moveFile.split("/").length - 1]));
					
					try {
					
						Files.move(Paths.get(moveFile), Paths.get(moveFile.replace(moveFile.split("/")[moveFile.split("/").length - 1], "complete/" + moveFile.split("/")[moveFile.split("/").length - 1])));
					
					} catch (IOException e1) {
					
						e1.printStackTrace();
					
					}
					
					Main.this.setTitle("");
					
					next(spv, files, author, authorModel);
					
				}
				
			}
			
		};
		
		//
		
		JPanel proseQuality = new JPanel(); 
		
		proseQuality.setLayout(new GridLayout(0, 1));
		
		proseQuality.add(new JLabel("Prose Quality"));
		
		JButton prose_5 = new JButton();
		prose_5.setName("prose_5");
		prose_5.addActionListener(mark);
		proseQuality.add(prose_5);
		prose_5.setText(":>");
		
		JButton prose_4 = new JButton();
		prose_4.setName("prose_4");
		prose_4.addActionListener(mark);
		proseQuality.add(prose_4);
		prose_4.setText(":)");
		
		JButton prose_3 = new JButton();
		prose_3.setName("prose_3");
		prose_3.addActionListener(mark);
		proseQuality.add(prose_3);
		prose_3.setText(":|");
		
		JButton prose_2 = new JButton();
		prose_2.setName("prose_2");
		prose_2.addActionListener(mark);
		proseQuality.add(prose_2);
		prose_2.setText(":(");
		
		JButton prose_1 = new JButton();
		prose_1.setName("prose_1");
		prose_1.addActionListener(mark);
		proseQuality.add(prose_1);
		prose_1.setText(":<");
		
		JButton prose_0 = new JButton();
		prose_0.setName("prose_0");
		prose_0.addActionListener(mark);
		proseQuality.add(prose_0);
		prose_0.setText("Nothing");
		
		markingControls.add(proseQuality);
		
		//
		
		JPanel numberOfDiagrams = new JPanel(); 
		
		numberOfDiagrams.setLayout(new GridLayout(0, 1));
		
		numberOfDiagrams.add(new JLabel("Diagrams"));
		
		JButton diagrams_5 = new JButton();
		diagrams_5.setName("diagrams_5");
		diagrams_5.addActionListener(mark);
		numberOfDiagrams.add(diagrams_5);
		diagrams_5.setText("4");
		
		JButton diagrams_4 = new JButton();
		diagrams_4.setName("diagrams_4");
		diagrams_4.addActionListener(mark);
		numberOfDiagrams.add(diagrams_4);
		diagrams_4.setText("3");
		
		JButton diagrams_3 = new JButton();
		diagrams_3.setName("diagrams_3");
		diagrams_3.addActionListener(mark);
		numberOfDiagrams.add(diagrams_3);
		diagrams_3.setText("2");
		
		JButton diagrams_2 = new JButton();
		diagrams_2.setName("diagrams_2");
		diagrams_2.addActionListener(mark);
		numberOfDiagrams.add(diagrams_2);
		diagrams_2.setText("1");
		
		JButton diagrams_1 = new JButton();
		diagrams_1.setName("diagrams_1");
		diagrams_1.addActionListener(mark);
		numberOfDiagrams.add(diagrams_1);
		diagrams_1.setText("0");
		
		JButton diagrams_0 = new JButton();
		diagrams_0.setName("diagrams_0");
		diagrams_0.addActionListener(mark);
		numberOfDiagrams.add(diagrams_0);
		diagrams_0.setText("Nothing");
		
		markingControls.add(numberOfDiagrams);
		
		//
		
		JPanel diagramText = new JPanel(); 
		
		diagramText.setLayout(new GridLayout(0, 1));
		
		diagramText.add(new JLabel("Diagram Description"));
		
		JButton diagramText_5 = new JButton();
		diagramText_5.setName("diagramText_5");
		diagramText_5.addActionListener(mark);
		diagramText.add(diagramText_5);
		diagramText_5.setText("Critique");
		
		JButton diagramText_4 = new JButton();
		diagramText_4.setName("diagramText_4");
		diagramText_4.addActionListener(mark);
		diagramText.add(diagramText_4);
		diagramText_4.setText("Why");
		
		JButton diagramText_3 = new JButton();
		diagramText_3.setName("diagramText_3");
		diagramText_3.addActionListener(mark);
		diagramText.add(diagramText_3);
		diagramText_3.setText("How");
		
		JButton diagramText_2 = new JButton();
		diagramText_2.setName("diagramText_2");
		diagramText_2.addActionListener(mark);
		diagramText.add(diagramText_2);
		diagramText_2.setText("Description");
		
		JButton diagramText_1 = new JButton();
		diagramText_1.setName("diagramText_1");
		diagramText_1.addActionListener(mark);
		diagramText.add(diagramText_1);
		diagramText_1.setText("Something");
		
		JButton diagramText_0 = new JButton();
		diagramText_0.setName("diagramText_0");
		diagramText_0.addActionListener(mark);
		diagramText.add(diagramText_0);
		diagramText_0.setText("Nothing");
		
		markingControls.add(diagramText);
		
		//
		
		add(markingControls, BorderLayout.EAST);
		
		next(spv, files, author, authorModel);
		
	}
	
	private void next(SinglePageView spv, ArrayList<String> files, JList authorList, DefaultListModel authorModel) {
		
		String file = files.get(0);
		
		spv.openPDFFile(new File(file));
		
		authorModel.clear();
		
		scores.clear();
		
		for ( String author : fileNameToStudent(file.split("/")[file.split("/").length - 1]) ) {
			
			authorModel.addElement(author);
			
		}
		
		if ( authorModel.size() > 1 ) super.setTitle("MULTIPLE potential authors ");
		
		authorList.setSelectedIndex(0);
		
	}
	
	private ArrayList<String> fileNameToStudent(String filename) {
		
		ArrayList<String> potentials = new ArrayList<String>();
		
		try ( BufferedReader reader = Files.newBufferedReader(Paths.get("/Users/Martin/Dropbox/University/Teaching Fellow/2016-17/Teaching/4CCS1PPA/Semester 2/Assignments/Coursework X/ppa-major-coursework-2017/ppa-major-coursework-2017/marking/student_data/students.csv")) ) {
			
			for ( String line : reader.lines().collect( Collectors.toList() )) {
				
				String firstname = line.split(",")[3].toLowerCase();
				
				String surname = line.split(",")[4].toLowerCase();
				
				String studentNumber = line.split(",")[0].toLowerCase();
				
				filename = filename.toLowerCase();
				
				// 
				
				if ( line.contains("First name") ) continue;
				
				if ( filename.contains(firstname) || filename.contains(surname) || filename.contains(studentNumber) ) {
					
					potentials.add(firstname + "," + surname + "," + studentNumber);
					
				}
				
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		
		}
		
		if ( potentials.size() == 0 ) { 
		
			System.out.println(filename + ": No record.");
			
			System.exit(0); 
			
		}
		
		return potentials;
		
	}
	
	public static void main(String[] args) {
		
		new Main().setVisible(true);
	}

}
