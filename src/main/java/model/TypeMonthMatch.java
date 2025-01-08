package model;

public class TypeMonthMatch {
    private String Type, Month;
    private int Count;
    public TypeMonthMatch(String type, String month, int count) {
        this.Type = type;
        this.Month = month;
        this.Count = count;
    }
    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    @Override
    public String toString() {
        return String.valueOf(Count);
    }
}
