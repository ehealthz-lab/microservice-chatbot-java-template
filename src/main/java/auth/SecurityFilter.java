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

package main.java.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.Properties;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;


public class SecurityFilter {
    
    @Context
    UriInfo uriInfo;

    public User authenticate(String authentication) {
        // Extract authentication credentials
        //System.out.println("Auth: " + authentication);
        
        Properties misPropiedades = new Properties();
        try {
        	misPropiedades.load(new FileInputStream(System.getProperty("user.dir") + "/files/application.properties"));
        } catch (Exception e){ System.out.println("Ha ocurrido una excepcion al abrir el fichero, no se encuentra o esta protegido");}
        
        if (authentication == null) {
            return null;
        }
        if (!authentication.startsWith("Bearer")) {
        	return null;
        }
        if (authentication.equals("Bearer") || authentication.equals("Bearer ")) {
            return null;
        }
        String microservice = "";
        int exitValue = -1;
        try {
            // It executes the Python script
            String commands[] = {"python", "interceptors.py", authentication};
            ProcessBuilder pb = new ProcessBuilder(commands);
            // Debug, it shows the Python log, but if it is active then 'line' will be empty
            //pb.inheritIO();
            pb.directory(new File(misPropiedades.getProperty("securityFilter.pathFile")));
            Process process = pb.start();

            // It reads the output of the Python script
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = stdout.readLine()) != null) {
                //System.out.println("Resultado: " + line);
                microservice = line;
            }

            exitValue = process.waitFor(); // 0 means that the program has run correctly and 1 means that there have been errors
            System.out.println("Codigo de salida: "+ exitValue);
        }
        catch (InterruptedException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (microservice.equals("")) {
            return null;
        }

        // Validate the extracted credentials
        User user = null;

        if ((exitValue ==  0) && !microservice.equals("Error in token decoding")){ // The developers can connect to the API gateway to add or remove functionalities
            user = new User(microservice, "user");
            System.out.println("USER AUTHENTICATED");
        } else {
            System.out.println("USER NOT AUTHENTICATED");
            return null;
        }
        return user;
    }

    public class Authorizer implements SecurityContext {

        private User user;
        private Principal principal;

        public Authorizer(final User user) {
            this.user = user;
            this.principal = new Principal() {

                public String getName() {
                    return user.username;
                }
            };
        }

        public Principal getUserPrincipal() {
            return this.principal;
        }

        public boolean isUserInRole(String role) {
            return (role.equals(user.role));
        }

        public boolean isSecure() {
            return "https".equals(uriInfo.getRequestUri().getScheme());
        }

        public String getAuthenticationScheme() {
            return SecurityContext.BASIC_AUTH;
        }
    }

    public class User {

        public String username;
        public String role;

        public User(String username, String role) {
            this.username = username;
            this.role = role;
        }
        
        public String getUsername() {
        	return username;
        }
    }
}
