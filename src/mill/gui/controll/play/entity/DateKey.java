package mill.gui.controll.play.entity;

public class DateKey implements Comparable {
    
    private Integer year, month, day, type;
    private static final String[] MONTHS = new String[] {"Január", "Február", "Március", "Április", "Május", "Június", "Július", "Augusztus", "Szeptember", "Október", "November", "December"};
    
    public static final int YEAR = 1;
    public static final int MONTH = 2;
    public static final int DAY = 3;

    public DateKey(int year) {
        this(year, null, null, YEAR);
    }
    
    public DateKey(int year, int month) {
        this(year, month, null, MONTH);
    }
    
    public DateKey(int year, int month, int day) {
        this(year, month, day, DAY);
    }
    
    private DateKey(Integer year, Integer month, Integer day, int type) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.type = type;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    @Override
    public String toString() {
        switch (getType()) {
            case YEAR:
                return Integer.toString(getYear());
            case MONTH:
                return MONTHS[getMonth()];
            case DAY:
                return Integer.toString(getDay());
            default:
                return super.toString();
        }
    }

    public int getType() {
        return type;
    }

    @Override
    public int compareTo(Object o) {
        try {
            DateKey dk = (DateKey) o;
            if (dk.getType() != getType()) return -1;
            boolean b = true;
            b &= getYear().equals(dk.getYear());
            if (getType() != YEAR) b &= getMonth().equals(dk.getMonth());
            if (getType() == DAY) b &= getDay().equals(dk.getDay());
            return b ? 0 : -1;
        }
        catch(Exception ex) {
            return -1;
        }
    }
    
}