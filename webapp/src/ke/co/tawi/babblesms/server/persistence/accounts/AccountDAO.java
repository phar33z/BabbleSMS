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

package ke.co.tawi.babblesms.server.persistence.accounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;
import ke.co.tawi.babblesms.server.servlet.util.SecurityUtil;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;




/**
 *  Persistence implementation for {@link Account}s.
 * <p> 
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class AccountDAO extends GenericDAO implements BabbleAccountDAO {
	
	private static AccountDAO accountDAO;
	
	private BeanProcessor beanProcessor = new BeanProcessor();
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	
    /**
     * @return the singleton instance of  {@link AccountDAO}
     */
	public static AccountDAO getInstance(){
		if(accountDAO == null){
			accountDAO = new AccountDAO();
		}
		
		return accountDAO;		
	}
	
	
	/**
	 * 
	 */	
	protected AccountDAO(){
		super();
	}
	
	/**
	 * Used for testing purposes only.
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param dbPort
	 */
	
	public AccountDAO(String dbName,String dbHost, String dbUsername,
			String dbPassword, int dbPort){
		super(dbName,dbHost,dbUsername,dbPassword,dbPort);
	}

	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.accounts.BabbleAccountDAO#getAccount(java.lang.String)
	 */
	@Override
	public Account getAccount(String uuid) {
		Account account = null;
        ResultSet rset = null;
        
		
        try (  
        	 Connection conn = dbCredentials.getConnection();
        	 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Account WHERE Uuid = ?;");        		
    		){
           
	            pstmt.setString(1, uuid);
	            rset = pstmt.executeQuery();
	
	            if (rset.next()) {
	                account = beanProcessor.toBean(rset, Account.class);
	            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting an account with uuid: " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        
        return account;		
	}
	
    
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.accounts.BabbleAccountDAO#getAccountByName(java.lang.String)
	 */
	@Override
	public Account getAccountByName(String username) {
		 Account account = null;
		 ResultSet rset = null;

	    try(
	    		Connection conn = dbCredentials.getConnection();
	    		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Account WHERE username = ?;");
    		) {
	        
		        pstmt.setString(1, username);
		        rset = pstmt.executeQuery();
		
		        if (rset.next()) {
		            account = beanProcessor.toBean(rset, Account.class);
		        }
	
	    } catch (SQLException e) {
	        logger.error("SQL Exception when getting an account with username: " + username);
	        logger.error(ExceptionUtils.getStackTrace(e));
	    } 
	    
	    return account;
	}
	
	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.accounts.BabbleAccountDAO#getAllAccounts()
	 */

	@Override
	public List<Account> getAllAccounts() {
		List<Account> list = null;
        
        try(   
        		Connection conn = dbCredentials.getConnection();
        		PreparedStatement  pstmt = conn.prepareStatement("SELECT * FROM Account;");   
        		ResultSet rset = pstmt.executeQuery();
    		) {
        	
            list = beanProcessor.toBeanList(rset, Account.class);

        } catch(SQLException e){
        	logger.error("SQL Exception when getting all accounts");
            logger.error(ExceptionUtils.getStackTrace(e));
        }
       
		return list;
	}
	

	/**
	 * @see ke.co.tawi.babblesms.server.persistence.accounts.BabbleAccountDAO#putAccount(ke.co.tawi.babblesms.server.beans.account.Account)
	 */
	@Override
	public boolean putAccount(Account account) {
		boolean success = true;

        try (
        		Connection conn = dbCredentials.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Account" 
        		+"(Uuid, username, logpassword, name, mobile, email,statusuuid) VALUES (?,?,?,?,?,?,?);");
            ) {
        	
            pstmt.setString(1, account.getUuid());
            pstmt.setString(2, account.getUsername());
            pstmt.setString(3, SecurityUtil.getMD5Hash(account.getLogpassword()));
            pstmt.setString(4, account.getName());
            pstmt.setString(5, account.getMobile());
            pstmt.setString(6, account.getEmail()); 
            pstmt.setString(7, account.getStatusuuid());
           

            pstmt.executeUpdate();
            
        } catch (SQLException e) {
        	logger.error("SQLException when trying to put: " + account);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        }      
        
        return success;
	}

	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.accounts.BabbleAccountDAO#updateAccount(java.lang.String, ke.co.tawi.babblesms.server.beans.account.Account)
	 */
	@Override
	public boolean updateAccount(String uuid, Account account) {
		 boolean success = true;

        try (  Connection conn = dbCredentials.getConnection();
        	PreparedStatement pstmt = conn.prepareStatement("UPDATE Account SET username=?, "
        			+ "logpassword=?, name=?, mobile=?, email=? WHERE Uuid = ?;");
        	) {
            
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getLogpassword());
            pstmt.setString(3, account.getName());
            pstmt.setString(4, account.getMobile());
            pstmt.setString(5, account.getEmail());
            pstmt.setString(6, account.getUuid());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when updating accounts with uuid " + account);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        
        return success;
	}
	
	
	public boolean updateStatus(String uuid, String statusuuid) {
		 boolean success = true;

       try (  Connection conn = dbCredentials.getConnection();
       	PreparedStatement p = conn.prepareStatement("UPDATE Account SET statusuuid=? WHERE Uuid = ?");
       	) {
           
           p.setString(1, statusuuid);
           p.setString(2, uuid);
          

           p.executeUpdate();

       } catch (SQLException e) {
           logger.error("SQL Exception when updating accounts with uuid " + uuid);
           logger.error(ExceptionUtils.getStackTrace(e));
           success = false;
       } 
       
       return success;
	}
	
	
	
	
	
}
