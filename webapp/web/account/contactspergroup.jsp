<%
    /**
    Copyright 2015 Tawi Commercial Services Ltd

    Licensed under the Open Software License, Version 3.0 (the ?License?); you may 
    not use this file except in compliance with the License. You may obtain a copy 
    of the License at:
    http://opensource.org/licenses/OSL-3.0

    Unless required by applicable law or agreed to in writing, software distributed 
    under the License is distributed on an ?AS IS? BASIS, WITHOUT WARRANTIES OR
    CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and limitations 
    under the License.
    */
%>

<%@page import="ke.co.tawi.babblesms.server.beans.status.Status"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Group"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>

<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>

<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.servlet.accountmngmt.VeiwGrpContacts"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import=" java.util.Map"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>


<jsp:include page="contactheader.jsp" />
<%
    // The following is for session management.    
    if (session == null) {
        response.sendRedirect("../index.jsp");
    }

    String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
    if (StringUtils.isEmpty(username)) {
        response.sendRedirect("../index.jsp");
    }

    session.setMaxInactiveInterval(SessionConstants.SESSION_TIMEOUT);
    response.setHeader("Refresh", SessionConstants.SESSION_TIMEOUT + "; url=../logout");

    List<Group> contactsgrpList = new ArrayList<>();   
         

     AccountDAO accountDAO = AccountDAO.getInstance();
     Account account = accountDAO.getAccountByName(username);

     GroupDAO gDAO = new GroupDAO();
    contactsgrpList = gDAO.getGroups(account);

    VeiwGrpContacts gcDAO = VeiwGrpContacts.getInstance();
    HashMap<String, String> grpList = gcDAO.getcontacts("a118c8ea-f831-4288-986d-35e22c91fc4d");

    String accountuuid = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_ACCOUNTUUID);
    CacheManager mgr = CacheManager.getInstance();
    Cache statusCache = mgr.getCache(CacheVariables.CACHE_STATUS_BY_UUID);
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);

    HashMap<String, String> networkHash = new HashMap<String, String>();

    List<Status> list = new ArrayList<>();
    List<Network> networkList = new ArrayList<Network>();

    Element element;
    List keys;
    Status st;
    keys = statusCache.getKeys();
    for (Object key : keys) {
        element = statusCache.get(key);
        st = (Status) element.getObjectValue();
        list.add(st);

    }
    List keys1;
    Network network; 
    keys1 = networksCache.getKeys();
    for (Object key : keys1) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), network.getName());
        networkList.add(network);
    }

%>

<div>
    <ul class="breadcrumb">
        <li>
            <a href="index.jsp">Home</a> <span class="divider">/</span>
        </li>
        <li>
            View Contacts per group 
        </li>
    </ul>
</div>




<div class="row-fluid sortable">
    <div class="box span12">   
     <div class="box-header well" data-original-title=" view contacts">
        <h2><i class="icon-picture"></i> View Contacts per Group</h2>
        
        </div>
        <div class="box-content">
       
<div class="controls"> <h4>Choose a group:&nbsp;             
            <select class="groupselect" onclick="Chromecheck()">
                 <% if (contactsgrpList != null) {                        
                    out.println("<td>");
                    for(Group gr : contactsgrpList) { %>
                  <option class="grp" value="<%=gr.getUuid()%>" name="<%=gr.getName()%>" ><%=gr.getName()%></option>          
                          <%}                            
                 } else {%> 
                 <option >No groups available</option>
                 <% } %>                          
                           
            </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      Choose a network:&nbsp;
         <select class="networkselect" onclick="Chromecheck()">
                <option class="nwk"  value="allnetworks" name="All Networks">All Networks</option>
                   <%                   
                      int count2 = 1;
                   if (networkList != null) {
                  for (Network netwk : networkList) {
                     %>
             <option  class="nwk" value="<%= netwk.getUuid()%>" name="<%= netwk.getName()%>"><%= netwk.getName()%></option>
                     <%
                  count2++;
                       }
                     }
               %>
          </select></h4>
                        <div id="header-display"></div>
                        <table class="table table-striped table-bordered" id="contactgrp">                        
                        <thead > 
                           <tr>
                             <td width="10%">*</td>
                             <td width="50%"><a>Contact Name/Network</a></td>
                             <td width="40%"><a>Total contact count/Phone No.</a></td>
                          </tr>
                        </thead>
                        <tbody >
                           <%  int i=0;                           
                             if (grpList != null) {
                             
                            for(Map.Entry fone: grpList.entrySet()){
                              i++;
                              %>
                          <tr>
                             <td width="10%"><%=i%></td>
                             <td width="50%"><%=fone.getKey()%></td>
                             <td width="40%"><%=fone.getValue()%></td>
                          </tr>               
                            <%}
                            }                       

                        else {%> 
                        
                       <% } %>  
                        </tbody>
                        </table>                       
      </div>

        </div>
    </div><!--/span-->

</div><!--/row-->



<jsp:include page="footer.jsp" />
