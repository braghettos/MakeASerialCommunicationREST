package com.rapplogic.xbee;

import com.pi4j.io.serial.SerialDataEvent;

public interface RxTxSerialEventListener {
	public void handleSerialEvent(SerialDataEvent event);
}
