/**
 * Copyright 2015 Tawi Commercial Services Ltd
 * 
 * Licensed under the Open Software License, Version 3.0 (the “License”); you may
 * not use this file except in compliance with the License. You may obtain a copy
 * of the License at:
 * http://opensource.org/licenses/OSL-3.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package ke.co.tawi.babblesms.server.servlet.ajax.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * Tests the servlet that returns a JSON representation of a Contact.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class TestGetContact {

	final String CGI_URL = "http://localhost:8080/BabbleSMS/account/getContact";
	
	final String CONTACT_UUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED";
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.servlet.ajax.client.GetContact#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() {
		try {
        	System.out.println("JSON response for contact uuid '" + CONTACT_UUID + "':\n" + 
            		getResponse(CGI_URL + "?" +	"contactUuid=" + URLEncoder.encode(CONTACT_UUID, "UTF-8")));
        	
        } catch(UnsupportedEncodingException e) {
        	fail("Test to get balance for contact uuid " + CONTACT_UUID);
        	e.printStackTrace();
        }
	}

	
	/**
	 * 
	 * @param urlStr
	 * @return String
	 */
	private String getResponse(String urlStr) {		
        URLConnection conn;
        URL url;
        BufferedReader reader;
		String line;
		StringBuffer stringBuff = new StringBuffer();
		
		try {            
            url = new URL(urlStr);
            conn = url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true); 
            
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
            while( (line = reader.readLine()) != null) {
            	stringBuff.append(line);
            }
            
            reader.close();
            
        } catch(MalformedURLException e) {
            System.err.println("MalformedURLException exception");
            e.printStackTrace();
            
        } catch(IOException e) {
            System.err.println("IOException exception");
            e.printStackTrace();
        }
        
		return stringBuff.toString();
	}
}
