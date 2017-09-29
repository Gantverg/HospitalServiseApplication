package tel_ran.random;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class RandomGenerator {
private Random gen=new Random();
public Integer getRandomInteger(Integer min, Integer max){
	return min + gen.nextInt(max-min);
}
public Double getRandomDouble(Double min, Double max){
	return min + gen.nextDouble()*(max-min);
}
public Float getRandomFloat(Float min, Float max){
	return min + gen.nextFloat()*(max-min);
}
public Long getRandomLong(Long min, Long max){
	return getRandomDouble(min.doubleValue(),max.doubleValue()).longValue();
}
public String getRandomAsciiString(int minLength, int maxLength){
	//String contains only lower case ascii letters (no digits, no other symbols)
	if(maxLength <= minLength)
		return "";
	int length=getRandomInteger(minLength, maxLength);
	byte[]characters=new byte[length];
	for(int i=0; i<length; i++){
		characters[i]=getRandomInteger((int)'a', 'z'+1).byteValue();
	}
	return new String(characters);
}
public LocalDate getRandomDate(LocalDate from, LocalDate to){
	int period=gen.nextInt((int)ChronoUnit.DAYS.between(from, to));
	return from.plusDays(period);
}
}
