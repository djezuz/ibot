/*
 * Copyright 2011 Vincent Behar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.vbehar.ibot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Hold the program's configuration
 * 
 * @author Vincent Behar
 */
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Name of the IRC Bot */
    public static final transient String IRC_BOT_NAME = "irc.bot.name";

    /** Hostname of the IRC server to connect to */
    public static final transient String IRC_SERVER_HOSTNAME = "irc.server.hostname";

    /** Port of the IRC server to connect to */
    public static final transient String IRC_SERVER_PORT = "irc.server.port";

    /** Name of the IRC channel to join */
    public static final transient String IRC_CHANNEL = "irc.channel";

    /** The configuration is stored as a Properties object */
    private final Properties configuration;

    /**
     * Instantiate a new {@link Configuration} object, with the data from the given file
     * 
     * @param configurationFilePath path of the configuration file (should be in the java-properties format)
     * @throws IOException in case of error while reading the file
     */
    public Configuration(String configurationFilePath) throws IOException {
        super();
        configuration = new Properties();
        File configurationFile = new File(configurationFilePath);
        FileInputStream configurationStream = null;
        try {
            configurationStream = FileUtils.openInputStream(configurationFile);
            configuration.load(configurationStream);
        } finally {
            IOUtils.closeQuietly(configurationStream);
        }
    }

    /**
     * Get a configuration value as a String
     * 
     * @param key
     * @return the value, or null if not found
     */
    public String get(String key) {
        return configuration.getProperty(key);
    }

    /**
     * Get a configuration value as an Integer
     * 
     * @param key
     * @return the value, or null if not found or if not a valid Integer
     */
    public Integer getInteger(String key) {
        String value = configuration.getProperty(key);
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
