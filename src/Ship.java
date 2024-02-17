public class Ship {
    private final int length;
    private final String name;
    private int timesHit;

    public Ship(int length, String name) {
        this.length = length;
        this.name = name;
        timesHit = 0;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public int getTimesHit() {
        return timesHit;
    }

    public void setTimesHit(int timesHit) {
        this.timesHit = timesHit;
    }
}
