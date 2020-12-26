import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

import javax.swing.*;


import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TreeGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	TreeMemento tm = new TreeMemento();
	
	AbsTree tree;
		
	boolean is_new_tree = true; // set to false after first insert

	Choice tree_kind, element_kind;
	
	public static Font font = new Font("Comic Sans MS", Font.BOLD, 24);

	JTextField input_elem_text, input_elem_text2, min_text, max_text; 
	
	JButton insertButton, deleteButton, undoButton; 
	JButton minButton, maxButton, clearButton;
	
	JPanel inputPanel;  
	OutputPanel outputPanel; 

	public TreeGUI() {
		
		super("GUI for Tree Operations");
		Label tree_kind_label;
		JPanel input1, input2;
		
		//The JPanel input1 details follow

		input1 = new JPanel(new FlowLayout());   //FlowLayout default
		
		tree_kind_label = new Label("Tree Kind:");
		tree_kind_label.setFont(font);
		input1.add(tree_kind_label);
		tree_kind = new Choice();
		tree_kind.setFont(font);
		tree_kind.addItem("Normal Tree");
		tree_kind.setFont(font);
		tree_kind.addItem("Dup Tree");
		tree_kind.setFont(font);
		input1.add(tree_kind);

		input_elem_text = new JTextField("integer");
		input_elem_text.setFont(font);
		input_elem_text.requestFocus(true);
		input_elem_text.selectAll();
		input_elem_text.setEditable(true);
		insertButton = new JButton("Insert");
		insertButton.setFont(font);
		input1.add(insertButton);
		input1.add(input_elem_text);
		
		input_elem_text2 = new JTextField("integer");
		input_elem_text2.setFont(font);
		input_elem_text2.requestFocus(true);
		input_elem_text2.selectAll();
		input_elem_text2.setEditable(true);
		deleteButton = new JButton("Delete");
		deleteButton.setFont(font);
		input1.add(deleteButton);
		input1.add(input_elem_text2);

		undoButton = new JButton("Undo");
		undoButton.setFont(font);
		input1.add(undoButton);
		
		//The JPanel input2 details follow

		input2 = new JPanel(new FlowLayout());   //FlowLayout default

		minButton = new JButton("Minimum");
		minButton.setFont(font);
		input2.add(minButton);
		min_text = new JTextField(10);
		min_text.setFont(font);
		min_text.setEditable(false);
		input2.add(min_text);

		maxButton = new JButton("Maximum");
		maxButton.setFont(font);
		input2.add(maxButton);
		max_text = new JTextField(10);
		max_text.setFont(font);
		max_text.setEditable(false);
		input2.add(max_text);

		clearButton = new JButton("Clear");
		clearButton.setFont(font);
		input2.add(clearButton);
		
		// The JPanels inputPanel and outputPanel details follow
		
		inputPanel = new JPanel(new BorderLayout());
		outputPanel = new OutputPanel();
		
		inputPanel.add("North", input1);
		inputPanel.add("South", input2);
				
		// Add Button Listeners here for:
		// min, max, clear, insert, delete, undo
		
		minButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (is_new_tree || tree == null) {
					JOptionPane.showMessageDialog(null, "Cannot take min of an empty tree");
					return;
				}
				min_text.setText(Integer.toString(tree.min().value));
			}
		});
		
		maxButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (is_new_tree || tree == null) {
					JOptionPane.showMessageDialog(null, "Cannot take max of an empty tree");
					return;
				}
				max_text.setText(Integer.toString(tree.max().value));
			}
		});
		
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				is_new_tree = true;
				tree = null;
				input_elem_text.setText("");
				input_elem_text2.setText("");
				min_text.setText("");
				max_text.setText("");
				outputPanel.clearPanel();
				tm.clear();
			}
		});
		
		insertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = input_elem_text.getText();
				boolean b = false;  // whether insert changed state of tree
				
				if (is_new_tree) {
					try {
						tm.set_state(null);
						if (tree_kind.getSelectedIndex() == 0)
							tree = new Tree(Integer.parseInt(s));
						else
							tree = new DupTree(Integer.parseInt(s));
					} catch (NumberFormatException e2) {
						JOptionPane.showMessageDialog(null, "Number format error – please re-enter value");
						return;
					}
	
				} // end of if is_new_tree
				else {
					 tm.set_state(tree);
			         try {
			        	 if (tree == null) {
			        		 if (tree_kind.getSelectedIndex() == 0)
							 tree = new Tree(Integer.parseInt(s));
			        		 else
							 tree = new DupTree(Integer.parseInt(s));
			        		 b = true;
			        	 }
			        	 else 
			        		 b = tree.insert(Integer.parseInt(s));
			         } catch (NumberFormatException e2) {
					 JOptionPane.showMessageDialog(null, "Number format error – please re-enter value");
					 return;
				   }
			         if(b!=true) {
			        	 tm.get_state();
			         }
				}
				is_new_tree = false;
				outputPanel.drawTree(tree);
				input_elem_text.selectAll();
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String s = input_elem_text2.getText();
				if(s.isEmpty() && tree==null)
				{
					JOptionPane.showMessageDialog(null, "Cannot delete from an empty tree");
					return;
				}
				
				else if(s.isEmpty() && tree!=null) {
					JOptionPane.showMessageDialog(null, "Please enter a valid value to delete");
					return;
				}
			
				else if((tree.max().value==tree.min().value) && (tree.max().value==Integer.parseInt(s)))
				{
					if(tree.get_count()==1)
					{
						tm.set_state(tree);
						tree = null;
						input_elem_text.setText("");
						min_text.setText("");
						max_text.setText("");
						outputPanel.clearPanel();
						return;
					}
					else
					{
						tm.set_state(tree);
						tree.set_count(tree.get_count()-1);
						outputPanel.drawTree(tree);
					}
				}
			
				else if(!s.isEmpty()) {
					tm.set_state(tree);
					if(tree.delete(Integer.parseInt(s))) {
						outputPanel.drawTree(tree);
						input_elem_text2.selectAll();
					}
					else
					{ 
						tm.get_state();
						JOptionPane.showMessageDialog(null, "Cannot delete non-existent value "+Integer.parseInt(s));
					}
				}
			}
		});
		
		
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(tm.is_empty())
				{
					JOptionPane.showMessageDialog(null, "No more undo operations are possible");
					
				}
				AbsTree absTree = tm.get_state();
				tree = absTree;
				if(absTree!=null) {
					outputPanel.drawTree(absTree);
					input_elem_text2.selectAll();
				}else {
					tree = null;
					input_elem_text.setText("");
					input_elem_text2.setText("");
					min_text.setText("");
					max_text.setText("");
					if(outputPanel!=null){
						outputPanel.clearPanel();
					}
				}
			}	 
		});
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		add("North", inputPanel);
		add("Center", outputPanel);

		setSize(1400, 1000); // for the frame
		setVisible(true);
	}
}
