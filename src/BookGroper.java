
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/*                                     UWAGA!

    W związku nieudanego połącznia z bazą danych Derby (Remote, ClientDriver) na lokalhoście
    zorganizowałem połączenie z Derby (Embedded, EmbeddedDriver), któwa mieści się w środku projektu
    w folderze db. Dla tego dla poprawnego działania programu należy w metodzie doPost()
    zmienić zawartośc zmiennej URL (czyli podać ścieżkę do tej bazy danych)

    jdbc:derby:[ścieżka]






*/



public class BookGroper extends HttpServlet {
    Connection con;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        printWriter.write("<html>\n" +
                "<head>\n" +
                "<title>Book Groper</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div align=\"center\">\n" +
                "<h1>Book Groper</h1>\n" +
                "<form method=\"post\">\n" +
                "<p><strong>Search by name: </strong><input type=\"text\" name=\"book\" size=\"30\"></p>\n" +
                "<p><input type=\"submit\" name=\"submit\"></p>\n" +
                "</form>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String book = req.getParameter("book");
        String out = "<html>\n" +
                "<head>\n" +
                "<title>Search results</title>\n" +
                "<style>\n" +
                "   .brd {\n" +
                "    border: 4px double black; /* Параметры границы */\n" +
                "    background: #fc3; /* Цвет фона */\n" +
                "    padding: 10px; /* Поля вокруг текста */\n" +
                "   }\n" +
                "  </style>" +
                "</head>\n" +
                "<body>\n" +
                "<div align=\"center\">\n" +
                "<h1>Book Groper</h1>\n" +
                "<form method=\"post\">\n" +
                "<p><strong>Search by name: </strong><input maxlength=\"25\" type=\"text\" name=\"book\" size=\"30\"></p>\n" +
                "<p><input type=\"submit\" name=\"submit\"></p>\n" +
                "</form>\n" +
                "</div>\n" +
                "\n" +
                "<h3>Results for request: </h3>";


        String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
        //TODO: wskazać właściwą ścieżkę do bazy danych
        String URL = "jdbc:derby:D:/Documents/pjatk/4sem/tpo/U_WEBAPP_BOOKDB/db";
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ISBN, TYTUL, ROK, A.NAME, W.NAME AS \"WYDNAME\", ROK, CENA FROM POZYCJE P JOIN AUTOR A on P.AUTID = A.AUTID JOIN WYDAWCA W on P.WYDID = W.WYDID WHERE TYTUL LIKE \'%" + book + "%\'");


            while (rs.next()) {
//                System.out.println(rs.getString("TYTUL"));

                out += "<div class=\"brd\">";
                out += "<p>Name: <i>"+rs.getString("TYTUL") + "</i></p>" +
                "<p>ISBN: " + rs.getString("ISBN") + "</p>" +
                "<p>Year of publication: " + rs.getString("ROK") + "</p>" +
                        "<p>Autor: " + rs.getString("NAME") + "</p>" +
                        "<p>Price: " + rs.getString("CENA") + "</p>" +
                        "<p>Publisher: " + rs.getString("WYDNAME") + "</p>";



                out += "</div>";
            }



            if (con != null) con.close();
        } catch (Exception exc) {
            System.out.println(exc);
        }
        out += "</body>\n" +
                "</html>";


        PrintWriter printWriter = resp.getWriter();
        printWriter.write(out);
    }
}

