import javafx.application.Application;
import view.MitoBenchWindow;


public class Launcher {

    public static void main(String[] args) {
        new Thread(() -> Application.launch(MitoBenchWindow.class)).start();

    }
}
