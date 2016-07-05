package de.tse.launch;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Tino on 16.06.2016.
 */
public class StartTomcat {

    private static final int TOMCAT_PORT = 8123;
    private static final String WAR_FILE_NAME = "mywebapp.war";
    private static final String CONTEXT_PATH = "/mywebapp";

    public static void main(String[] args) throws ServletException, LifecycleException, URISyntaxException, IOException {

        /**
         * # Start from IDE #
         * If you want to run this main from your IDE, configure your RunConfig with '-DrunFromIDE' as VM Option
         *
         * # Info #
         * All webapp related Modules (like 'webapp-code') should be referenced by the 'webapp-modules' Module,
         *  because 'webapp-modules' is referenced by 'webapp' and 'webapp-launcher'.
         *
         * # URLs #
         * http://localhost:8123/mywebapp/
         * http://localhost:8123/mywebapp/myservlet
         */
        new StartTomcat().start();
    }

    public void start() throws ServletException, LifecycleException, URISyntaxException, IOException {

        System.setProperty("tomcat.util.scan.DefaultJarScanner.jarsToSkip", "*");
        System.setProperty("org.apache.catalina.startup.ContextConfig.jarsToSkip", "*");
        System.setProperty("org.apache.catalina.startup.TldConfig.jarsToSkip", "*");

        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(TOMCAT_PORT);

        final RunMode mode = getRunMode();

        final Path absolutePath = detectPath(mode);

        final StandardContext context = (StandardContext) tomcat.addWebapp(CONTEXT_PATH, absolutePath.toString());

        tomcat.start();
        tomcat.getServer().await();

        // While starting the embedded Tomcat with the WAR, an Error orrcus:
        //      SEVERE: Exception fixing docBase for context [/mywebapp]
        //      java.io.IOException: Unable to create the directory [...\webapps\mywebapp]
        //      at org.apache.catalina.startup.ExpandWar.expand(ExpandWar.java:115)
        //      at org.apache.catalina.startup.ContextConfig.fixDocBase(ContextConfig.java:618)
        //      at org.apache.catalina.startup.ContextConfig.beforeStart(ContextConfig.java:744)
        //      at org.apache.catalina.startup.ContextConfig.lifecycleEvent(ContextConfig.java:307)
        //      ...
        //
        // When the 'webapps/mywebapp' Folder is created manually, the Exception will not occur, but
        //  the Applications does not run.
    }

    private RunMode getRunMode() {
        if (System.getProperty("runFromIDE") != null) {
            return RunMode.FROM_IDE;
        }
        return RunMode.WITH_WAR;
    }

    private Path detectPath(final RunMode mode) {
        switch (mode) {
            case FROM_IDE:
                return Paths.get("webapp/src/main/webapp").normalize().toAbsolutePath();
            case WITH_WAR:
                return Paths.get(WAR_FILE_NAME).normalize().toAbsolutePath();
            default:
                throw new IllegalArgumentException("No Path for RunMode supported");
        }
    }
}
