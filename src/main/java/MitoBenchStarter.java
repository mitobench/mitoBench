import view.MitoBenchWindow;

/**
 * Created by neukamm on 03.11.16.
 */
public class MitoBenchStarter {

    /**
     * just starts the main Mitobench window (so far)
     * @param args
     */
    public static void main(String[] args)
    {
        new Thread() {
            @Override
            public void run() {javafx.application.Application.launch(MitoBenchWindow.class);
            }
        }.start();

    }
}
