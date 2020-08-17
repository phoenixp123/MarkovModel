public class MarkovModel {
    ST<String, Integer> mm = new ST<String, Integer>();
    ST<String, int[]> nc = new ST<String, int[]>();
    int order;

    // creates a Markov model of order k for the specified text
    public MarkovModel(String text, int k) {        //text = asdasdfgas
        order = k;
        String newText = text + text.substring(0, k);
        int length = text.length();
        for (int i = 0; i < length; i++) {
            String kg = newText.substring(i, i + k);
            char nextChar = newText.charAt(i + k);
            if (!nc.contains(kg)) {
                int[] ascii = new int[128];
                ascii[(int) nextChar]++;
                mm.put(kg, 1);
                nc.put(kg, ascii);
            } else {
                int[] check = nc.get(kg);
                check[(int) nextChar]++;
                nc.put(kg, check);
                mm.put(kg, mm.get(kg) + 1);
            }
        }
    }

    // returns the order k of this Markov model
    public int order() {
        return order;
    }

    // returns a string representation of the Markov model (as described below)
    public String toString() {
        String str = "";
        int[] frequency;
        char c = 0;
        int frq = 0;
        for (String kgram : nc.keys()) {
            str += kgram + ":";
            frequency = nc.get(kgram);
            for (int i = 0; i < frequency.length; i++) {
                if (frequency[i] != 0) {
                    c = (char) i;
                    frq = frequency[i];
                    str += " " + c + " " + frq;
                }
            }
            str += "\n";
        }
        return str;
    }

    // returns the number of times the specified kgram appears in the text
    public int freq(String kgram) throws IllegalArgumentException {
        if (kgram.length() != order) {
            throw new IllegalArgumentException("kgram exceeds length k");
        }
        if (mm.contains(kgram)) {
            return (int) mm.get(kgram);
        }
        return 0;
    }

    // returns the number of times the character c follows the specified
    // kgram in the text
    public int freq(String kgram, char c) throws IllegalArgumentException {
        int[] charFreq = nc.get(kgram);
        if (kgram.length() != order) {
            throw new IllegalArgumentException(kgram + c + "argument exceeds length k");
        } else {
            return charFreq[(int) c];
        }
    }


    // returns a random character that follows the specified kgram in the text,
    // chosen with weight proportional to the number of times that character
    // follows the specified kgram in the text
    public char random(String kgram) {
        int[] frequency = nc.get(kgram);
        if (kgram.length() != order || !nc.contains(kgram)) {
            throw new IllegalArgumentException("Kgram is not of length k");
        }

        int rand = StdRandom.discrete(frequency);
        return (char) rand;
    }

    public static void main(String[] args) {
        String text1 = "banana";
        MarkovModel model1 = new MarkovModel(text1, 2);
        StdOut.println(model1);
        StdOut.println("freq(\"an\", 'a')    = " + model1.freq("an", 'a'));
        StdOut.println("freq(\"na\", 'b')    = " + model1.freq("na", 'b'));
        StdOut.println("freq(\"na\", 'a')    = " + model1.freq("na", 'a'));
        StdOut.println("freq(\"na\")         = " + model1.freq("na"));
        StdOut.println();
        String text3 = "one fish two fish red fish blue fish";
        MarkovModel model3 = new MarkovModel(text3, 4);
        StdOut.println("freq(\"ish \", 'r') = " + model3.freq("ish ", 'r'));
        StdOut.println("freq(\"ish \", 'x') = " + model3.freq("ish ", 'x'));
        StdOut.println("freq(\"ish \")      = " + model3.freq("ish "));
        StdOut.println("freq(\"tuna\")      = " + model3.freq("tuna"));
    }
}
