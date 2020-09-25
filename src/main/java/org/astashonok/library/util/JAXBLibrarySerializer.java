package org.astashonok.library.util;

import org.astashonok.library.model.Library;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class JAXBLibrarySerializer {

    public static Library unmarshal(String fileName) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Library.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Library) unmarshaller.unmarshal(new File(fileName));
    }

    public static void marshal(Library library, String fileName) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Library.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(library, new File(fileName));
    }

}
