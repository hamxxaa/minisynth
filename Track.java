import java.io.ByteArrayOutputStream;

public class Track {
    public String name;
    public int sampleRate = 44100;
    public double bpm;
    public String waveform;
    public double bps;
    public double quarterNote;
    public double halfNote;
    public double wholeNote;
    public ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
    public int depth = 127;
    public double volume = 0.3;

    public Track(String name, int sampleRate, double bpm, String waveform) {
        this.name = name;
        this.sampleRate = sampleRate;
        this.bpm = bpm;
        this.bps = bpm / 60;
        this.quarterNote = 60 / bpm;
        this.halfNote = 2 * quarterNote;
        this.wholeNote = 4 * quarterNote;
        this.waveform = waveform;
        // Add a small amount of silence at the beginning
        int silenceSamples = (int) (sampleRate * 0.1); // 100ms of silence
        for (int i = 0; i < silenceSamples; i++) {
            bufferStream.write((byte) 0);
        }
    }

    public Track(String name, double bpm, String waveform) {
        this(name, 44100, bpm, waveform); // Default sample rate is 44100
    }

    public void addNote(Sound sound) {
        for (int i = 0; i < sampleRate * sound.duration; i++) {
            double angle = 2.0 * Math.PI * i * sound.frequency / sampleRate;
            bufferStream.write(createWave(angle));
        }
    }

    public void setVolume(double vol) {
        this.volume = vol;
    }

    public ByteArrayOutputStream getbufferStream() {
        return bufferStream;
    }

    public byte[] getbufferStreamAsArray() {
        return bufferStream.toByteArray();
    }

    public int getBufferSize() {
        return bufferStream.size();
    }

    private byte createWave(double angle) {
        switch (waveform) {
            case "SINE":
                return (byte) (Math.sin(angle) * depth * volume);
            case "SQUARE":
                return (byte) (Math.signum(Math.sin(angle)) * depth * volume);
            case "SAWTOOTH":
                return (byte) (angle % (2 * Math.PI) / Math.PI * depth * volume - depth);
            case "TRIANGLE":
                return (byte) (Math.abs((angle % (2 * Math.PI)) / Math.PI * depth * volume - depth) - depth);
            default:
                return (byte) 0;
        }
    }

}
