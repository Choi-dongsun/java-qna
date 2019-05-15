package codesquad.web;

import codesquad.domain.qna.Question;
import codesquad.domain.qna.QuestionRepository;
import codesquad.util.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("")
    public String list(Model model, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), 4,  new Sort(Sort.Direction.DESC, "id"));

        Page<Question> pages = questionRepository.findAllByDeleted(false, pageRequest);
        Paging paging = new Paging(pageable.getPageNumber(), pages.getTotalPages() - 1);

        model.addAttribute("paging", paging);
        model.addAttribute("questions", pages);
        return "/index";
    }
}
