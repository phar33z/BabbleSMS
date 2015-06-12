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

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Returns a JSON list of Groups for a given account.
 * <p>
 * Sample output is as follows:<br/>
 * {  "groups": [
 * {"orange_contacts": "5", "name": "Management", "airtel_contacts": "10", "safaricom_contacts": "22", "uuid": "a118c8ea-f831-4288-986d-35e22c91fc4d"},
 * {"orange_contacts": "15", "name": "Parents", "airtel_contacts": "50", "safaricom_contacts": "30", "uuid": "094def52-bc18-4a9e-9b84-c34cc6476c75" },
 * {"orange_contacts": "23", "name": "Staff", "airtel_contacts": "24", "safaricom_contacts": "44", "uuid": "a2688d72-291b-470c-8926-31a903f5ed0c" },
 * {"orange_contacts": "64", "name": "Students", "airtel_contacts": "15", "safaricom_contacts": "0", "uuid": "9bef62f6-e682-4efd-98e9-ca41fa4ef993"},
 * {"orange_contacts": "19", "name": "Teachers", "airtel_contacts": "16", "safaricom_contacts": "2", "uuid": "e9570c5d-0cc4-41e5-81df-b0674e9dda1e" }
 * ] }
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * @author <a href="mailto:eugene.g99@gmail.com">Eugene Wang'ombe</a>
 * 
 */
public class GetGroups extends HttpServlet {
	

	private Cache accountsCache;

	private GroupDAO groupDAO;

	/**
	 *
	 * @param config
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		CacheManager mgr = CacheManager.getInstance();
		accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID);

		groupDAO = GroupDAO.getInstance();
	}
	

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Account account = new Account();

		String accountUuid = request.getParameter("accountuuid");

		Element element;
		if ((element = accountsCache.get(accountUuid)) != null) {
			account = (Account) element.getObjectValue();
		}

		OutputStream out = response.getOutputStream();

		response.setContentType("application/json;charset=UTF-8");

		// Instantiate the JSon
		// The '=' sign is encoded to \u003d. Hence you need to use
		// disableHtmlEscaping().
		Gson gson = new GsonBuilder().disableHtmlEscaping()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.setPrettyPrinting().serializeNulls().create();
		
		out.write(gson.toJson(getGroupMaps(account)).getBytes());
		out.flush();
		out.close();
	}

	
	/**
	 * @param account
	 * @return a HashMaps containing a list of other HashMaps, each HashMap with
	 *         the relevant Group information.
	 */
	private Map<String, List<HashMap<String, String>>> getGroupMaps(
			Account account) {
		Map<String, List<HashMap<String, String>>> parentGroupHash = new HashMap<String, List<HashMap<String, String>>>();

		List<HashMap<String, String>> groupMaps = new LinkedList<>();
		HashMap<String, String> groupHash;

		if (account == null) {
			return parentGroupHash;
		}

		List<Group> groupList = groupDAO.getGroups(account);

		for (Group group : groupList) {
			groupHash = new HashMap<>();
			groupHash.put("name", group.getName());
			groupHash.put("uuid", group.getUuid());
			groupHash.put("safaricom_contacts", "22");
			groupHash.put("airtel_contacts", "10");
			groupHash.put("orange_contacts", "5");

			groupMaps.add(groupHash);
		}
		parentGroupHash.put("groups", groupMaps);

		return parentGroupHash;
	}
	

	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 *             , IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	
	private static final long serialVersionUID = 2839939167530680340L;
}
