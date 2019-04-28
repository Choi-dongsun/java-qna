package codesquad.qna;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class QnaController {
    List<Question> questions = new ArrayList<>();

    @GetMapping("/form")
    public String form() {
        return "/qna/form";
    }

    @PostMapping("")
    public String create(Question question) {
        questions.add(question);
        System.out.println(Arrays.toString(questions.toArray()));
        return "redirect:/";
    }
}
