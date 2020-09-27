package morozov.ru.models.createupdateanswers;

import java.util.Objects;

public class Answer {

    private Boolean success;

    public Answer() {
    }

    public Answer(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return Objects.equals(success, answer.success);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success);
    }
}
