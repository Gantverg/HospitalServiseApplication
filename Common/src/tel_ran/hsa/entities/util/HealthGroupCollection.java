package tel_ran.hsa.entities.util;

import java.util.*;

import tel_ran.hsa.entities.dto.*;

public class HealthGroupCollection {
	static Map<Integer, HealthGroup> healthGroups = new HashMap<>();

	public HealthGroupCollection() {
		addHealthgroup(1, "group1", 60, 90, 60);
		addHealthgroup(2, "group2", 70, 100, 360);
		addHealthgroup(3, "group3", 80, 110, 3600);

	}

	public static void addHealthgroup(int groupId, String name, int minNormalPulse, int maxNormalPulse,
			int surveyPeriod) {
		healthGroups.put(groupId, new HealthGroup(groupId, name, minNormalPulse, maxNormalPulse, surveyPeriod));
	}

	public static boolean removeHealthgroup(int groupId) {
		if (!healthGroups.containsKey(groupId))
			return false;
		healthGroups.remove(groupId);
		return true;
	}

	public static Map<Integer, HealthGroup> getHealthgroups() {
		return healthGroups;
	}

	public static HealthGroup getHealthgroup(int groupId) {
		return healthGroups.get(groupId);
	}
	
/*	public static void showListHealthGroupsNames() {
		for (Map.Entry<Integer, HealthGroup> pair : healthGroups.entrySet()) {

			System.out.println(pair.getValue() + ". " + pair.getKey());

		}
	}
*/}
