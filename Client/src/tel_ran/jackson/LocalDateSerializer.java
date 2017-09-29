package tel_ran.jackson;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalDateSerializer extends StdSerializer<LocalDate> {
public LocalDateSerializer(){
	super(LocalDate.class);
}

@Override
public void serialize(LocalDate date, JsonGenerator gen, SerializerProvider arg2) throws IOException {
	gen.writeString(date.toString());
	
}
}
