// Do not submit with package statements if you are using eclipse.
// Only use what is provided in the standard libraries.

import java.io.*;
import java.util.*;

public class NaiveBayes {
    private Map<String, Double> mapSpamProb = new HashMap<String, Double>();
    private Map<String, Double> mapHamProb = new HashMap<String, Double>();
    private double hamsSize;
    private double spamsSize;


    /*
     * !! DO NOT CHANGE METHOD HEADER !!
     * If you change the method header here, our grading script won't
     * work and you will lose points!
     *
     * Train your Naive Bayes Classifier based on the given training
     * ham and spam emails.
     *
     * Params:
     *      hams - email files labeled as 'ham'
     *      spams - email files labeled as 'spam'
     */
    public void train(File[] hams, File[] spams) throws IOException {
        hamsSize = hams.length;
        spamsSize = spams.length;
        mapSpamProb = prob(spams, spamsSize);    //1
        mapHamProb = prob(hams, hamsSize);     //2

    }


    private Map<String, Double> prob(File[] emails, double total) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        for (File f : emails) {
            for (String token : tokenSet(f)) {
                if (map.containsKey(token)) {
                    map.put(token, map.get(token) + 1);
                } else {
                    map.put(token, 1);
                }
            }
        }
        Map<String, Double> probMap = new HashMap<>();
        for (String token : map.keySet()) {
            probMap.put(token, (map.get(token) + 1.0) / (total + 2.0));
        }

        return probMap;
    }


    /*
     * !! DO NOT CHANGE METHOD HEADER !!
     * If you change the method header here, our grading script won't
     * work and you will lose points!
     *
     * Classify the given unlabeled set of emails. Add each email to the correct
     * label set. SpamFilterMain.java would follow the format in
     * example_output.txt and output your result to stdout. Note the order
     * of the emails in the output does NOT matter.
     *
     *
     * Params:
     *      emails - unlabeled email files to be classified
     *      spams  - set for spam emails that needs to be populated
     *      hams   - set for ham emails that needs to be populated
     */
    public void classify(File[] emails, Set<File> spams, Set<File> hams) throws IOException {
        HashSet<String> emailsSet = new HashSet<>();

        double a = hamsSize;
        double b = spamsSize;
        double emailsSpam = Math.log(b / (b + a));  //3
        double emailsHam = Math.log(a / (b + a)); //4

        for (int i = 0; i < emails.length; i++) {
            emailsSet = tokenSet(emails[i]);
            double sumSpam = emailsSpam;
            double sumHam = emailsHam;


            for (String ele : emailsSet) {

                if (mapSpamProb.containsKey(ele)){
                    sumSpam  = sumSpam + Math.log(mapSpamProb.get(ele));
                } else {
                    sumSpam = sumSpam + Math.log(1.0 / (2.0 + b));
                }

                if (mapHamProb.containsKey(ele)){
                    sumHam  = sumHam + Math.log(mapHamProb.get(ele));
                } else {
                    sumHam = sumHam + Math.log(1.0 / (2.0 + a));
                }

            }

            if (sumSpam > sumHam) {
                spams.add(emails[i]);
            } else {
                hams.add(emails[i]);
            }


        }

    }


    /*
     *  Helper Function:
     *  This function reads in a file and returns a set of all the tokens.
     *  It ignores "Subject:" in the subject line.
     *
     *  If the email had the following content:
     *
     *  Subject: Get rid of your student loans
     *  Hi there ,
     *  If you work for us , we will give you money
     *  to repay your student loans . You will be
     *  debt free !
     *  FakePerson_22393
     *
     *  This function would return to you
     *  ['be', 'student', 'for', 'your', 'rid', 'we', 'of', 'free', 'you',
     *   'us', 'Hi', 'give', '!', 'repay', 'will', 'loans', 'work',
     *   'FakePerson_22393', ',', '.', 'money', 'Get', 'there', 'to', 'If',
     *   'debt', 'You']
     */
    public static HashSet<String> tokenSet(File filename) throws IOException {
        HashSet<String> tokens = new HashSet<String>();
        Scanner filescan = new Scanner(filename);
        filescan.next(); // Ignoring "Subject"
        while (filescan.hasNextLine() && filescan.hasNext()) {
            tokens.add(filescan.next());
        }
        filescan.close();
        return tokens;
    }
}
