package se.fermitet.android.infektionsdagbok.model;

import java.io.Serializable;
import java.util.UUID;

public abstract class ModelObjectBase implements Serializable {

	protected UUID uuid;

	public ModelObjectBase() {
		super();

		this.uuid = UUID.randomUUID();
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.uuid == null) ? 0 : this.uuid.hashCode());
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
		ModelObjectBase other = (ModelObjectBase) obj;
		if (this.uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!this.uuid.equals(other.uuid))
			return false;
		return true;
	}



}
