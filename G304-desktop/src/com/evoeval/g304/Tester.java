package com.evoeval.g304;

import java.awt.BorderLayout;





import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;

import com.evoeval.g304.singleplayer.AIBase;
import com.evoeval.g304.singleplayer.InterpreterTools;
import com.evoeval.g304.util.AIActionIntepreter;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;


public class Tester extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public Tester() {
		setTitle("Test");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 818, 567);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		final JScrollPane jsc1 = new JScrollPane();
		final JTextArea jtxtin = new JTextArea();
		jtxtin.setFont(new Font("Courier New", Font.PLAIN, 15));
		jtxtin.setBackground(new Color(240, 248, 255));
		jtxtin.setRows(15);
		jsc1.setViewportView(jtxtin);
		contentPane.add(jsc1, BorderLayout.NORTH);
		
		final JScrollPane jsc2 = new JScrollPane();
		final JTextArea jtxtOut = new JTextArea();
		jtxtOut.setRows(12);
		jsc2.setViewportView(jtxtOut);
		contentPane.add(jsc2, BorderLayout.SOUTH);
		
		JButton btnOk = new JButton();
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(AIBase.currentJexl!=null){
					jtxtOut.setText(AIActionIntepreter.runJexlScript(AIBase.currentJexl, jtxtin.getText()));
				}else{
					JexlContext jc = new MapContext();
					jc.set("tools", new InterpreterTools());
					jtxtOut.setText(AIActionIntepreter.runJexlScript(jc, jtxtin.getText()));
				}
			}			
		});
		btnOk.setText("Ok");
		contentPane.add(btnOk, BorderLayout.WEST);
	}

}
