/**
 * Copyright 2016 Daniel "Dadie" Korner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	 http://www.apache.org/licenses/LICENSE-2.0
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 * Unless required by applicable law or agreed to in writing, software
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package com.android.server.valuesupplier.odroidxu3;

import java.lang.RuntimeException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.android.server.ValueSupplierService;

//Usage of /proc/stat
//For more Information: http://www.linuxhowtos.org/System/procstat.htm

public class CPUnFrequencySupplierService extends ValueSupplierService  {
	
	private int        cpuNumber;
	private ByteBuffer valueByteBuffer;
	
	public CPUnFrequencySupplierService(int cpuNumber) {
		super("ODROID_XU3_CPU_"+cpuNumber+"_FREQUENCY");
		this.cpuNumber  = cpuNumber;
		this.valueByteBuffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
	}

	private Double readValue() {
		BufferedReader bufReader;
		try {
			bufReader = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu"+this.cpuNumber+"/cpufreq/scaling_cur_freq"));
			String frequency = bufReader.readLine();
			bufReader.close();
			return Double.valueOf(frequency);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public byte[] value() {
		this.valueByteBuffer.clear();
		return this.valueByteBuffer.putDouble((this.readValue()).doubleValue()).array();
	}
	
	@Override
	public int size() {
		return 8;
	}
	
	@Override
	public String type() {
		return "f64";
	}
	
	@Override
	public String unit() {
		return "Hz";
	}

}

