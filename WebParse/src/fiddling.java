import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class fiddling {

    String url = "https://www.merriam-webster.com/thesaurus/";

    public static void main(String[] args) throws IOException {
        System.out.println(synonymSentence(args[0]));
    }

    public static String synonymSentence(String input) throws IOException {
        String[] words;
        words = parseWords(input);
        String s = "";
        for (int i = 0; i < words.length; i++) {
            String temp = synonym(words[i]);
            if (i == 0)
                temp = capitalize(temp);
            s += temp;
            if (i < words.length - 1)
                s += " ";
            else
                s += ".";
        }
        return s;
    }

    private static String[] parseWords(String s) {
        ArrayList<String> out = new ArrayList<String>();
        String word = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ' && s.charAt(i) != '.') {
                word += s.charAt(i);
            } else {
                out.add(word);
                word = "";
            }
        }
        if (s.charAt(s.length() - 1) != '.')
            out.add(word);
        String[] output = new String[out.size()];
        return out.toArray(output);
    }

    private static String firstWord(String s) {
        int i = 0;
        String word = "";
        while (s.charAt(i) != ',' && s.charAt(i) != '[') {
            word += s.charAt(i++);
        }
        return word;
    }

    private static String firstWordTitle(String s) {
        int i = 0;
        String word = "";
        while (s.charAt(i) != ',' && s.charAt(i) != '[' && s.charAt(i) != ' ') {
            word += s.charAt(i++);
        }
        return word;
    }

    private static String synonym(String word) throws IOException {
        try {
            Document doc = Jsoup.connect("https://www.merriam-webster.com/thesaurus/" + word).get();
            Elements b = doc.getElementsByClass("thes-list-content");
            String title = firstWordTitle(doc.getElementsByTag("title").first().text()).toLowerCase();
            if (equal(title, word))
                return firstWord(b.first().text());
            else {
                // System.out.println("'" + title + "' does not equal " + "'"+ word + "'");
                return word;
            }
        } catch (HttpStatusException e) {
            return word;
        }
    }

    private static String capitalize(String s) {
        char[] temp = s.toCharArray();
        temp[0] = Character.toUpperCase(temp[0]);
        String out = "";
        for (char c : temp)
            out += c;
        return out;
    }

    private static boolean equal(String a, String b) {
        if (a.length() != b.length())
            return false;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i))
                return false;
        }
        return true;
    }

}
