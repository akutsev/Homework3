package ru.otus.akutsev.studtesting.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.otus.akutsev.studtesting.configs.YamlProps;
import ru.otus.akutsev.studtesting.model.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionsDaoCsv {

	private final YamlProps yamlProps;

	@Autowired
	public QuestionsDaoCsv(YamlProps yamlProps) {
		this.yamlProps = yamlProps;
	}

	private static final String CSV_SPLITERATOR = ";";
	private static final int COLUMN_NUM_QUESTION_ID = 0;
	private static final int COLUMN_NUM_QUESTION_TEXT = 1;
	private static final int COLUMN_NUM_QUESTION_ANSWER = 2;

	public List<Question> getQuestions() throws IOException {
		List<Question> output = new ArrayList<>();

		BufferedReader reader;
		String csvString;

		try {
			File questionsBase = ResourceUtils.getFile(yamlProps.getQuestionsBasePath());
			reader = new BufferedReader(new FileReader(questionsBase));

			while ((csvString = reader.readLine()) != null) {
				output.add(rowMapper(csvString));
			}

		} catch (RuntimeException e) {
			System.out.println("Wrong questions base file format, check it");
		}

		return output;
	}

	private Question rowMapper(String csvString) {
		String[] splitedCsvString = csvString.split(CSV_SPLITERATOR);
		String id = splitedCsvString[COLUMN_NUM_QUESTION_ID];
		String text = splitedCsvString[COLUMN_NUM_QUESTION_TEXT];
		String answer = splitedCsvString[COLUMN_NUM_QUESTION_ANSWER];

		return new Question(id, text, answer);
	}
}
