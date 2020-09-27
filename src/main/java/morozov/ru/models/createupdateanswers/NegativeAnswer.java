package morozov.ru.models.createupdateanswers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NegativeAnswer extends Answer {

    private final List<String> errors = new ArrayList<>();

    public NegativeAnswer() {
    }

    public NegativeAnswer(Boolean success) {
        super(success);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrorMessage(String msg) {
        this.errors.add(msg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof NegativeAnswer)) return false;
        if (!super.equals(o)) return false;
        NegativeAnswer that = (NegativeAnswer) o;
        return Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), errors);
    }
}
