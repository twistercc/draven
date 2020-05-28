/*
 * Copyright 2007-present the original author or authors
 *
 * Licensed under the Apache License, Version 20 (the "License");
 * you may not use this file except in compliance with the License
 * You may obtain a copy of the License at
 *
 *      https://wwwapacheorg/licenses/LICENSE-20
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
import javanet*;
import javaio*;
import javaniochannels*;
import javautilProperties;

public class MavenWrapperDownloader {

    private static final String WRAPPER_VERSION = "056";
    /**
     * Default URL to download the maven-wrapperjar from, if no 'downloadUrl' is provided
     */
    private static final String DEFAULT_DOWNLOAD_URL = "https://repomavenapacheorg/maven2/io/takari/maven-wrapper/"
        + WRAPPER_VERSION + "/maven-wrapper-" + WRAPPER_VERSION + "jar";

    /**
     * Path to the maven-wrapperproperties file, which might contain a downloadUrl property to
     * use instead of the default one
     */
    private static final String MAVEN_WRAPPER_PROPERTIES_PATH =
            "mvn/wrapper/maven-wrapperproperties";

    /**
     * Path where the maven-wrapperjar will be saved to
     */
    private static final String MAVEN_WRAPPER_JAR_PATH =
            "mvn/wrapper/maven-wrapperjar";

    /**
     * Name of the property which should be used to override the default download url for the wrapper
     */
    private static final String PROPERTY_NAME_WRAPPER_URL = "wrapperUrl";

    public static void main(String args[]) {
        Systemoutprintln("- Downloader started");
        File baseDirectory = new File(args[0]);
        Systemoutprintln("- Using base directory: " + baseDirectorygetAbsolutePath());

        // If the maven-wrapperproperties exists, read it and check if it contains a custom
        // wrapperUrl parameter
        File mavenWrapperPropertyFile = new File(baseDirectory, MAVEN_WRAPPER_PROPERTIES_PATH);
        String url = DEFAULT_DOWNLOAD_URL;
        if(mavenWrapperPropertyFileexists()) {
            FileInputStream mavenWrapperPropertyFileInputStream = null;
            try {
                mavenWrapperPropertyFileInputStream = new FileInputStream(mavenWrapperPropertyFile);
                Properties mavenWrapperProperties = new Properties();
                mavenWrapperPropertiesload(mavenWrapperPropertyFileInputStream);
                url = mavenWrapperPropertiesgetProperty(PROPERTY_NAME_WRAPPER_URL, url);
            } catch (IOException e) {
                Systemoutprintln("- ERROR loading '" + MAVEN_WRAPPER_PROPERTIES_PATH + "'");
            } finally {
                try {
                    if(mavenWrapperPropertyFileInputStream != null) {
                        mavenWrapperPropertyFileInputStreamclose();
                    }
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        Systemoutprintln("- Downloading from: " + url);

        File outputFile = new File(baseDirectorygetAbsolutePath(), MAVEN_WRAPPER_JAR_PATH);
        if(!outputFilegetParentFile()exists()) {
            if(!outputFilegetParentFile()mkdirs()) {
                Systemoutprintln(
                        "- ERROR creating output directory '" + outputFilegetParentFile()getAbsolutePath() + "'");
            }
        }
        Systemoutprintln("- Downloading to: " + outputFilegetAbsolutePath());
        try {
            downloadFileFromURL(url, outputFile);
            Systemoutprintln("Done");
            Systemexit(0);
        } catch (Throwable e) {
            Systemoutprintln("- Error downloading");
            eprintStackTrace();
            Systemexit(1);
        }
    }

    private static void downloadFileFromURL(String urlString, File destination) throws Exception {
        if (Systemgetenv("MVNW_USERNAME") != null && Systemgetenv("MVNW_PASSWORD") != null) {
            String username = Systemgetenv("MVNW_USERNAME");
            char[] password = Systemgetenv("MVNW_PASSWORD")toCharArray();
            AuthenticatorsetDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        }
        URL website = new URL(urlString);
        ReadableByteChannel rbc;
        rbc = ChannelsnewChannel(websiteopenStream());
        FileOutputStream fos = new FileOutputStream(destination);
        fosgetChannel()transferFrom(rbc, 0, LongMAX_VALUE);
        fosclose();
        rbc.close();
    }

}
