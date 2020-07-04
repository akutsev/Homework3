package ru.otus.akutsev.studtesting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.akutsev.studtesting.configs.YamlProps;
import ru.otus.akutsev.studtesting.dao.QuestionsDaoCsv;
import ru.otus.akutsev.studtesting.model.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class StudentTestServiceImpl implements StudentTestService{
	private final QuestionsDaoCsv questionsDaoCsv;
	private final YamlProps yamlProps;
	private final MessageSource messageSource;

	@Autowired
	public StudentTestServiceImpl(QuestionsDaoCsv questionsDaoCsv, YamlProps yamlProps, MessageSource messageSource) {
		this.questionsDaoCsv = questionsDaoCsv;
		this.yamlProps = yamlProps;
		this.messageSource = messageSource;
	}

	public void startStudentTest() throws IOException {
		printStartMessage();

		List<Question> questions = questionsDaoCsv.getQuestions();
		int correctAnswersCount = 0;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String studentAnswer;

		try {
			for (int i =0; i < yamlProps.getQuestionsNumber(); i++) {
				System.out.println(questions.get(i).getText());
				studentAnswer = reader.readLine();
				if (questions.get(i).getAnswer().equals(studentAnswer)) {
					correctAnswersCount++;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println(yamlProps.getNoMoreQuestionsMessage());
		}


		printResult(correctAnswersCount);
	}

	private void printStartMessage() {
		String message = messageSource.getMessage("start.message",
				new Integer[] {yamlProps.getQuestionsNumber()}, yamlProps.getLocale());
		System.out.println(message);
	}

	private void printResult(int correctAnswersNumber) {
		String message = correctAnswersNumber >= yamlProps.getRequiredCorrectAnswers()
				? yamlProps.getSuccessMessage() : yamlProps.getFailMessage();
		System.out.println(message);
	}

}
