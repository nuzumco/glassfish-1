/*
 * Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.jdbcra.common;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Enumeration;
import com.sun.jdbcra.util.MethodExecutor;
import javax.resource.ResourceException;

import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * Utility class, which would create necessary Datasource object according to the 
 * specification.
 *
 * @version	1.0, 02/07/23
 * @author	Binod P.G
 * @see		com.sun.jdbcra.common.DataSourceSpec
 * @see		com.sun.jdbcra.util.MethodExcecutor
 */
public class DataSourceObjectBuilder implements java.io.Serializable{

    private DataSourceSpec spec;
    
    private Hashtable driverProperties = null;
    
    private MethodExecutor executor = null;    
    
    private static Logger _logger;
    static {
        _logger = Logger.getAnonymousLogger();
    }
    private boolean debug = false;
    /**
     * Construct a DataSource Object from the spec.
     * 
     * @param	spec	<code> DataSourceSpec </code> object.
     */
    public DataSourceObjectBuilder(DataSourceSpec spec) {
    	this.spec = spec;
    	executor = new MethodExecutor();
    }
    
    /**
     * Construct the DataSource Object from the spec.
     *
     * @return	Object constructed using the DataSourceSpec.
     * @throws	<code>ResourceException</code> if the class is not found or some issue in executing
     *		some method.
     */
    public Object constructDataSourceObject() throws ResourceException{
    	driverProperties = parseDriverProperties(spec);
        Object dataSourceObject = getDataSourceObject();
        Method[] methods = dataSourceObject.getClass().getMethods();
        for (int i=0; i < methods.length; i++) {
            String methodName = methods[i].getName();
	    if (methodName.equalsIgnoreCase("setUser")){	    	
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.USERNAME),methods[i],dataSourceObject);
	    	
	    } else if (methodName.equalsIgnoreCase("setPassword")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.PASSWORD),methods[i],dataSourceObject);
	    	
	    } else if (methodName.equalsIgnoreCase("setLoginTimeOut")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.LOGINTIMEOUT),methods[i],dataSourceObject);
	    	
	    } else if (methodName.equalsIgnoreCase("setLogWriter")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.LOGWRITER),methods[i],dataSourceObject);
	    	
	    } else if (methodName.equalsIgnoreCase("setDatabaseName")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.DATABASENAME),methods[i],dataSourceObject);
	    	
	    } else if (methodName.equalsIgnoreCase("setDataSourceName")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.DATASOURCENAME),methods[i],dataSourceObject);	    	
	    	
	    } else if (methodName.equalsIgnoreCase("setDescription")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.DESCRIPTION),methods[i],dataSourceObject);	
	    	
	    } else if (methodName.equalsIgnoreCase("setNetworkProtocol")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.NETWORKPROTOCOL),methods[i],dataSourceObject);
	    	
	    } else if (methodName.equalsIgnoreCase("setPortNumber")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.PORTNUMBER),methods[i],dataSourceObject);
	    	
	    } else if (methodName.equalsIgnoreCase("setRoleName")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.ROLENAME),methods[i],dataSourceObject);	    		    	
	    	
	    } else if (methodName.equalsIgnoreCase("setServerName")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.SERVERNAME),methods[i],dataSourceObject);	    
	    	
	    } else if (methodName.equalsIgnoreCase("setMaxStatements")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.MAXSTATEMENTS),methods[i],dataSourceObject);
	    	
	    } else if (methodName.equalsIgnoreCase("setInitialPoolSize")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.INITIALPOOLSIZE),methods[i],dataSourceObject);	    	
	    	
	    } else if (methodName.equalsIgnoreCase("setMinPoolSize")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.MINPOOLSIZE),methods[i],dataSourceObject);
	    	
	    } else if (methodName.equalsIgnoreCase("setMaxPoolSize")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.MAXPOOLSIZE),methods[i],dataSourceObject);	    	
	    	
	    } else if (methodName.equalsIgnoreCase("setMaxIdleTime")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.MAXIDLETIME),methods[i],dataSourceObject);	    	
	    	
	    } else if (methodName.equalsIgnoreCase("setPropertyCycle")){
	    	executor.runJavaBeanMethod(spec.getDetail(DataSourceSpec.PROPERTYCYCLE),methods[i],dataSourceObject);	    	
	    	
	    } else if (driverProperties.containsKey(methodName.toUpperCase())){
	    	Vector values = (Vector) driverProperties.get(methodName.toUpperCase());
	        executor.runMethod(methods[i],dataSourceObject, values);
	    }
        }
        return dataSourceObject;
    }
    
    /**
     * Get the extra driver properties from the DataSourceSpec object and
     * parse them to a set of methodName and parameters. Prepare a hashtable
     * containing these details and return.
     *
     * @param	spec	<code> DataSourceSpec </code> object.
     * @return	Hashtable containing method names and parameters,
     * @throws	ResourceException	If delimiter is not provided and property string
     *					is not null.
     */
    private Hashtable parseDriverProperties(DataSourceSpec spec) throws ResourceException{
    	String delim = spec.getDetail(DataSourceSpec.DELIMITER);

        String prop = spec.getDetail(DataSourceSpec.DRIVERPROPERTIES);
	if ( prop == null || prop.trim().equals("")) {
	    return new Hashtable();
	} else if (delim == null || delim.equals("")) {
	    throw new ResourceException ("Delimiter is not provided in the configuration");
	}
	
	Hashtable properties = new Hashtable();
	delim = delim.trim();	
	String sep = delim+delim;
	int sepLen = sep.length();
	String cache = prop;
	Vector methods = new Vector();
	
	while (cache.indexOf(sep) != -1) {
	    int index = cache.indexOf(sep);
	    String name = cache.substring(0,index);
	    if (name.trim() != "") {
	        methods.add(name);
	    	cache = cache.substring(index+sepLen);
	    }
	}
	
    	Enumeration allMethods = methods.elements();
    	while (allMethods.hasMoreElements()) {
    	    String oneMethod = (String) allMethods.nextElement();    	    
    	    if (!oneMethod.trim().equals("")) {
    	    	String methodName = null;
    	    	Vector parms = new Vector();
    	    	StringTokenizer methodDetails = new StringTokenizer(oneMethod,delim);
		for (int i=0; methodDetails.hasMoreTokens();i++ ) {
		    String token = (String) methodDetails.nextToken();
		    if (i==0) {
		    	methodName = token.toUpperCase();
		    } else {
		    	parms.add(token);		    	
		    }
		}
		properties.put(methodName,parms);
    	    }    	    
    	}
    	return properties;
    }
    
    /**
     * Creates a Datasource object according to the spec.
     *
     * @return	Initial DataSource Object instance.
     * @throws	<code>ResourceException</code> If class name is wrong or classpath is not set
     *		properly.
     */
    private Object getDataSourceObject() throws ResourceException{
    	String className = spec.getDetail(DataSourceSpec.CLASSNAME);
        try {            
            Class dataSourceClass = Class.forName(className);
            Object dataSourceObject = dataSourceClass.newInstance();
            return dataSourceObject;
        } catch(ClassNotFoundException cfne){
	    _logger.log(Level.SEVERE, "jdbc.exc_cnfe_ds", cfne);
	
            throw new ResourceException("Class Name is wrong or Class path is not set for :" + className);
        } catch(InstantiationException ce) {
	    _logger.log(Level.SEVERE, "jdbc.exc_inst", className);
            throw new ResourceException("Error in instantiating" + className);
        } catch(IllegalAccessException ce) {
	    _logger.log(Level.SEVERE, "jdbc.exc_acc_inst", className);
            throw new ResourceException("Access Error in instantiating" + className);
        }
    }
  
}
