package tel_ran.hospital.entities;

import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tel_ran.hsa.model.dto.HealthGroup;

//import tel_ran.hospital.entities.not_use.HealthGroup;

public class HealthGroupCollection {
static Map<Integer,HealthGroup> healthGroups = new HashMap<>();
public HealthGroupCollection()
{
	addHealthGroup(1, "group1", 60, 90, 60);
	addHealthGroup(2, "group2", 70, 100, 360);
	addHealthGroup(3, "group3", 80, 110, 3600);

}
public static void addHealthGroup(int groupId, String name,int minNormalPulse,int maxNormalPulse,int serveyPeriod) {
	healthGroups.put(groupId, new HealthGroup(groupId, name, minNormalPulse, maxNormalPulse, serveyPeriod));
}

public static boolean removeHealthGroup(int groupId)
{
	if (!healthGroups.containsKey(groupId)) return false;
	healthGroups.remove(groupId);
	return true;
}
public static Map<Integer,HealthGroup> getHealthgroups(){
  return healthGroups;
}
public static void showListHealthGroupsNames()
{
	for (Map.Entry<Integer, HealthGroup> pair : healthGroups.entrySet()) {
		System.out.println(pair.getKey()+". "+pair.getValue().getGroupName());
		
	}
}
}
