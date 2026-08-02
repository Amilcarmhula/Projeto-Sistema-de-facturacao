package jminvsm.fiscal.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SaftXmlValidator {

    public List<String> validate(Path xml, InputStream xsd) throws IOException {
        if (xsd == null) throw new IOException("XSD SAF-T provisório não encontrado.");
        final List<String> errors = new ArrayList<String>();
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            validator.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException ex) { errors.add(format("AVISO", ex)); }
                public void error(SAXParseException ex) { errors.add(format("ERRO", ex)); }
                public void fatalError(SAXParseException ex) { errors.add(format("FATAL", ex)); }
            });
            validator.validate(new StreamSource(xml.toFile()));
        } catch (SAXException ex) {
            errors.add("ERRO: " + ex.getMessage());
        }
        return Collections.unmodifiableList(errors);
    }

    private static String format(String severity, SAXParseException ex) {
        return severity + " linha " + ex.getLineNumber() + ", coluna "
                + ex.getColumnNumber() + ": " + ex.getMessage();
    }
}

