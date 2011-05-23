/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/kernel-impl/src/main/java/org/sakaiproject/db/impl/SqlServiceSql.java $
 * $Id: SqlServiceSql.java 51317 2008-08-24 04:38:02Z csev@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007, 2008 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.db.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

/**
 * database methods.
 */
public interface SqlServiceSql
{
    /**
     * returns whether the error code is a deadlock error or not
     * @param errorCode
     * @return
     */
    public boolean isDeadLockError(int errorCode);
	/**
	 * returns whether the the database supports reading and updating blobs.
	 */
	public boolean canReadAndUpdateBlob();

	/**
	 * returns a databases representation of the specified java boolean value.
	 */
	public String getBooleanConstant(boolean value);

	/**
	 * returns whether the sql exception indicates that a record already exists in a table.
	 */
	public boolean getRecordAlreadyExists(SQLException ex);

	/**
	 * returns the sql statement which returns the next number in a sequence. <br/><br/>
	 * 
	 * @param table
	 *        name of table to read the sequence number from.
	 */
	public String getNextSequenceSql(String table);

	/**
	 * sets the value of a bytes field in the specified column. <br/><br/>
	 * 
	 * @param pstmt
	 *        prepared statement
	 * @param var
	 *        value to bind to the last parameter in the sql statement.
	 * @param pos
	 *        number of column of bytes field.
	 */
	public PreparedStatement setBytes(PreparedStatement pstmt, String var, int pos) throws SQLException;

	/**
	 * sets the value of a timestamp field in the specified column. <br/><br/>
	 * 
	 * @param pstmt
	 *        prepared statement
	 * @param timestamp
	 *        timestamp
	 * @param calendar
	 *        calendar
	 * @param pos
	 *        number of column of bytes field.
	 */
	public PreparedStatement setTimestamp(PreparedStatement pstmt, Timestamp timestamp, GregorianCalendar calendar, int pos) throws SQLException;

   /**
    *  set a null in the given statement at the given postion
    * @param pstmt
    * @param pos
    */
   public PreparedStatement setNull(PreparedStatement pstmt, int pos) throws SQLException;

   /**
    * sets the value of a bytes field in the specified column. <br/><br/>
    *
    * @param pstmt
    *        prepared statement
    * @param var
    *        value to bind to the last parameter in the sql statement.
    * @param pos
    *        number of column of bytes field.
    */
   public PreparedStatement setBytes(PreparedStatement pstmt, byte[] bytes, int pos) throws SQLException;

}
