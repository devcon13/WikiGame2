import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class WikiGame {

    private int maxDepth;
    private ArrayList<String> path = new ArrayList<>();
    public String currentLink;
    public String startingLink;
    public String endLink;
    public JFrame mainFrame;
    public JPanel panel;
    public JPanel bigPanel;
    public JTextArea urlSearch;
    public JTextArea termSearch;
    public JLabel urlText;
    public JLabel termText;
    public JTextArea results;
    public JButton searchButton;
    public JScrollPane scrollBar;

    public static void main(String[] args) {
        WikiGame w = new WikiGame();
    }

    public WikiGame() {

        //prepareGUI();

        startingLink = "https://en.wikipedia.org/wiki/Dwayne_Johnson";  // beginning link, where the program will start
        endLink = "https://en.wikipedia.org/wiki/Mickey_Mouse";    // ending link, where the program is trying to get to
        maxDepth = 2;           // start this at 1 or 2, and if you get it going fast, increase

        search();

        System.out.println();

    }

    // recursion method
    public boolean findLink(String startLink, String endLink, int depth) {

        if(isItABadLink(startLink)|| (startLink.equals(startingLink) && depth != 0)) {
            return false;
        }
        System.out.println("depth is: " + depth + ", link is: " + startLink);

        if (subLinks(startLink).contains(endLink)) {
            System.out.println("found itx********************************************************************");
            path.add(startLink);
            return true;
        }

        for (String i : subLinks(startLink)) {
            /*if (i.equals(startLink) || i.equals(startingLink)) {
                return false;
            }

             */

            if (depth == maxDepth) {
                //System.out.println("brrrr");
                return false;
            } else if (findLink(i, endLink, depth + 1)) {
                System.out.println(startLink+"found it********************************************************************");
                path.add(startLink);
                return true;
            } else {
                //System.out.println("did not found it ********************************************************************");
            }

        }

        return false;
    }

    public ArrayList<String> subLinks(String link) {
        ArrayList<String> links = new ArrayList<>();
        boolean keepGoing;
        String subCurrentLink;
        String nextPart;
        int startPoint;
        try {
            URL url = new URL(link);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                nextPart = line;
                keepGoing = true;
                if (line.contains("<a href=\"/wiki")) {
                    while (keepGoing == true) {
                        if (nextPart.contains("<a href=\"/wiki")) {
                            subCurrentLink = nextPart.substring((nextPart.indexOf("<a href=\"/wiki") + 9), (nextPart.indexOf("\"", nextPart.indexOf("<a href=\"/wiki/") + 9)));
                            currentLink = "https://en.wikipedia.org".concat(subCurrentLink);
                            links.add(currentLink);
                            nextPart = nextPart.substring(nextPart.indexOf(subCurrentLink) + 9);
                        } else {
                            keepGoing = false;
                        }
                    }
                }
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return links;
    }

    public void prepareGUI() {
        mainFrame = new JFrame("WikiGame");
        mainFrame.setLayout(new GridLayout(2, 1));

        bigPanel = new JPanel();
        bigPanel.setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        urlText = new JLabel("Start:", JLabel.CENTER);
        termText = new JLabel("Destination:", JLabel.CENTER);

        urlSearch = new JTextArea();
        urlSearch.setBounds(50, 5, 700, 650);
        termSearch = new JTextArea();
        termSearch.setBounds(50, 5, 700, 650);

        searchButton = new JButton("Go!");
        searchButton.setActionCommand("Search");
        searchButton.addActionListener(new ButtonClickListener());

        results = new JTextArea();
        results.setLineWrap(true);
        scrollBar = new JScrollPane(results);
        scrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(urlText);
        panel.add(urlSearch);
        panel.add(termText);
        panel.add(termSearch);
        bigPanel.add(panel, BorderLayout.CENTER);
        bigPanel.add(searchButton, BorderLayout.SOUTH);
        mainFrame.add(bigPanel);
        mainFrame.add(scrollBar);
        mainFrame.setSize(800, 700);
        mainFrame.setVisible(true);
    }

    public void search() {
        String finalPath = "";
        path.add(endLink);
        //results.setText("Loading...");
        if (findLink(startingLink, endLink, 0)) {
            System.out.println("found it********************************************************************");
            System.out.println(path);
            for(String i: path) {
                finalPath = finalPath.concat(i + "-->");
            }
            finalPath = finalPath.substring(0, finalPath.length()-3);
            //results.setText(finalPath);
        } else {
            System.out.println("did not found it********************************************************************");
        }
    }

    public boolean isItABadLink(String h){
        if(h.equals("https://en.wikipedia.org/wiki/Main_Page")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Wikipedia:Contents")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Portal:Current_events")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Special:Random")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Wikipedia:About")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Help:Contents")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Help:Introduction")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Wikipedia:Community_portal")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Special:RecentChanges")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Wikipedia:File_upload_wizard")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Special:Search")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Special:MyContributions")){
            return true;
        }
        if(h.equals("https://en.wikipedia.org/wiki/Special:MyTalk")){
            return true;
        }


        return false;
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Search")) {
                startingLink = urlSearch.getText();
                endLink = termSearch.getText();
                if (startingLink.equals("")) {
                } else {
                }
                search();
            }
        }
    }
}