package ru.otus.akutsev.studtesting;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import ru.otus.akutsev.studtesting.service.StudentTestService;

import java.io.*;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@SpringBootTest
@EnableConfigurationProperties(YamlPropsTest.class)
class StudTestingApplicationTests {
	static ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	static ByteArrayInputStream inputStream;
	static InputStream sysInBackup;

	@Autowired
	StudentTestService studentTestService;
	@Autowired
	MessageSource messageSource;

	@BeforeAll
	private static void setUpStreams() {
		System.setOut(new PrintStream(outputStream));
		sysInBackup = System.in;
	}

	@BeforeEach
	private void cleanOutput() {
		outputStream.reset();
	}

	@Test
	void studentTestServiceTest_questionsMoreThanInBase() throws IOException {
		int questionsNum = 5;

		String input = "1147" + "\nbla-bla" + "\nbla-bla" + "\nbla-bla";
		inputStream = new ByteArrayInputStream(input.getBytes());
		System.setIn(inputStream);

		studentTestService.startStudentTest();

		String startSting = messageSource.getMessage("start.message",
				new Integer[] {questionsNum}, Locale.US);
		String[] expectedOutput = {startSting, "When Moscow city was founded?",
				"When Saint Petersberg city was founded?", "How many regions there are in Russia?",
				"What year did the first Woodstock festival was?",
				"No more question in base. Change questions number setting",
				"You failed :-("};
		Arrays.sort(expectedOutput);

		String[] actualOutput = outputStream.toString().split("\r\n");
		Arrays.sort(actualOutput);

		assertArrayEquals(expectedOutput, actualOutput);
	}

	@AfterAll
	private static void cleanUpStreams() {
		System.setOut(null);
		System.setIn(sysInBackup);
	}

}
