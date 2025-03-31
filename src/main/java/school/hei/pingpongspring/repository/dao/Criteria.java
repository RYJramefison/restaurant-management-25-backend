package school.hei.pingpongspring.repository.dao;

public class Criteria {
    private CriteriaAccepted column;
    private Object value;

    public Criteria(CriteriaAccepted column, Object value) {
        this.column = column;
        this.value = value;
    }

    public CriteriaAccepted getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }
}
