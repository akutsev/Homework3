package ru.otus.akutsev.studtesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import ru.otus.akutsev.studtesting.configs.YamlProps;
import ru.otus.akutsev.studtesting.service.StudentTestServiceImpl;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(YamlProps.class)
public class StudTestingApplication {

	public static void main(String[] args) throws IOException {
		ApplicationContext context = SpringApplication.run(StudTestingApplication.class, args);
		context.getBean(StudentTestServiceImpl.class).startStudentTest();
	}

}
