package tel_ran.hsa.entities.orm;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class ContainsId implements Serializable{
	private  int idDoctor;
	private int idPatient;

	public ContainsId(int idDoctor, int idPatient) {
		super();
		this.idDoctor = idDoctor;
		this.idPatient = idPatient;
	}

    protected ContainsId() {}
	public  int getIdDoctor() {
		return idDoctor;
	}

	public void setIdDoctor(int idDoctor) {
		this.idDoctor = idDoctor;
	}

	public  int getIdPatient() {
		return idPatient;
	}

	public  void setIdPatient(int idPatient) {
		this.idPatient = idPatient;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idDoctor;
		result = prime * result + idPatient;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContainsId other = (ContainsId) obj;
		if (idDoctor != other.idDoctor)
			return false;
		if (idPatient != other.idPatient)
			return false;
		return true;
	}
	
	
}
