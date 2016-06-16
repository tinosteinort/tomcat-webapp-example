package de.tse.launch;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.ServletException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Tino on 16.06.2016.
 */
public class StartTomcat {

    public static void main(String[] args) throws ServletException, LifecycleException {

        System.setProperty("tomcat.util.scan.DefaultJarScanner.jarsToSkip", "*");
        System.setProperty("org.apache.catalina.startup.ContextConfig.jarsToSkip", "*");
        System.setProperty("org.apache.catalina.startup.TldConfig.jarsToSkip", "*");

        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(8123);

        final Path absolutePath = Paths.get("webapp/src/main/webapp").normalize();
        final StandardContext context = (StandardContext) tomcat.addWebapp("/webapp", absolutePath.normalize().toAbsolutePath().toString());


        final File additionWebInfClasses = new File("webapp-code/target/classes");
        final WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
}
