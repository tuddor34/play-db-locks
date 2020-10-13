package play.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import play.domain.StudentEntity;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    @GetMapping
    public List<StudentEntity> getStudents() {
        return new ArrayList<>();
    }
}
