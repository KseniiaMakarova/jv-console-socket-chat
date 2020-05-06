import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class MessageStory {
    private LinkedList<String> story = new LinkedList<>();

    public void addToStory(String message) {
        if (story.size() >= 10) {
            story.removeFirst();
        }
        story.add(message);
    }

    public void printStory(BufferedWriter writer) throws IOException {
        if (story.size() > 0) {
            writer.write("History messages" + "\n");
            for (String message : story) {
                writer.write(message + "\n");
            }
            writer.flush();
        }
    }
}
