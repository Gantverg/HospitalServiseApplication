package tel_ran.hsa.controller;

import tel_ran.hsa.model.interfaces.IHospital;
import tel_ran.hsa.utils.RestConfig;
import tel_ran.security.accounting.AccountStream;
import tel_ran.view.*;

public abstract class HospitalItem implements Item {
	
	protected static IHospital hospital;
	protected static InputOutput inputOutput;
	protected static String format="dd/MM/yyyy";
	protected static AccountStream accauntStream;
	protected static RestConfig restConfig;
	
	public static AccountStream getAccauntStream() {
		return accauntStream;
	}
	public static void setAccauntStream(AccountStream accauntStream) {
		HospitalItem.accauntStream = accauntStream;
		HospitalItem.accauntStream.setRest(restConfig);
		
	}
	
	public static RestConfig getRestConfig() {
		return restConfig;
	}
	public static void setRestConfig(RestConfig restConfig) {
		HospitalItem.restConfig = restConfig;
	}
	public static String getFormat() {
		return format;
	}
	public static void setFormat(String format) {
		HospitalItem.format = format;
	}
	
	
	
	
	public static void setHospital(IHospital hospital) {
		HospitalItem.hospital = hospital;
	}

	public static void setInputOutput(InputOutput inputOutput) {
		HospitalItem.inputOutput = inputOutput;
	}

	@Override
	public boolean isExit() {
		return false;
	}

	
	
}
