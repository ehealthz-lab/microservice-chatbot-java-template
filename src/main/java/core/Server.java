/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package main.java.core;

import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;

import java.io.FileInputStream;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

/**
 *
 * @author pavel.bucek@sun.com
 * Modified by surya@unizar.es
 */
public class Server {

    private HttpServer webServer;

    public URI BASE_URI;

    private URI getBaseURI(String address) {
        return UriBuilder.fromUri("https://" + address + "/").build();
    }
    
    protected void startServer(String address) {

   	 	Properties misPropiedades = new Properties();
   	 	try {
     		misPropiedades.load(new FileInputStream(System.getProperty("user.dir") + "/files/application.properties"));
     	} catch (Exception e){ System.out.println("Ha ocurrido una excepcion al abrir el fichero, no se encuentra o esta protegido");}
        
        // Grizzly ssl configuration
        SSLContextConfigurator sslContext = new SSLContextConfigurator();
        
        // set up security context
        sslContext.setKeyStoreFile(misPropiedades.getProperty("server.file")); // contains server keypair
        sslContext.setKeyStorePass(misPropiedades.getProperty("server.password"));
        try {

        	BASE_URI = getBaseURI(address);
            
        	//create ResourceConfig from Resource class
        	org.glassfish.jersey.server.ResourceConfig rc = new org.glassfish.jersey.server.ResourceConfig(main.java.resource.Resources.class);
        	
            //create the Grizzly server instance
            webServer = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc, true, new SSLEngineConfigurator(sslContext).setClientMode(false).setNeedClientAuth(false));
            
            // start Grizzly embedded server
            System.out.println("Jersey app started. Try out " + BASE_URI + "\nHit CTRL + C to stop it...");
            //start the server
            webServer.start();

            

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(address);
        
    }

    protected void stopServer() {
        webServer.stop();
    }
}

