package codesquad.question;

import codesquad.answer.Answer;
import codesquad.exception.Result;
import codesquad.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false, length=100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String contents;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private boolean deleted;

    @OneToMany(mappedBy="question", cascade = CascadeType.REMOVE)
    @OrderBy("id ASC")
    private List<Answer> answers;

    private Question() {}

    private Question(User writer, String title, String contents) {
        super();
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
        this.deleted = false;
    }

    public static Question newInstance(User writer, String title, String contents) {
        return new Question(writer, title, contents);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getFormattedCreatedDate() {
        if (createdDate == null) return "";
        return createdDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getFormattedUpdatedDate() {
        if (updatedDate == null) return "";
        return updatedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    void update(Question updatedQuestion) {
        this.setTitle(updatedQuestion.getTitle());
        this.setContents(updatedQuestion.getContents());
        this.setUpdatedDate(LocalDateTime.now());
    }

    Result delete() {
        if (answers == null) this.deleted = true;
        for (Answer answer : answers) {
            if (!answer.isSameWriter(writer)) {
                if(answer.isDeleted()) continue;
                return Result.fail("Because the other user's answer exist, you can't edit the question.");
            }
        }
        for (Answer answer : answers) answer.delete();
        this.deleted = true;

        return Result.success();
    }

    public boolean isSameWriter(User sessionedUser) {
        return this.writer.equals(sessionedUser);
    }
}
