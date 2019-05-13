package codesquad.domain.qna;

import codesquad.domain.user.User;
import support.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Question extends AbstractEntity {
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false, length = 30)
    private String title;

    @Lob
    private String contents;

    @OneToMany(mappedBy = "question")
    @OrderBy("id ASC")
    @JsonIgnore
    private List<Answer> answers;

    private boolean deleted;

    private int countOfAnswer = 0;

    public Question() {
    }

    public Question(User writer, String title, String contents) {
        super();
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.deleted = false;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public int getCountOfAnswer() {
        return countOfAnswer;
    }

    public void setCountOfAnswer(int countOfAnswer) {
        this.countOfAnswer = countOfAnswer;
    }

    public void update(Question updatedQuestion) {
        this.title = updatedQuestion.title;
        this.contents = updatedQuestion.contents;
    }

    public boolean checkDeletePossibility() {
        if (answers.size() == 0) return true;
        for (Answer a : answers) {
            if (!a.isDeleted()) {
                if (!a.isSameWriter(this.writer)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void delete() {
        this.deleted = true;
        for (Answer answer : answers) {
            answer.delete();
        }
        deleteAllAnswer();
    }

    public boolean isSameWriter(User loginUser) {
        return this.writer.equals(loginUser);
    }

    public void addAnswer() {
        this.countOfAnswer++;
    }

    public void deleteAnswer() {
        this.countOfAnswer--;
    }

    public void deleteAllAnswer() {
        this.countOfAnswer = 0;
    }

    @Override
    public String toString() {
        return "Question{" +
                super.toString() +
                "writer=" + writer +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", answers=" + answers +
                ", deleted=" + deleted +
                ", countOfAnswer=" + countOfAnswer +
                '}';
    }
}
