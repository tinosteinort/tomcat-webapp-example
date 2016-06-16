package de.tse.webappcode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Tino on 16.06.2016.
 */
public class WebAppServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = new PrintWriter(resp.getOutputStream())) {

            out.write("Ordner:\n");
            out.write("-------\n");
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("."))) {

                for (Path dir : directoryStream) {

                    out.write(dir.toString() + "\n") ;
                }
            }
            out.write("-------\n");
        }
    }

}
