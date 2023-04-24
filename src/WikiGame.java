import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class WikiGame {

    //prune: wiki/User:, wiki/User_talk, wiki/Special:, wiki/Help:, wiki/Wikiedia:, wiki/Template, wiki/File:, wiki/Category:

    private int maxDepth;
    private ArrayList<String> path = new ArrayList<>();
    public String currentLink;
    public String startingLink;
    public String endLink;
    public String finalPath;
    public JFrame mainFrame;
    public JPanel panel;
    public JPanel bigPanel;
    public JTextArea urlSearch;
    public JTextArea termSearch;
    public JTextArea depthSearch;
    public JLabel urlText;
    public JLabel termText;
    public JLabel depth;
    public JTextArea results;
    public JButton searchButton;
    public JScrollPane scrollBar;
    public HashMap<String, String> visitedLinks = new HashMap();

    public static void main(String[] args) {
        WikiGame wikigame= new WikiGame();
    }

    public WikiGame() {

        //prepareGUI();
        addBadLinks();

        startingLink = "https://en.wikipedia.org/wiki/Dwayne_Johnson";  // beginning link, where the program will start
        endLink = "https://en.wikipedia.org/wiki/Mickey_Mouse";    // ending link, where the program is trying to get to
        maxDepth = 2;           // start this at 1 or 2, and if you get it going fast, increase


        // https://en.wikipedia.org/wiki/Dwayne_Johnson
        // https://en.wikipedia.org/wiki/Mickey_Mouse

        System.out.println();

        search();

    }

    // recursion method
    public boolean findLink(String startLink, String endLink, int depth) {

        if(visitedLinks.containsValue(startLink) && depth != 0) {
            return false;
        }
        System.out.println("depth is: " + depth + ", link is: " + startLink);

        if (subLinks(startLink).contains(endLink)) {
            System.out.println("found itx********************************************************************");
            path.add(startLink);
            return true;
        }
        visitedLinks.put(startLink,startLink);

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
        try {
            URL url = new URL(link);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                nextPart = line;
                keepGoing = true;
                    while (keepGoing == true) {
                        if (nextPart.contains("<a href=\"/wiki")) {
                            subCurrentLink = nextPart.substring((nextPart.indexOf("<a href=\"/wiki") + 9), (nextPart.indexOf("\"", nextPart.indexOf("<a href=\"/wiki/") + 9)));
                            currentLink = "https://en.wikipedia.org".concat(subCurrentLink);
                            if(!currentLink.contains("wiki/User:") && !currentLink.contains("wiki/User_talk") && !currentLink.contains("wiki/Special:") && !currentLink.contains("wiki/Help:") && !currentLink.contains("wiki/Wikipedia:") && !currentLink.contains("wiki/Template") && !currentLink.contains("wiki/File:") && !currentLink.contains("wiki/Category:")) {
                                links.add(currentLink);
                            }
                            nextPart = nextPart.substring(nextPart.indexOf(subCurrentLink) + 9);
                        } else {
                            keepGoing = false;
                        }
                    }
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex);
            results.setText("One or more links are invalid");

        }
        return links;
    }

    public void prepareGUI() {
        mainFrame = new JFrame("WikiGame");
        mainFrame.setLayout(new GridLayout(2, 1));

        bigPanel = new JPanel();
        bigPanel.setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        urlText = new JLabel("Starting Link:", JLabel.CENTER);
        termText = new JLabel("Destination Link:", JLabel.CENTER);
        depth = new JLabel("Maximum Search Depth:", JLabel.CENTER);

        urlSearch = new JTextArea();
        urlSearch.setBounds(50, 5, 700, 650);
        termSearch = new JTextArea();
        termSearch.setBounds(50, 5, 700, 650);
        depthSearch = new JTextArea();
        depthSearch.setBounds(50,5,700,650);

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
        panel.add(depth);
        panel.add(depthSearch);
        bigPanel.add(panel, BorderLayout.CENTER);
        bigPanel.add(searchButton, BorderLayout.SOUTH);
        mainFrame.add(bigPanel);
        mainFrame.add(scrollBar);
        mainFrame.setSize(800, 700);
        mainFrame.setVisible(true);
    }

    public void search() {
        path.add(endLink);
        finalPath = " ";
        //results.setText("Loading...");
        if (findLink(startingLink, endLink, 0)) {
            System.out.println("found it********************************************************************");
            System.out.println(path);
            for(String i: path) {
                i = i.replace("https://en.wikipedia.org/wiki/","");
                i = i.replace("_", " ");
                finalPath = finalPath.concat(" " + i + " -->");
            }
            finalPath = finalPath.substring(0, finalPath.length()-3);
            System.out.println(finalPath);
            //results.setText(finalPath);
        } else {
            System.out.println("did not found it********************************************************************");
        }
    }

    public void addBadLinks(){
        visitedLinks.put("https://en.wikipedia.org/wiki/Main_Page","https://en.wikipedia.org/wiki/Main_Page");
        visitedLinks.put("https://en.wikipedia.org/wiki/Wikipedia:Contents","https://en.wikipedia.org/wiki/Wikipedia:Contents");
        visitedLinks.put("https://en.wikipedia.org/wiki/Portal:Current_events","https://en.wikipedia.org/wiki/Portal:Current_events");
        visitedLinks.put("https://en.wikipedia.org/wiki/Special:Random","https://en.wikipedia.org/wiki/Special:Random");
        visitedLinks.put("https://en.wikipedia.org/wiki/Wikipedia:About","https://en.wikipedia.org/wiki/Wikipedia:About");
        visitedLinks.put("https://en.wikipedia.org/wiki/Help:Contents","https://en.wikipedia.org/wiki/Help:Contents");
        visitedLinks.put("https://en.wikipedia.org/wiki/Help:Introduction","https://en.wikipedia.org/wiki/Help:Introduction");
        visitedLinks.put("https://en.wikipedia.org/wiki/Wikipedia:Community_portal","https://en.wikipedia.org/wiki/Wikipedia:Community_portal");
        visitedLinks.put("https://en.wikipedia.org/wiki/Special:RecentChanges","https://en.wikipedia.org/wiki/Special:RecentChanges");
        visitedLinks.put("https://en.wikipedia.org/wiki/Wikipedia:File_upload_wizard","https://en.wikipedia.org/wiki/Wikipedia:File_upload_wizard");
        visitedLinks.put("https://en.wikipedia.org/wiki/Special:Search","https://en.wikipedia.org/wiki/Special:Search");
        visitedLinks.put("https://en.wikipedia.org/wiki/Special:MyContributions","https://en.wikipedia.org/wiki/Special:MyContributions");
        visitedLinks.put("https://en.wikipedia.org/wiki/Special:MyTalk","https://en.wikipedia.org/wiki/Special:MyTalk");
        System.out.println(visitedLinks);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            results.setText(null);
            startingLink = urlSearch.getText();
            endLink = termSearch.getText();
            try {
                maxDepth = Integer.parseInt(depthSearch.getText());
                results.setText("Start: "+startingLink+"\nEnd: "+endLink+"\nMax Depth: "+maxDepth+"\nLoading...");
                String command = e.getActionCommand();
                if (command.equals("Search")) {
                    search();
                    results.setText("Start: "+startingLink+"\nEnd: "+endLink+"\nMax Depth: "+maxDepth+"\n"+"Path: "+finalPath);
                }
            } catch (Exception ex) {
                results.setText("please enter a valid depth");
            }
        }
    }
}