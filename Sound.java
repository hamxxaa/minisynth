public class Sound {
    public double frequency;
    public double duration;
    public double attack;
    public double sustain;
    public double release;

    public Sound(double frequency, double duration, double attack, double sustain, double release) {
        this.frequency = frequency;
        this.duration = duration;
        this.attack = attack;
        this.sustain = sustain;
        this.release = release;

    }

    public Sound(double frequency, double duration) {
        this(frequency, duration, 0, 1, 0);
    }

    public Sound(double duration) {
        this(0, duration, 0, 1, 0);
    }
}