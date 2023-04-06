import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class WikiGame {

    private int maxDepth;
    private ArrayList<String> path = new ArrayList<>();
    public String currentLink;

    public static void main(String[] args) {
        WikiGame w = new WikiGame();
    }

    public WikiGame() {

        String startLink = "https://en.wikipedia.org/wiki/Dwayne_Johnson";  // beginning link, where the program will start
        String endLink = "https://en.wikipedia.org/wiki/Walt_Disney_Animation_Studios";    // ending link, where the program is trying to get to
        maxDepth = 1;           // start this at 1 or 2, and if you get it going fast, increase

        if (findLink(startLink, endLink, 0)) {
            System.out.println("found it********************************************************************");
            path.add(startLink);
            System.out.println(path);
        } else {
            System.out.println("did not found it********************************************************************");
        }

    }

    // recursion method
    public boolean findLink(String startLink, String endLink, int depth) {
        System.out.println("depth is: " + depth + ", link is: " + startLink);

        for (String i: subLinks(startLink)) {
            //System.out.println(i);

            // BASE CASE
            if(i.equals(endLink)){
                System.out.println("found it********************************************************************");
                path.add(startLink);
                return true;
            }
            else if (depth == maxDepth) {
                //System.out.println("brrrr");
                return false;
            }

            // GENERAL RECURSIVE CASE
            else {
                if (findLink(i, endLink, depth+1)) {
                    System.out.println("found it********************************************************************");
                    path.add(startLink);
                } else {
                    //System.out.println("did not found it ********************************************************************");
                }
            }
        }

        return false;
    }

    public ArrayList<String> subLinks (String link){
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
        } catch(Exception ex) {
            System.out.println(ex);
        }
        return links;
    }

}