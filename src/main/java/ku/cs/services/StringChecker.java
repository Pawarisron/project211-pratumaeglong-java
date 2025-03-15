package ku.cs.services;

public class StringChecker {

    //Method ตรวจสอบ String ว่า เป็น ตัวเลขทั้งหมดมั้ย
    public static boolean isAllNumeric(String s) {

        if (s == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    //Method ตรวจสอบ String ว่า เป็น ตัวอักษรตัวใหญ่ทั้งหมดมั้ย
    public static boolean isAllUpper(String s) {

        if (s == null) {
            return false;
        }
        for(char c : s.toCharArray()) {
            if(Character.isLetter(c) && Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }
    //Method ตรวจสอบ String ว่า เป็น ตัวอักษรตัวเล็กทั้งหมดมั้ย
    public static boolean isAllLower(String s) {

        if (s == null) {
            return false;
        }
        for(char c : s.toCharArray()) {
            if(Character.isLetter(c) && Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }
    //Method ตรวจสอบ String ว่า เป็น ตัวอักษรอย่างเดียวใช่มั้ย ห้ามมีตัวเลขผสม
    public static boolean isAllLetter(String s) {

        if (s == null) {
            return false;
        }
        for(char c : s.toCharArray()) {
            if( !(Character.isLetter(c)) ) {
                return false;
            }
        }
        return true;
    }


}
