package io.writer;

import Logging.LogClass;
import javafx.application.Platform;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

/**
 * PDF Export for javafx nodes. Here, it is used to export charts as pdf to user-defined file. Unfortunately, the Page
 * layout cannot be set appropriately. This is reported as JDK-8088509 on the JDK Bug System.
 */
public class PDFExporter {

    private Printer pdfPrinter;
    private Node printImage;
    private Stage owner;
    protected org.apache.log4j.Logger LOG;
    protected LogClass logClass;

    /**
     * PDF Export for javafx nodes. Here, it is used to export charts as pdf to user-defined file. Unfortunately, the
     * Page layout cannot be set appropriately. This is reported as JDK-8088509 on the JDK Bug System.
     * @param image JavaFX node
     * @param owner Stage owning the print dialog. Or null, if no stage should own the print dialog.
     */
    public PDFExporter(Node image, Stage owner, LogClass logClass) {
        this.printImage = image;
        this.owner = owner;
        this.logClass = logClass;
        this.LOG = logClass.getLogger(this.getClass());
    }

    /**
     * Write node image to pdf
     */
    public void print() {
        PrinterJob job = PrinterJob.createPrinterJob();
        boolean executePrint = true;
        findPrinter();
        // Detect error occurring on MacOS
        if(job == null) {
            Platform.runLater(() -> {
                Notifications.create()
                        .text("Could not export to PDF file. " +
                                "The operating system requires at least one printer to be installed.")
                        .title("No printer")
                        .showWarning();
            });
            return;
        }
        if(pdfPrinter != null) {
            job.setPrinter(pdfPrinter);
        } else {
            executePrint = job.showPrintDialog(owner);
            pdfPrinter = job.getPrinter();
        }
        if(!executePrint) {
            this.LOG.info("Cancelled PDF export");
            return;
        }

//        Paper customPaper = PrintHelper.createPaper("Custom", printImage.getLayoutBounds().getWidth(),
//                printImage.getLayoutBounds().getHeight(), Units.POINT);
//        PageLayout layout = pdfPrinter.createPageLayout(customPaper, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        // TODO: 30.05.2017 improve or wait until bug JDK-8088509 is fixed. Alternatively:
//        job.showPageSetupDialog(new Stage());
//        Paper paper = PrintHelper.createPaper("CustomSize", 600,400, Units.POINT);
        PageLayout layout = pdfPrinter.createPageLayout(Paper.A5, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
//        job.showPageSetupDialog(stage);
        job.getJobSettings().setPageLayout(layout);

        // Scale image to paper size (A4)
        double scaleX = layout.getPrintableWidth() / printImage.getLayoutBounds().getWidth();
        double scaleY = layout.getPrintableHeight() / printImage.getLayoutBounds().getHeight();
        if(scaleX > scaleY) {
            scaleX = scaleY;
        } else {
            scaleY = scaleX;
        }
        printImage.getTransforms().add(new Scale(scaleX, scaleY));

        boolean printSpooled = job.printPage(layout, printImage);
        if(printSpooled) {
            job.endJob();
            this.LOG.info("Wrote Image to PDF successfully");
        } else {
            this.LOG.info("Error writing PDF");
        }
        printImage.getTransforms().add(new Scale(1 / scaleX, 1 / scaleY));
    }

    /**
     * Lookup all available printers and return the pdf printer if available.
     */
    private void findPrinter() {
        for(Printer printer : Printer.getAllPrinters()) {
            if(printer.getName().endsWith("PDF")) {
                pdfPrinter = printer;
            }
        }
    }
}