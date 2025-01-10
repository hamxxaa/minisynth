import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.*;

public class SoundGenerator {
    public ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();

    public SoundGenerator() {
    }

    public static void main(String[] args) throws LineUnavailableException {

        SoundGenerator soundGenerator = new SoundGenerator();

        Track track1 = new Track("1", 100, "SAWTOOTH");
        track1.addNote(new Sound(SoundGenerator.Note.A4_SHARP.frequency, track1.quarterNote));
        track1.addNote(new Sound(SoundGenerator.Note.F4.frequency * 2, track1.quarterNote));
        track1.addNote(new Sound(SoundGenerator.Note.G4.frequency, track1.quarterNote));
        track1.addNote(new Sound(SoundGenerator.Note.E4.frequency * 2, track1.wholeNote));
        Track track2 = new Track("2", 100, "SQUARE");
        track2.addNote(new Sound(SoundGenerator.Note.A4_SHARP.frequency / 16, track2.quarterNote));
        track2.addNote(new Sound(SoundGenerator.Note.F4.frequency / 8, track2.quarterNote));
        track2.addNote(new Sound(SoundGenerator.Note.G4.frequency / 16, track2.quarterNote));
        track2.addNote(new Sound(SoundGenerator.Note.E4.frequency / 8, track2.halfNote));
        track2.addNote(new Sound(0, track2.halfNote));
        // track2.addNote(new Sound(SoundGenerator.Note.E4.frequency / 8, track2.halfNote));

        // soundGenerator.playTrack(track1);
        // track1.setVolume(1.0);
        // soundGenerator.playTrack(track2);
        soundGenerator.combineTracks(List.of(track1, track2));

    }

    public void playTrack(Track track) {
        try {
            byte[] buffer = track.getbufferStream().toByteArray();
            AudioFormat format = new AudioFormat(track.sampleRate, 8, 1, true, false);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();
            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    private void playBuffer(byte[] buffer, int sampleRate) {
        try {
            AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, false);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();
            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    public void combineTracks(List<Track> trackList) {
        List<byte[]> buffers = new ArrayList<>();
        int maxBufferSize = 0;
        for (Track track : trackList) {
            maxBufferSize = Math.max(maxBufferSize, track.getBufferSize());
            buffers.add(track.getbufferStreamAsArray());
        }

        byte[] combinedBuffer = new byte[maxBufferSize];

        for (int i = 0; i < maxBufferSize; i++) {
            int combinedValue = 0;

            for (byte[] buffer : buffers) {
                if (i < buffer.length) {
                    combinedValue += buffer[i];
                }
            }
            combinedValue = Math.min(Math.max(combinedValue / trackList.size(), -128), 127);
            combinedBuffer[i] = (byte) combinedValue;
        }
        int maxSampleRate = 0;
        for (Track track : trackList) {
            maxSampleRate = Math.max(maxSampleRate, track.sampleRate);
        }
        playBuffer(combinedBuffer, maxSampleRate);
    }

    public enum Note {
        C4(261.63), C4_SHARP(277.18), D4(293.66), D4_SHARP(311.13), E4(329.63),
        F4(349.23), F4_SHARP(369.99), G4(392.00), G4_SHARP(415.30), A4(440.00),
        A4_SHARP(466.16), B4(493.88),;

        public final double frequency;

        Note(double frequency) {
            this.frequency = frequency;
        }
    }
}
