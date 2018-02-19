import java.util.ArrayList;
public class Cut {
  // Fields
  public static final double SAMPLE_RATE = 22050.0;
  private double[] originalSamples; // array of doubles generated prior to being stripped
  private double totalTime; // total time of reading in file

  private ArrayList<Double> splits; // array of doubles which are potential splittable times
  // The total number of bytes between each wave of audio data.
  private ArrayList<Integer> doublesPerGap = new ArrayList<Integer>(); // holds # doubles between splits, useful for conversion to time data
  private ArrayList<Double> times = new ArrayList<Double>();
  // Instantiation
  public Cut(String filename) {
    this.originalSamples = StdAudio.read(filename);
    this.splits = possibleSplits();
    this.totalTime = (StdAudio.readByte(filename)).length/SAMPLE_RATE;
    this.convertTime();
  }
  /*
  Generating double values for which there could be a gap in conversation. For now,
  this method returns an array of doubles at which the amplitude of the
  sound wave is zero. (In other words, points where there is no sound being produced.)
  */
  private ArrayList<Double> possibleSplits() {
    ArrayList<Double> negativeSamples = new ArrayList<Double>();
    ArrayList<Double> positiveSamples = new ArrayList<Double>();
    ArrayList<Double> nilSamples = new ArrayList<Double>();

    /*
    numDoublesPerGap is set. The below is the algorithm for determining how many
    "samples" there are between zeroes.
    */
    int numDoublesPerGap = 0;
    for (int i = 0; i < originalSamples.length; i++) {
      double val = originalSamples[i];

      numDoublesPerGap++;

      if (val < 0) {
        negativeSamples.add(val);
      }
      else if (val > 0) {
        positiveSamples.add(val);
      }
      else {
        nilSamples.add(val);
        doublesPerGap.add(numDoublesPerGap);
        numDoublesPerGap = 0;
      }
    }

    return nilSamples;
  }
  /*
  Converts the number of doubles between zeroes (doublesPerGap) into times
  so as to create human-readable "flags".
  */
  private void convertTime() {
    double d = 0.0;

    for (Integer numBytes: this.doublesPerGap) {
      d = numBytes / SAMPLE_RATE;
      times.add(d);
    }
  }
  // Getter methods; returns values of fields.
  public ArrayList<Double> getSplits() {
    return splits;
  }

  public ArrayList<Double> getTimes() {
    return times;
  }

  public double getTotalTime() {
    return totalTime;
  }

  public static void main(String[] args) {
    String tempFile = "1-welcome.wav";
    Cut c = new Cut(tempFile);
    System.out.println(c.getTimes());
    double f = 0;
    for(int i = 0; i < c.getTimes().size(); i++) {
      f += c.getTimes().get(i);
    }
    System.out.println(c.getTotalTime());
  }
}