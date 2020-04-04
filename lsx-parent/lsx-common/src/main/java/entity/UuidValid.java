package entity;

public class UuidValid {

    public boolean isValidUUID(String uuid) {
        // UUID校验
        if (uuid == null) {
            System.out.println("uuid is null");
        }
        String regex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
        if (uuid.matches(regex)) {
            return true;
        }
        return false;
    }

}
