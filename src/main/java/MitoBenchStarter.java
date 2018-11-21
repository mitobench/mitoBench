import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import view.MitoBenchWindow;

/**
 * Created by neukamm on 03.11.16.
 */
public class MitoBenchStarter {

    /**
     * starts the main Mitobench window
     *
     * @param args
     */
    public static void main(String[] args)
    {
        SvgImageLoaderFactory.install();
        new Thread(() -> Application.launch(MitoBenchWindow.class)).start();

    }
}
