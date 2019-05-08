package codesquad.qna.question;

import codesquad.qna.answer.Answer;
import codesquad.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false, length = 30)
    private String title;

    @Lob
    private String contents;
    private LocalDateTime createDate; // hibernate 5.2부터는 timestamp 위한 추가설정 필요x

    @OneToMany(mappedBy = "question")
    @OrderBy("id ASC")
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
        this.createDate = LocalDateTime.now();
        this.deleted = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFormattedCreateDate() {
        if (createDate == null) {
            return "";
        }
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
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
            if(!a.isDeleted()) {
                if(!a.isSameWriter(this.writer)) {
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
        return "question{" +
                "id='" + id + '\'' +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
