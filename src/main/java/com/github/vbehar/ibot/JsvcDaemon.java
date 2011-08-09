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

import static com.github.vbehar.ibot.Configuration.IRC_BOT_NAME;
import static com.github.vbehar.ibot.Configuration.IRC_CHANNEL;
import static com.github.vbehar.ibot.Configuration.IRC_SERVER_HOSTNAME;
import static com.github.vbehar.ibot.Configuration.IRC_SERVER_PORT;
import java.io.IOException;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.exception.NickAlreadyInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class started by the daemon (jsvc - see apache commons daemon)
 * 
 * @author Vincent Behar
 */
public class JsvcDaemon {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(JsvcDaemon.class);

    private Configuration configuration;

    private PircBotX ircBot;

    /**
     * For easy testing, just run this main class (simulates jsvc)
     * 
     * @param args
     */
    public static void main(String[] args) {
        JsvcDaemon daemon = new JsvcDaemon();
        daemon.init(args);
        daemon.start();
        System.out.println("Program started ! Press \"Enter\" to quit...");
        System.console().readLine();
        System.out.println("Exiting...");
        daemon.stop();
        daemon.destroy();
        System.exit(0);
    }

    /*
     * JSVC required methods : init - start - stop - destroy
     */

    /**
     * Required by JSVC. Here open configuration files, create a trace file, create ServerSockets, Threads
     * 
     * @param arguments
     */
    public void init(String[] arguments) {
        LOGGER.info("Initializing Configuration...");
        if (arguments != null && arguments.length == 1) {
            try {
                configuration = new Configuration(arguments[0]);
                LOGGER.info("Done !");
            } catch (IOException e) {
                LOGGER.error("Unable to read configuration !", e);
                System.exit(1);
            }
        } else {
            LOGGER.error("Please provide only 1 parameter : the path of the configuration file (ibot.properties) !");
            System.exit(1);
        }

        LOGGER.info("Initializing IRC Bot...");
        ircBot = new PircBotX();
        ircBot.setName(configuration.get(IRC_BOT_NAME));
        LOGGER.info("Done !");
    }

    /**
     * Required by JSVC. Start the Thread, accept incoming connections
     */
    public void start() {
        LOGGER.info("Starting IRC Bot...");
        String hostname = configuration.get(IRC_SERVER_HOSTNAME);
        Integer port = configuration.getInteger(IRC_SERVER_PORT);
        try {
            ircBot.connect(hostname, port);
            ircBot.joinChannel(configuration.get(IRC_CHANNEL));
            LOGGER.info("Done !");
        } catch (NickAlreadyInUseException e) {
            LOGGER.error("Failed to start IRC Bot", e);
        } catch (IOException e) {
            LOGGER.error("Failed to start IRC Bot", e);
        } catch (IrcException e) {
            LOGGER.error("Failed to start IRC Bot", e);
        }
    }

    /**
     * Required by JSVC. Inform the Thread to terminate the run(), close the ServerSockets
     */
    public void stop() {
        LOGGER.info("Stopping IRC Bot...");
        if (ircBot.isConnected()) {
            ircBot.disconnect();
        }
        LOGGER.info("Done !");
    }

    /**
     * Required by JSVC. Destroy any object created in init()
     */
    public void destroy() {
        LOGGER.info("Destroying IRC Bot...");
        ircBot = null;
        LOGGER.info("Done !");
    }

}
