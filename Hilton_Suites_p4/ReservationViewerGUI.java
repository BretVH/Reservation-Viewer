/*
 * ReservationViewer.java
 *
 * Created on February 24, 2012, 11:31:07 AM
 */
package ReservationViewer;



import SortAFile.Reservation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;

/**
 * Reservation Viewer
 *
 * GUI to display and sort a database of Hotel Reservations
 * 
 *
 * @author Bret A. Van Hof, Sereyvathanak Khorn, Tamir Enkhbat 
 * @version 1.0
 *
 * Compiler: Java 1.6 
 * OS: Windows 7 
 * Hardware: PC 
 *
 * March 03 2012
 * BVH, SK, TE completed v 1.0
 */ 
public class ReservationViewerGUI extends JFrame
{
    private static final int FRAME_WIDTH = 880;
    private static final int FRAME_HEIGHT = 410;
    private static final int ROWS = 4;
    private static final int STRUT_SIZE = 15;
    private JPanel aContainer;
    private JPanel picturePanel;
    private JPanel searchPanel;
    private DefaultListModel model;
    private JList listJList;
    private Comparable[] reservations;
    private JPanel textFields;
    private  JLabel arrival = new JLabel("Arrival Date: ");
    private  JLabel departure = new JLabel("Departure Date: ");
    private JLabel imageJLabel;
    private JTextField aDate = new JTextField();
    private JTextField bDate = new JTextField();
    private JScrollPane jScrollPane;
    private JRadioButton choice1;
    private JRadioButton choice2;
    private JButton search;
    private JTextField searchBox;
    private JLabel partial;
    private JLabel full;
    private JMenuBar menu = new JMenuBar();
    private JMenu file = new JMenu("File");
    private JMenu exit = new JMenu("Exit");
    private JMenuItem closeProgram = new JMenuItem("Close Program");
    private JMenuItem loadBinaryFile = new JMenuItem("Load Reservations File");
    private MouseListener mouseListener = new MouseMoveOn();
    /**
     * Default constructor
     */
    public ReservationViewerGUI()
    {
        initComponents();
        setTitle("Reservation Viewer by Team Hilton Suites");
        setLayout(new BorderLayout());
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage
                ("logo.gif")); //sets icon
        searchBox.grabFocus();
        getRootPane().setDefaultButton(search);
    }
    /**
     * Method: main
     * runs the program
     * @param args 
     */
    public static void main(String[] args)
    {
         java.awt.EventQueue.invokeLater(new Runnable() 
         {
            public void run() 
            {
                new ReservationViewerGUI().setVisible(true);
                
            } 
         });  
    }
    /**
     * Method: readBinaryFile
     * reads in reservations from binary file to an array, and returns the 
     * array. 
     * @return aReservation
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static Comparable[] readBinaryFile(String fileName) 
          throws IOException, ClassNotFoundException
    {
      File f = new File(fileName);
      if(!f.exists())
      {
        JOptionPane.showMessageDialog(null, "Can't Open Binary Reservation File"  
              + " it may not exist", "", 
                            JOptionPane.WARNING_MESSAGE);
          Comparable[] aReservation = {};
          return aReservation;
      }
      else
      {
          ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
          Comparable[] aReservation = (Reservation[])in.readObject();
          in.close();
          return aReservation;
      }

    }
    /**
     * Method: JListValueChanged
     * updates arrival and departure text boxes as the reservation selected in
     * the JList is changed.
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void jListValueChanged() 
            throws IOException, ClassNotFoundException
    {
       
        int index = listJList.getSelectedIndex();
        if(index == -1)
        {
           
            return;
        }
        if(index >= 0)
        {    
                Reservation aRes = (Reservation)reservations[index];
                String arrivalDate = "" + aRes.getArrivalDate().toString();
                String departureDate = "" + 
                        aRes.getDepartureDate().toString();
                bDate.setText(departureDate);
                aDate.setText(arrivalDate);
        }
    }
    /**
     * Method: initComponents 
     * creates the GUI
     */
    private void initComponents()
    {
        aContainer = new JPanel();
        textFields = new JPanel();
        textFields.setLayout(new GridLayout(ROWS, 1));
        aDate.setColumns(20);
        bDate.setColumns(20);
        textFields.add(arrival);
        textFields.add(aDate);
        textFields.add(departure);
        textFields.add(bDate);
        aDate.setEditable(false);
        bDate.setEditable(false);
        aDate.setBackground(Color.WHITE);
        bDate.setBackground(Color.WHITE);
        model = new DefaultListModel();
        listJList = new JList();
        jScrollPane = new javax.swing.JScrollPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        listJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        aContainer.add(listJList);
        try 
        {
            reservations = readBinaryFile("reservations.dat");
            for(Comparable res: reservations)
            {
                Reservation newRes = (Reservation)res;
                 model.addElement(newRes.getName());
            }
            listJList.setModel(model);
            jListValueChanged();
            ListSelectionListener listener = new AddJListActionListener();
            listJList.addListSelectionListener(listener); 
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ReservationViewerGUI.class.getName()).log
                    (Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(ReservationViewerGUI.class.getName()).log
                    (Level.SEVERE, null, ex);
        }
        jScrollPane.setViewportView(listJList);
        imageJLabel = new JLabel();
        picturePanel = new JPanel();
        searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        File input = new File("hilton_hotel.jpg");
        BufferedImage image;
        try 
        {
            image = ImageIO.read(input);
            ImageIcon icon = new ImageIcon(image);
            imageJLabel.setIcon(icon);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(ReservationViewerGUI.class.getName()).log
                    (Level.SEVERE, null, ex);
        }
        choice1 = new JRadioButton();
        choice2 = new JRadioButton();
        ButtonGroup group = new ButtonGroup();
        group.add(choice1);
        group.add(choice2);
        choice1.setSelected(true);
        search = new JButton("Search Now!");
        ActionListener listener = new SearchJButtonActionListener();
        search.addActionListener(listener);
        search.setMnemonic('s');
        search.setToolTipText("Click to generate search results");
        search.setSelected(true);
        searchBox = new JTextField();
        searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.add(Box.createHorizontalStrut(STRUT_SIZE));
        partial = new JLabel("Starting With: ");
        full = new JLabel("Whole Name: ");
        searchPanel.add(partial);
        searchPanel.add(choice1);
        searchPanel.add(Box.createHorizontalStrut(STRUT_SIZE));
        searchPanel.add(full);
        searchPanel.add(choice2);
        searchPanel.add(Box.createHorizontalStrut(STRUT_SIZE));
        searchPanel.add(search);
        searchPanel.add(Box.createHorizontalStrut(STRUT_SIZE));
        searchPanel.add(searchBox);
        picturePanel.add(imageJLabel);
        getContentPane().add(jScrollPane, java.awt.BorderLayout.WEST);
        getContentPane().add(textFields, java.awt.BorderLayout.CENTER);
        getContentPane().add(picturePanel, java.awt.BorderLayout.EAST);
        getContentPane().add(searchPanel, java.awt.BorderLayout.SOUTH);
        createMenu();
        setJMenuBar(menu);
        listJList.addMouseListener(mouseListener);
        pack();
    }
    /**
     * ListSelectionListener for JList
     */
    class AddJListActionListener implements ListSelectionListener
    {
        @Override
        /**
         * Method: valueChanged
         * calls jListValueChanged if new name on JList selected
         */
        public void valueChanged(javax.swing.event.ListSelectionEvent evt) 
        {

            try 
            {
                jListValueChanged();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ReservationViewerGUI.class.getName()).log
                        (Level.SEVERE, null, ex);
            }
            catch (ClassNotFoundException ex) 
            {
                Logger.getLogger(ReservationViewerGUI.class.getName()).log
                        (Level.SEVERE, null, ex);
            }
         }
      }
    /**
     * searchButton ActionListener
     */
    class SearchJButtonActionListener implements ActionListener
    {
    @Override
    /**
     * Method: actionPerformed
     * Calls appropriate binary search to match what is in textField
     */
        public void actionPerformed(ActionEvent event)
        {
            int index = -1;
            if(choice1.isSelected())
            {
                if(searchBox.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(null,
                        " No input", "Search Result", 
                            JOptionPane.WARNING_MESSAGE);
                    index = 0;
                }
                else
                {
                 index = partialBinarySearch(reservations,
                         searchBox.getText()); 
                }
            }
            if(choice2.isSelected())
            {
                index = binarySearch(reservations, searchBox.getText());
            }
            if (index == -1)
            {
                JOptionPane.showMessageDialog(null, searchBox.getText()  + 
                        " not Found", "Search Result", 
                            JOptionPane.WARNING_MESSAGE);
                    listJList.setSelectedIndex(0);
            }
                else
                    listJList.setSelectedIndex(index);

        }
    }
    
/** 
 * Method: binarySearch
 * Binary search function
 * @param array
 * @param key
 * @return -1 if not found; index of reservation if found
 */
    public static int binarySearch(Comparable[] array, String key)
    {
       int low = 0;                     // low subscript
       int high = array.length - 1;     // high subscript
       int middle;                      // middle subscript
       int i = 0;
       Reservation[] anArray = new Reservation[array.length];
       for(Comparable res: array)
       {
           anArray[i] = (Reservation)res;
           i++;
       }
       while ( low <= high ) {
          middle = ( low + high ) / 2;

          if (key.equalsIgnoreCase((anArray[middle].getName())))  // match
             return middle;
          else if (key.compareToIgnoreCase(anArray[middle].getName()) < 0 )
             high = middle - 1;  // search low end of array
          else
             low = middle + 1;   // search high end of array
       }

       return -1;   // searchKey not found
    }
    /**
     * Method: partialBinarySearch
     * binary search function for substrings
     * @param array
     * @param key
     * @return -1 if not found; index of reservation if found
     */
    public static int partialBinarySearch(Comparable[] array, String key)
    {
       int low = 0;                     // low subscript
       int high = array.length - 1;     // high subscript
       int middle;                      // middle subscript
       int i = 0;
       int length = key.length();
       Reservation[] anArray = new Reservation[array.length];
       for(Comparable res: array)
       {
           anArray[i] = (Reservation)res;
           i++;
       }
       while ( low <= high ) {
          middle = ( low + high ) / 2;

          if (key.equalsIgnoreCase((anArray[middle].getName().substring
                  (0,length))))  // match
             return middle;
          else if (key.compareToIgnoreCase(anArray[middle].getName().substring
                  (0,length)) < 0 )
             high = middle - 1;  // search low end of array
          else
             low = middle + 1;   // search high end of array
       }

       return -1;   // searchKey not found
    }
    /**
     * creates a menu on the GUI
     */
    public void createMenu()
    {
        menu.add(file);
        menu.add(exit);
        file.add(loadBinaryFile);
        exit.add(closeProgram);
        /**
         * ActionListener for exit
         */
        class exitMenuActionListener implements ActionListener
        {

            @Override
            /**
             * Method: actionPerformed
             * when closeProgram is clicked GUI closes
             */
            public void actionPerformed(ActionEvent e) 
            {
                System.exit(0);
            }
            
        }
        /** Creates new ActionListener for loadDictionary JMenuItem on GUI */
        class MenuActionListener implements ActionListener 
        {
            /**
            * Method: actionPerformed
            * Sets behavior of loadDictionary JMenuItem when pressed.
            * pre-condition: default dictionary.
            * post-condition: JFileChooser pops-up.
            */    
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JFileChooser dictionaryChooser = new JFileChooser
                        ("reservations.dat");
                if(dictionaryChooser.showOpenDialog(null) == 
                        JFileChooser.APPROVE_OPTION)
                {
                    try 
                    {
                        reservations = readBinaryFile
                               (dictionaryChooser.getSelectedFile().toString());
                        model.removeAllElements();
                        for(Comparable res: reservations)
                        {
                            Reservation newRes = (Reservation)res;
                            model.addElement(newRes.getName());
                        }
                        listJList.setModel(model);
                        
                    }
                    catch (IOException ex) 
                    {
                        Logger.getLogger(ReservationViewerGUI.class.getName
                                ()).log(Level.SEVERE, null, ex);
                           JOptionPane.showMessageDialog
                                (null, "Can't Read File!","alert", JOptionPane.
                                   ERROR_MESSAGE);
                                    return;
                    } 
                    catch (ClassNotFoundException ex) 
                    {
                        Logger.getLogger(ReservationViewerGUI.class.getName
                                ()).log(Level.SEVERE, null, ex);
                    }
                    
                       
                }
            }   
        }
        MenuActionListener listener = new MenuActionListener();
        exitMenuActionListener listener2 = new exitMenuActionListener();
        loadBinaryFile.addActionListener(listener); 
        closeProgram.addActionListener(listener2);
    }
    /**
     * MouseMoveOn is a MouseAdapter Class for the JList
     */
      public class MouseMoveOn extends MouseAdapter
      { 
        /**
           * Method: mouseEntered
           * @param e 
           */  
        public void mouseEntered(MouseEvent e)
        {
            eventOutput1(e);
            
        }
        /**
         * Method: mouseExited
         * @param e 
         */
        public void mouseExited(MouseEvent e)
        {
            eventOutput2(e);
        }
        /**
         * Method: eventOutput1
         * changes JList text to red
         * @param e 
         */
        private void eventOutput1(MouseEvent e) 
        {
            listJList.setForeground(Color.red);
        }
        /**
         * Method: eventOutput2
         * changes JList text back to black
         * @param e 
         */
        private void eventOutput2(MouseEvent e) 
        {
            listJList.setForeground(Color.black);
        }
        
    }
}
