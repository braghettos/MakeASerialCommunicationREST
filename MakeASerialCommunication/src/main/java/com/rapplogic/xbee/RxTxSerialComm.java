/**
 * Copyright (c) 2008 Andrew Rapp. All rights reserved.
 *  
 * This file is part of XBee-API.
 *  
 * XBee-API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * XBee-API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with XBee-API.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.rapplogic.xbee;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.PacketParser;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeFrameIdResponse;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.util.ByteUtils;

/** 
 * This class encapsulates a RXTX serial port, providing access to input/output streams,
 * and notifying the subclass of new data events via the handleSerialData method.
 * 
 * @author andrew
 * 
 */
public class RxTxSerialComm implements XBeeConnection, SerialDataListener {

	private final static Logger log = LoggerFactory.getLogger(RxTxSerialComm.class);
		
	private byte[] receivedBytes = new byte[0];
	
	private InputStream inputStream;
	private OutputStream outputStream;
	
	// create an instance of the serial communications class
    private Serial serialPort = SerialFactory.createInstance();
	    
    public RxTxSerialComm() {
	
		// create and register the serial data listener
		serialPort.addListener(this);
		
		inputStream = new ByteArrayInputStream(receivedBytes);
		
    }
	
	public void openSerialPort(String port, int baudRate) {
		
		log.debug("Port to open: " + port + ", baudRate=" + baudRate);
		
		try {
			// open the default serial port provided on the GPIO header
	        serialPort.open(port, baudRate);
		}
        catch(SerialPortException ex) {
            log.error("Serial setup failed", ex);
            return;
        }
		catch(Exception ex) {
            log.error("Error",ex);
            return;
        }
	            
	}
	

	/**
	 * Shuts down RXTX
	 */
	public void close() {
		
		try {
			log.debug("Removing listener");
			serialPort.removeListener(this);
			
			log.debug("Closing serialPort" + serialPort.DEFAULT_COM_PORT);
			serialPort.close();
			
		} catch (SerialPortException e) {
			log.error("Exception while closing serial port");
		}
	}
	
	public boolean write(String bs) {
		
		StringTokenizer st = new StringTokenizer(bs, ",");
		 
		while (st.hasMoreElements()) {
			
			String element = ((String) st.nextElement()).substring(2, 4);
			
			try{
				log.debug("byte to write" + element);
				int decimal = Integer.parseInt(element, 16);
				serialPort.write((byte)decimal);
			}
			catch(IllegalStateException ex){
				log.error("Error",ex);
				return false;
			}
		}
		
		return true;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	
	public boolean isConnected() {
		
		return serialPort.isOpen();
	}

	@Override
	public void dataReceived(SerialDataEvent event) {
		
		String eventString = event.getData();
        receivedBytes = new byte[eventString.length()];
        	
        int dataAvailable = eventString.length();
        
        log.info("dataAvailable:" + dataAvailable);
        
        for(int i=0; i< dataAvailable; i++){
        		
        	log.info("Byte[" + i + "] =" + String.format("0x%02X", (byte)eventString.charAt(i)));
        	receivedBytes[i]=(byte)eventString.charAt(i);
        }
        
        if(dataAvailable>0){
        	
        	synchronized (this) {
	        	log.info("risveglio inputStream");
	        	
	        	//inputStream = new ByteArrayInputStream(eventString.getBytes());
	        	inputStream = new ByteArrayInputStream(receivedBytes);
	        	this.notify();		
	        }
	        
        	
        }      
	}

}