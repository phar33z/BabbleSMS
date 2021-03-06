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
package ke.co.tawi.babblesms.server.accountmgmt.pagination.inbox;

import ke.co.tawi.babblesms.server.beans.log.IncomingLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Abstracts an Inbox view on the HTML.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class InboxPage implements Serializable {

    private static final long serialVersionUID = 4746236147087730782L;

    private int pageNum;
    private int totalPage;
    private int pagesize;
    private List<IncomingLog> contents;

    /**
     *
     */
    public InboxPage() {
        super();
        pageNum = 1;
        totalPage = 1;
        pagesize = 1;
        contents = new ArrayList<>();
    }

    /**
     * @param pageNum	the page number
     * @param totalPage	total pages
     * @param pagesize	the number of items in the page
     * @param contents	the USSD session logs
     */
    public InboxPage(final int pageNum, final int totalPage, final int pagesize,
            final List<IncomingLog> contents) {
        this.pageNum = pageNum;
        this.totalPage = totalPage;
        this.pagesize = pagesize;
        this.contents = contents;
    }

    /**
     * @return the pageNum
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * @param pageNum - the pageNum to set
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * @return the totalPage
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * @param totalPage - the totalPage to set
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * @return the pagesize
     */
    public int getPagesize() {
        return pagesize;
    }

    /**
     * @param pagesize - the pagesize to set
     */
    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    /**
     * @return the contents
     */
    public List<IncomingLog> getContents() {
        return new ArrayList<>(contents);
    }

    /**
     * @param contents - the contents to set
     */
    public void setContents(List<IncomingLog> contents) {
        this.contents = contents;
    }

    /**
     *
     * @return		<code>true</code> if this is the first page; <code>false</code>
     * for otherwise
     */
    public boolean isFirstPage() {
        return pageNum == 1;
    }

    /**
     *
     * @return		<code>true</code> if this is the last page; <code>false</code>
     * for otherwise
     */
    public boolean isLastPage() {
        return pageNum == totalPage;
    }

}
