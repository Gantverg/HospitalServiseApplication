package tel_ran.hospital.entities;

import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HealthGroupCollection {
static Map<String,HealthGroup> healthGroups = new HashMap<>();
public HealthGroupCollection()
{
	addHealthGroup(1,"group1", 60, 90, 60);
	addHealthGroup(2,"group2", 70, 100, 360);
	addHealthGroup(3,"group3", 80, 110, 3600);

}
public static void addHealthGroup(int idGroup,String name,int minNormalPulse,int maxNormalPulse,int serveyPeriod) {
	healthGroups.put(name, new HealthGroup(idGroup,name, minNormalPulse, maxNormalPulse, serveyPeriod));
}

public static boolean removeHealthGroup(String name)
{
	if (!healthGroups.containsKey(name)) return false;
	healthGroups.remove(name);
	return true;
}
public static Map<String,HealthGroup> getHealthgroups(){
  return healthGroups;
}
public static void showListHealthGroupsNames()
{
	for (Map.Entry<String, HealthGroup> pair : healthGroups.entrySet()) {
		System.out.println(pair.getValue()+". "+pair.getKey());
		
	}
}
}
