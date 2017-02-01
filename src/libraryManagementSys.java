import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class libraryManagementSys extends JApplet implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel title;
	private JButton addBook;
	private JButton searchBook;
	private JButton exit;
	private JButton printBooks;
	private JButton deleteBook;
	private JButton lendBook;
	private JButton returnBook;
	private JButton listavail;
	private JButton listreturn;
	private Connection con;
	private JPanel panel;
	Image Background;
	
	public libraryManagementSys()
	{
		panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		title = new JLabel("Van Houten Library",SwingConstants.CENTER);
		title.setFont(new Font("Helvetica", Font.BOLD+Font.ITALIC, 24));
		addBook = new JButton("Add Book");
		searchBook = new JButton("Search Book");
		printBooks = new JButton("List All Books");
		deleteBook = new JButton("Delete Book");
		lendBook = new JButton("Lend Book");
		returnBook = new JButton("Return Book");
		exit = new JButton("Exit");
		listavail=new JButton("List Available Books");
		listreturn= new JButton("List Rented Books");
	}
	
	public static void connection()
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("success");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void init()
	{
		connection();
		try 
		{
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","root");
			System.out.println("connected to Database");	
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	setSize(400,600);
	Background = getImage(getCodeBase(),"save.JPG");
	panel.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill= GridBagConstraints.HORIZONTAL;
	title.setForeground(Color.white);
	
	c.gridx=0;
	c.gridy=0;
	c.ipadx=60;
	c.ipady=10;
	panel.add(title,c);

	c.gridx=0;
	c.gridy=1;
	panel.add(addBook,c);
	
	c.gridx=0;
	c.gridy=2;
	panel.add(searchBook,c);
	c.gridx=0;
	c.gridy=3;
	panel.add(printBooks,c);
	
	c.gridx=0;
	c.gridy=4;
	panel.add(deleteBook,c);
	
	c.gridx=0;
	c.gridy=5;
	panel.add(lendBook,c);
	
	c.gridx=0;
	c.gridy=6;
	panel.add(returnBook,c);
	
	c.gridx=0;
	c.gridy=7;
	panel.add(listreturn, c);
	
	c.gridx=0;
	c.gridy=8;
	panel.add(listavail, c);
	
	c.gridx=0;
	c.gridy=9;
	panel.add(exit,c);
	add(panel);
	
	addBook.addActionListener(this);
	searchBook.addActionListener(this);
	printBooks.addActionListener(this);
	deleteBook.addActionListener(this);
	lendBook.addActionListener(this);
	returnBook.addActionListener(this);
	exit.addActionListener(this);
	listavail.addActionListener(this);
	listreturn.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String title;
		int id;
		String author;
		String status;
		ResultSet set;
		if(e.getSource()==addBook)
		{
			try {
				Statement st = con.createStatement();
				JTextField text1 = new JTextField(20);
				JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),text1,"Enter Book ID",JOptionPane.CLOSED_OPTION);
				String getID = text1.getText();
				JTextField text2 = new JTextField(20);
				JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),text2,"Enter Book Title",JOptionPane.CLOSED_OPTION);
				String getName = text2.getText();
				JTextField text3 = new JTextField(20);
				JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),text3,"Enter Author's name",JOptionPane.CLOSED_OPTION);
				String getAuthor = text3.getText();
				if(getID.isEmpty()||getName.isEmpty()||getAuthor.isEmpty())
				{
					JOptionPane.showMessageDialog(rootPane, "Values Cannot be null");	
				}
				else
				{
					st.executeUpdate("INSERT INTO books " + " values ('"+getID+"','"+getName+"','"+getAuthor+"','Available')");
					JOptionPane.showMessageDialog(rootPane, "Book added to the DataBase");
					
				}
				}
			catch (MySQLIntegrityConstraintViolationException icv)
			{
				JOptionPane.showMessageDialog(rootPane, "Book already available in the Database");			
				
			}
			catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
	}
		if(e.getSource()==searchBook)
		{
		try 
		{
			Statement st = con.createStatement();
			JTextField searchId = new JTextField(20);
			JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),searchId,"Enter Book ID to Search",JOptionPane.CLOSED_OPTION);
			String getID = searchId.getText();
			set= st.executeQuery("SELECT * FROM books WHERE id = '"+getID+"'");
			if(!set.next())
			{
				JOptionPane.showMessageDialog(rootPane, "Book Not found in Database");
			}	
			else
			{
				id = set.getInt("ID");
				title = set.getString("Book_Title");
				author = set.getString("Author_name");
				status = set.getString("Book_Status");
				JOptionPane.showMessageDialog(rootPane, "Book Found \nID:"+id+"\nTitle:"+title+"\nAuthor:"+author+"\nStatus:"+status+"");	
				}	
			}
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	}
		if(e.getSource()==printBooks)
		{
			StringBuilder sb = new StringBuilder();
			try {
				Statement st = con.createStatement();
				set= st.executeQuery("SELECT * FROM books");
				while(set.next())
				{
					sb.append(set.getString("ID"));
					sb.append(" , ");
					sb.append(set.getString("Book_Title"));
					sb.append(" , ");
					sb.append(set.getString("Author_name"));
					sb.append(" , ");
					sb.append(set.getString("Book_Status"));
					sb.append("\n");
				}
				JOptionPane.showMessageDialog(rootPane,sb);
				System.out.println("~~~~~List of all books and their status~~~~~");
				System.out.println(sb);
				System.out.println("--------------------XXXXXXX----------------------");
				} 
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		if(e.getSource()==listavail)
		{
			StringBuilder sb = new StringBuilder();
			try 
			{
				Statement st = con.createStatement();
				set= st.executeQuery("SELECT * FROM books where Book_Status='Available'");
				while(set.next())
				{
					sb.append(set.getString("ID"));
					sb.append(" , ");
					sb.append(set.getString("Book_Title"));
					sb.append(" , ");
					sb.append(set.getString("Author_name"));
					sb.append(" , ");
					sb.append(set.getString("Book_Status"));
					sb.append("\n");
				}
				JOptionPane.showMessageDialog(rootPane,sb);
				System.out.println("~~~~~List of all Available books and their status~~~~~");
				System.out.println(sb);
				System.out.println("--------------------XXXXXXX----------------------");
				} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
		}
		if(e.getSource()==listreturn)
		{
			StringBuilder sb = new StringBuilder();
			try 
			{
				Statement st = con.createStatement();
				set= st.executeQuery("SELECT * FROM books where Book_Status='Lent'");
				while(set.next())
				{
					sb.append(set.getString("ID"));
					sb.append(" , ");
					sb.append(set.getString("Book_Title"));
					sb.append(" , ");
					sb.append(set.getString("Author_name"));
					sb.append(" , ");
					sb.append(set.getString("Book_Status"));
					sb.append("\n");
				}
				JOptionPane.showMessageDialog(rootPane,sb);
				System.out.println("~~~~~List of all Rented books and their status~~~~~");
				System.out.println(sb);
				System.out.println("--------------------XXXXXXX----------------------");
			} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
		}
		if(e.getSource()==deleteBook)
		{
		try
			{
			Statement st = con.createStatement();
			JTextField text1 = new JTextField(20);
			JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),text1,"Enter Book ID to Delete",JOptionPane.CLOSED_OPTION);
			String getId = text1.getText();
			set= st.executeQuery("SELECT * FROM books where ID='"+getId+"'");
			if(set.next())
				{
				id = set.getInt("ID");
				title = set.getString("Book_Title");
				author = set.getString("Author_name");
				st.executeUpdate("DELETE FROM books WHERE ID='"+getId+"'");
				JOptionPane.showMessageDialog(rootPane, "Book Deleted from the Database\nID: "+id+"\nTitle: "+title+"\nAuthor: "+author+"");
				}
			else
			{
				JOptionPane.showMessageDialog(rootPane, "Book not found in the DataBase");	
			}
			}
		catch (SQLException sql)
		{
			sql.printStackTrace();
		}	
		}
		if(e.getSource()==lendBook)
		{
			try
			{
				Statement st = con.createStatement();
				JTextField text1 = new JTextField(20);
				JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),text1,"Book Rental..Enter Book ID",JOptionPane.CLOSED_OPTION);
				String getId = text1.getText();
				set= st.executeQuery("SELECT * FROM books where ID='"+getId+"'");
				if(set.next())
				{
					 id = set.getInt("ID");
					 title = set.getString("Book_Title");
					 author = set.getString("Author_name");
					 status = set.getString("Book_Status");
					 if(status.contains("Available"))
					 {
						 st.executeUpdate("UPDATE books SET Book_Status='Lent' WHERE ID='"+id+"'");	
						 JOptionPane.showMessageDialog(rootPane, "Book Rented..Status Changed\nID: "+id+"\nTitle: "+title+"\nAuthor: "+author+"\nStatus: Lent");
					 }
					 else
					 {
						 JOptionPane.showMessageDialog(rootPane, "Book is already rented");	
					 }
					 
				}
				else
				{
					JOptionPane.showMessageDialog(rootPane, "Book not found in the DataBase");			
				}
			}
			catch (SQLException sql)
			{
				sql.printStackTrace();
			}
		}
		if(e.getSource()==returnBook)
		{
			try
			{
			Statement st = con.createStatement();
			JTextField text1 = new JTextField(20);
			JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),text1,"Book Return..Enter Book ID",JOptionPane.CLOSED_OPTION);
			String getId = text1.getText();
			set= st.executeQuery("SELECT * FROM books where ID='"+getId+"'");
			if(set.next())
			{
				id = set.getInt("ID");
				 title = set.getString("Book_Title");
				 author = set.getString("Author_name");
				 status = set.getString("Book_Status");
				 if(status.contains("Lent"))
				 {
					 st.executeUpdate("UPDATE books SET Book_Status='Available' WHERE ID='"+id+"'");	
					 JOptionPane.showMessageDialog(rootPane, "Book Returned..Status Changed\nID: "+id+"\nTitle: "+title+"\nAuthor: "+author+"\nStatus: Available");
				 }
				 else
				 {
					 JOptionPane.showMessageDialog(rootPane, "Book was never rented & available in the Library");	
				 }
			}
			else
			{
				JOptionPane.showMessageDialog(rootPane, "Invalid Book Id.. Book does not belong to Library!!");	
			}
			}
			catch (SQLException sql)
			{
				sql.printStackTrace();
			}
}
		if(e.getSource()==exit)
		{
			System.exit(0);
		}
}
}
