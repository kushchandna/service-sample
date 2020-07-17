package com.kush.apps.commons.api;

public class Identifier {

    private final Object id;

    public static Identifier id(Object id) {
        return new Identifier(id);
    }

    private Identifier(Object id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Id [" + id + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Identifier other = (Identifier) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }


}
