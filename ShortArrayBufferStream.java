import java.util.ArrayList;
import java.util.List;

public class ShortArrayBufferStream {
    private List<Short> buffer = new ArrayList<>();

    public void write(short value) {
        buffer.add(value);
    }

    public short[] toShortArray() {
        short[] result = new short[buffer.size()];
        for (int i = 0; i < buffer.size(); i++) {
            result[i] = buffer.get(i);
        }
        return result;
    }

    public int size() {
        return buffer.size();
    }

    public void clear() {
        buffer.clear();
    }
}