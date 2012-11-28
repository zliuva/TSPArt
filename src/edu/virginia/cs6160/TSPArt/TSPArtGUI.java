package edu.virginia.cs6160.TSPArt;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

public class TSPArtGUI {

	private JFrame frmTspArtGenerator;
	private JLabel lblOriginal;
	private JLabel lblTspArt;
	private BufferedImage originalImage;
	private BufferedImage resultImage;
	private JMenuItem mntmSave;
	private JMenuItem chckbxmntmNearestNeighbor;
	private JMenuItem chckbxmntmMSTTree;
	private JMenuItem chckbxmntmMSTTSP;
	private JCheckBoxMenuItem chckbxmntmFixCrossings;
	private JCheckBoxMenuItem chckbxmntmRandomizeDots;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ActionListener optionActionListener = new OptionActionListener();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "TSP Art Generator");
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TSPArtGUI window = new TSPArtGUI();
					window.frmTspArtGenerator.setExtendedState(Frame.MAXIMIZED_BOTH);
					window.frmTspArtGenerator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TSPArtGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTspArtGenerator = new JFrame();
		frmTspArtGenerator.setTitle("TSP Art Generator");
		frmTspArtGenerator.setBounds(100, 100, 800, 300);
		frmTspArtGenerator.setLocationRelativeTo(null);
		frmTspArtGenerator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmTspArtGenerator.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showOpenDialog();
			}
		});
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		mnFile.add(mntmOpen);

		mntmSave = new JMenuItem("Save");
		mntmSave.setEnabled(false);
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSaveDialog();
			}
		});
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		mnFile.add(mntmSave);

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		JMenu mnTspSolver = new JMenu("TSP Solver");
		mnOptions.add(mnTspSolver);

		chckbxmntmNearestNeighbor = new JCheckBoxMenuItem("Nearest Neighbor");
		chckbxmntmNearestNeighbor.addActionListener(optionActionListener);
		buttonGroup.add(chckbxmntmNearestNeighbor);
		chckbxmntmNearestNeighbor.setSelected(true);
		mnTspSolver.add(chckbxmntmNearestNeighbor);

		chckbxmntmMSTTree = new JCheckBoxMenuItem("Minimum Spanning Tree");
		chckbxmntmMSTTree.addActionListener(optionActionListener);
		buttonGroup.add(chckbxmntmMSTTree);
		mnTspSolver.add(chckbxmntmMSTTree);

		chckbxmntmMSTTSP = new JCheckBoxMenuItem("Minimum Spanning Tree w/ TSP walkthrough");
		chckbxmntmMSTTSP.addActionListener(optionActionListener);
		buttonGroup.add(chckbxmntmMSTTSP);
		mnTspSolver.add(chckbxmntmMSTTSP);

		chckbxmntmFixCrossings = new JCheckBoxMenuItem("Fix Crossings");
		chckbxmntmFixCrossings.addActionListener(optionActionListener);
		chckbxmntmFixCrossings.setSelected(true);
		mnOptions.add(chckbxmntmFixCrossings);

		chckbxmntmRandomizeDots = new JCheckBoxMenuItem("Randomize Dots");
		chckbxmntmRandomizeDots.addActionListener(optionActionListener);
		chckbxmntmRandomizeDots.setSelected(true);
		mnOptions.add(chckbxmntmRandomizeDots);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		frmTspArtGenerator.getContentPane().add(splitPane, BorderLayout.CENTER);

		lblOriginal = new JLabel("");
		lblOriginal.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				"Original"));
		splitPane.setLeftComponent(lblOriginal);
		lblOriginal.setHorizontalAlignment(SwingConstants.CENTER);

		lblTspArt = new JLabel("");
		lblTspArt.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				"TSP Art"));
		splitPane.setRightComponent(lblTspArt);
		lblTspArt.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private void generateTSPArt() {
		this.frmTspArtGenerator.setCursor(new Cursor(Cursor.WAIT_CURSOR));

		String solverName = null;

		if (this.chckbxmntmMSTTree.isSelected()) {
			solverName = "MST-Tree";
		} else if (this.chckbxmntmMSTTSP.isSelected()) {
			solverName = "MST-TSP";
		} else if (this.chckbxmntmNearestNeighbor.isSelected()) {
			solverName = "NN";
		}

		final TSPSolver solver = TSPSolverFactory.getSolver(solverName);

		final TSPFixer fixer = this.chckbxmntmFixCrossings.isEnabled() && this.chckbxmntmFixCrossings.isSelected() ? TSPFixerFactory
				.getFixer("2-Opt") : null;

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				TSPArtGenerator generator = new TSPArtGenerator(originalImage, solver, fixer, chckbxmntmRandomizeDots
						.isSelected());
				resultImage = generator.getTSPArtImage();

				lblTspArt.setIcon(new ImageIcon(resultImage));

				frmTspArtGenerator.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

				mntmSave.setEnabled(true);
			}
		});
	}

	private void showOpenDialog() {
		FileDialog fileDialog = new FileDialog(this.frmTspArtGenerator);
		fileDialog.setMode(FileDialog.LOAD);
		fileDialog.setDirectory(".");
		fileDialog.setFilenameFilter(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith("png") || name.toLowerCase().endsWith("jpg")
						|| name.toLowerCase().endsWith("jpeg");
			}
		});
		fileDialog.setVisible(true);

		String filename = fileDialog.getDirectory() + fileDialog.getFile();
		if (filename != null) {
			originalImage = null;
			File inputFile = null;

			try {
				inputFile = new File(filename);
				originalImage = ImageIO.read(inputFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			lblOriginal.setIcon(new ImageIcon(originalImage));

			generateTSPArt();
		}
	}

	private void showSaveDialog() {
		if (resultImage == null) {
			return;
		}

		FileDialog fileDialog = new FileDialog(this.frmTspArtGenerator);
		fileDialog.setMode(FileDialog.SAVE);
		fileDialog.setDirectory(".");
		fileDialog.setFilenameFilter(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith("png");
			}
		});
		fileDialog.setVisible(true);

		String filename = fileDialog.getDirectory() + fileDialog.getFile();
		if (filename != null) {
			try {
				ImageIO.write(resultImage, "png", new File(filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class OptionActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (chckbxmntmMSTTree.isSelected()) {
				chckbxmntmFixCrossings.setEnabled(false);
			} else {
				chckbxmntmFixCrossings.setEnabled(true);
			}

			if (originalImage != null) {
				generateTSPArt();
			}
		}
	}
}
