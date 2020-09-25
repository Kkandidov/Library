package org.astashonok.library;

        import org.astashonok.library.model.Author;
        import org.astashonok.library.model.Book;
        import org.astashonok.library.model.Genre;
        import org.astashonok.library.model.Library;
        import org.astashonok.library.util.JAXBLibrarySerializer;
        import org.astashonok.library.util.pool.ConnectionPool;
        import org.astashonok.library.util.pool.DBCPPool;
        import org.astashonok.library.util.pool.Pools;

        import javax.xml.bind.JAXBException;
        import java.sql.Connection;
        import java.sql.SQLException;
        import java.util.Arrays;
        import java.util.Date;

public class Main {
    public static void main(String[] args) throws JAXBException, SQLException {
//        Library library = JAXBLibrarySerializer.unmarshal("D:/Library/src/main/resources/library_storage.xml");
//        for (Book book : library.getBooks()){
//            System.out.println(book);
//        }
//        Book book1 = Book.builder()
//                .id(1)
//                .name("edfefe")
//                .authors(Arrays.asList(
//                        Author.builder()
//                                .id(1)
//                                .name("dferr")
//                                .surname("wdewfrefr")
//                                .build(),
//                        Author.builder()
//                                .id(2)
//                                .name("erer")
//                                .surname("ererer")
//                                .build()
//                ))
//                .genres(Arrays.asList(
//                        Genre.builder()
//                                .id(1)
//                                .name("wewrfer")
//                                .build(),
//                        Genre.builder()
//                                .id(2)
//                                .name("ewerwrewr")
//                                .build()
//                ))
//                .isbn("123456")
//                .date(new Date())
//                .build();
//
//        Book book2 = Book.builder()
//                .id(1)
//                .name("edfefe")
//                .authors(Arrays.asList(
//                        Author.builder()
//                                .id(1)
//                                .name("dferr")
//                                .surname("wdewfrefr")
//                                .build(),
//                        Author.builder()
//                                .id(2)
//                                .name("erer")
//                                .surname("ererer")
//                                .build()
//                ))
//                .genres(Arrays.asList(
//                        Genre.builder()
//                                .id(1)
//                                .name("wewrfer")
//                                .build(),
//                        Genre.builder()
//                                .id(2)
//                                .name("ewerwrewr")
//                                .build()
//                ))
//                .isbn("123456")
//                .date(new Date())
//                .build();
//        Library library = Library.builder()
//                .books(Arrays.asList(book1, book2))
//                .build();
//        JAXBLibrarySerializer.marshal(library, "D:/Library/src/main/resources/storage.xml");

        ConnectionPool cp = Pools.newPool(DBCPPool.class);
        for (int i=0; i<100; i++) {
            Connection connection = cp.getConnection();
            System.out.println(i + " " + connection);
            connection.close();
        }
    }
}
