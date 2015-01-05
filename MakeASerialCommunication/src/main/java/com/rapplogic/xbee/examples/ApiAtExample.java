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

package com.rapplogic.xbee.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.serial.Serial;
import com.rapplogic.xbee.api.AtCommand;
import com.rapplogic.xbee.api.AtCommandResponse;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;

/** 
 * The AtCommand/AtCommandResponse classes are supported by both ZNet and WPAN XBees but certain
 * commands are specific to ZNet or WPAN.  
 * 
 * Commands that are ZNet specific are located in the ZNetApiAtTest.
 * 
 * Refer to the manual for more information on available commands
 * 
 * @author andrew
 *
 */
public class ApiAtExample {

//	TODO split class in to WPAN class
	 
	private final static Logger log = LoggerFactory.getLogger(ApiAtExample.class);
	
	private XBee xbee = new XBee();
	
	public ApiAtExample() throws XBeeException {
			
		try {	
			
			System.out.println("****\n* ApiAtExample: provo ad aprire la porta seriale\n****");
			
			// replace with port and baud rate of your XBee
			xbee.open(Serial.DEFAULT_COM_PORT, 9600);
			
//			// set D1 analog input
//			this.sendCommand(new AtCommand("D1", 2));
//			// set D2 digital inpu
//			this.sendCommand(new AtCommand("D2", 3));
//			// send sample every 5 seconds
//			this.sendCommand(new AtCommand("IR", new int[] {0x13, 0x88}));
			
			System.out.println("****\n* ApiAtExample: provo ad invocare il comando MY\n****");
			
			AtCommandResponse ap = (AtCommandResponse)xbee.sendSynchronous(new AtCommand("MY"));

			System.out.println("****\n* ApiAtExample: risultato del comando MY: " + ap.toString() + "\n****");
			
//			log.info("MY is " + xbee.sendAtCommand(new AtCommand("MY")));
//			log.info("SH is " + xbee.sendAtCommand(new AtCommand("SH")));
			
			System.out.println("****\n* ApiAtExample: provo ad invocare il comando SH\n****");
			
			ap = (AtCommandResponse)xbee.sendSynchronous(new AtCommand("SH"));

			System.out.println("****\n* ApiAtExample: risultato del comando SH: " + ap.toString() + "\n****");
			
//			log.info("SL is " + xbee.sendAtCommand(new AtCommand("SL")));
			
			System.out.println("****\n* ApiAtExample: provo ad invocare il comando SL\n****");
			
			ap = (AtCommandResponse)xbee.sendSynchronous(new AtCommand("SL"));

			System.out.println("****\n* ApiAtExample: risultato del comando SL: " + ap.toString() + "\n****");
			
		} catch (Exception e) {
			log.error("at command failed", e);
		} finally {
			
			xbee.close();
		}
	}
	
	public static void main(String[] args) throws XBeeException {
		
		new ApiAtExample();
	}
}
