package io;

import java.util.HashMap;
import java.util.Map;

import cpu.element.Word;
import exception.IllegalDeviceStatusException;


/**
 * all the io devices are registered here
 *
 */
public class IODeviceController {
	
	/**
	 * store all the IO devices indexed by devID
	 */
	private Map<Integer,IODevice> devices;
	
	/**
	 * an instance
	 */
	private static IODeviceController _instance;
	public static IODeviceController getInstance(){
		if(_instance==null)
			_instance = new IODeviceController();
		return _instance;
	}
	
	/**
	 * constructor
	 */
	public IODeviceController(){
		devices = new HashMap<Integer,IODevice>();
		
		//keyboard
		KeyboardDevice keyboard = new KeyboardDevice();
		devices.put(keyboard.devid, keyboard);
		
		//printer
		PrinterDevice printer = new PrinterDevice();
		devices.put(printer.devid, printer);
		
	}
	
	/**
	 * for CPU to write data into Device
	 * @param devid
	 * @param value
	 * @throws IllegalDeviceStatusException: Device not found, Device not ready, Device error, etc
	 */
	public void output(int devid,Word value) throws IllegalDeviceStatusException{
		//find the device
		IODevice dev = devices.get(devid);
		if(dev==null){
			throw new IllegalDeviceStatusException("Device("+devid+") not found");
		}
		//use device to write data, Exception may be thrown
		dev.write(value);
	}
	/**
	 * for CPU to read data from Device
	 * @param devid
	 * @return
	 * @throws IllegalDeviceStatusException: Device not found, Device not ready, Device error, etc
	 */
	public Word input(int devid) throws IllegalDeviceStatusException{
		//find the device
		IODevice dev = devices.get(devid);
		if(dev==null){
			throw new IllegalDeviceStatusException("Device("+devid+") not found");
		}
		//fetching data from device, the fetching may cause CPU waiting. Exception may be thrown
		return dev.read();
	}
	
	/**
	 * get Keyboard device
	 * @return
	 */
	public IODevice getKeyboard(){
		return devices.get(IODevice.DEVID_KEYBOARD);
	}
	
	/**
	 * get Printer device
	 * @return
	 */
	public IODevice getPrinter(){
		return devices.get(IODevice.DEVID_PRINTER);
	}
	
	/*
	public CodeEditorDevice getCodeEditorDevice(){
		return (CodeEditorDevice)devices.get(IODevice.DEVID_EDITOR);
	}
	*/
	
	public void resetAll(){
		try {
			for(IODevice dev:devices.values()){
				dev.reset();
			}
		} catch (IllegalDeviceStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
