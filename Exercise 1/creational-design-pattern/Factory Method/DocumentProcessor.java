import java.util.logging.Logger;

abstract class Document {
    protected String content;
    protected static final Logger LOGGER = Logger.getLogger(Document.class.getName());

    public abstract void open();
    public abstract void save();
    public abstract void close();

    public void setContent(String content) {
        this.content = content;
    }
}

class PDFDocument extends Document {
    @Override
    public void open() {
        LOGGER.info("Opening PDF document");
    }

    @Override
    public void save() {
        LOGGER.info("Saving PDF document");
    }

    @Override
    public void close() {
        LOGGER.info("Closing PDF document");
    }
}

class WordDocument extends Document {
    @Override
    public void open() {
        LOGGER.info("Opening Word document");
    }

    @Override
    public void save() {
        LOGGER.info("Saving Word document");
    }

    @Override
    public void close() {
        LOGGER.info("Closing Word document");
    }
}

abstract class DocumentFactory {
    public abstract Document createDocument();

    public Document processDocument(String content) {
        Document doc = createDocument();
        doc.open();
        doc.setContent(content);
        doc.save();
        doc.close();
        return doc;
    }
}

class PDFFactory extends DocumentFactory {
    @Override
    public Document createDocument() {
        return new PDFDocument();
    }
}

class WordFactory extends DocumentFactory {
    @Override
    public Document createDocument() {
        return new WordDocument();
    }
}

public class DocumentProcessor {
    private static final Logger LOGGER = Logger.getLogger(DocumentProcessor.class.getName());

    public static void main(String[] args) {
        try {
            DocumentFactory pdfFactory = new PDFFactory();
            DocumentFactory wordFactory = new WordFactory();

            pdfFactory.processDocument("This is a PDF document.");
            wordFactory.processDocument("This is a Word document.");
        } catch (Exception e) {
            LOGGER.severe("An error occurred during document processing: " + e.getMessage());
        }
    }
}