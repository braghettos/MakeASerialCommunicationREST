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
import com.rapplogic.xbee.api.RemoteAtRequest;
import com.rapplogic.xbee.api.RemoteAtResponse;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeTimeoutException;

/** 
 * This example uses Remote AT to turn on/off I/O pins.  
 * This example is more interesting if you connect a LED to pin 20 on your end device.  
 * Remember to use a resistor to limit the current flow.  I used a 215 Ohm resistor.
 * <p/>
 * Note: if your coordinator is powered on and receiving I/O samples, make sure you power off/on to drain 
 * the traffic before running this example.
 * 
 * @author andrew
 *
 */
public class RemoteAtExample {

	private final static Logger log = LoggerFactory.getLogger(RemoteAtExample.class);
	
	private RemoteAtExample() throws XBeeException, InterruptedException {
		
		XBee xbee = new XBee();
		
		try {
			
			System.out.println("****\n* RemoteAtExample: provo ad aprire la porta seriale\n****");
			
			// replace with port and baud rate of your XBee
			xbee.open(Serial.DEFAULT_COM_PORT, 9600);	
			
			System.out.println("****\n* RemoteAtExample: creo addr64 di destinazione\n****");
						
			// replace with SH + SL of your end device (i.e. 00 13 a2 00 40 8b b1 4c)
			XBeeAddress64 addr64 = new XBeeAddress64(0, 0x13, 0xa2, 0, 0x40, 0x8b, 0xb1, 0x4c);
			
			// turn on end device (pin 20) D0 (Digital output high = 5) 
			//RemoteAtRequest request = new RemoteAtRequest(addr64, "D0", new int[] {5});
			//RemoteAtRequest request = new RemoteAtRequest(addr64, "IR", new int[] {0x7f, 0xff});
			//RemoteAtRequest request = new RemoteAtRequest(addr64, "D5", new int[] {3});
			//RemoteAtRequest request = new RemoteAtRequest(addr64, "D0", new int[] {2});
			//RemoteAtRequest request = new RemoteAtRequest(addr64, "P2", new int[] {3});
			
			System.out.println("****\n* RemoteAtExample: provo ad invocare il comando SL\n****");
			
			RemoteAtRequest request = new RemoteAtRequest(addr64, "SL");
			
			RemoteAtResponse response = (RemoteAtResponse) xbee.sendSynchronous(request, 10000);
			
			if (response.isOk()) {
				log.info("successfully found SL=" + response.getValue());	
				System.out.println("****\n* RemoteAtExample: valore di SL="+ response.getValue() +"\n****");
				
			} else {
				throw new RuntimeException("failed to get SL.  status is " + response.getStatus());
			}
				
		} catch (XBeeTimeoutException e) {
			log.error("request timed out. make sure you remote XBee is configured and powered on");
			System.out.println("****\n* RemoteAtExample: request timed out. make sure you remote XBee is configured and powered on\n****");
		} catch (Exception e) {
			log.error("unexpected error", e);
			System.out.println("****\n* RemoteAtExample: unexpected error "+ e.getMessage() +"\n****");
		} finally {
			xbee.close();
		}
	}
	
	public static void main(String[] args) throws XBeeException, InterruptedException {
		
		new RemoteAtExample();
	}
}
