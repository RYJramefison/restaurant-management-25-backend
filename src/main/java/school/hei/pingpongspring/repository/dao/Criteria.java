package school.hei.pingpongspring.repository.dao;

public class Criteria {
    private String column;
    private Object value;

    public Criteria(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }
}