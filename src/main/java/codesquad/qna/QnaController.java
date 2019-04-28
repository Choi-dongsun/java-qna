package codesquad.qna;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
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
        question.setId(questions.size() + 1);
        questions.add(question);
        return "redirect:/";
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("questions", questions);
        return "/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable String id, Model model) {
        model.addAttribute("question" , questions.get(Integer.parseInt(id) - 1));
        return "/qna/show";
    }
}
